package org.ic.tennistrader.score;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;
import org.ic.tennistrader.model.prediction.StatisticMeasures;

public class PredictionCalculator {

	private static double pwgA;
	private static double pwgB;
	private static double pwA;
	private static double pwB;
	// By default have final set tiebreak, not advantage set
	private static int TIEBREAK = 1;

	public static double[] getP1Data(double result[]) {
		double[] res = new double[4];
		for (int i = 0; i < res.length; i++) {
			res[i] = result[2 * i];
		}
		return res;
	}

	public static double[] getP2Data(double result[]) {
		double[] res = new double[4];
		for (int i = 0; i < res.length; i++) {
			res[i] = result[2 * i + 1];
		}
		return res;
	}

	public PredictionCalculator(Match match) {
		initialPWOS(match);
	}

	private void initialPWOS(Match match) {
		Statistics playerOneStats = match.getPlayerOne().getStatistics();
		Statistics playerTwoStats = match.getPlayerTwo().getStatistics();
		pwA = calculatePWOS(playerOneStats);
		pwB = calculatePWOS(playerTwoStats);
	}

	// Shahin, you can use this method, or the PWOS recalibrated one, for the
	// chart display of odds,
	// don't know which provides better overlay with actual odds (surprisingly
	// nobody tried it),
	// but I obviously assume the recalibrated one (and I can barely want to see
	// the overlay over a match's duration)
	public double[] calculateOddsWithStaticPWOS(Match match) {
		double[] result = new double[2];
		double[] res = calculate(match);
		result[0] = res[8];
		result[1] = res[9];
		return result;
	}

	public double[] calculateOddsWithRecalibratedPWOS(Match match, int avgPeriod) {
		double[] result = new double[2];
		recalibratePWOS(match, avgPeriod);
		double[] res = calculate(match);
		// player 1
		result[0] = res[8];
		// player 2
		result[1] = res[9];
		return result;
	}

	public double[] calculate(Match match) {
		double[] result = new double[10];

		PlayerEnum server = match.getServer();
		Score score = match.getScore();

		// ***NOTE: VERY IMPORTANT - Indice 1 corresponds to player 1, 0 to
		// player 2 in all functions;
		// CONVENTIONS - Even indices in result corespond to player 1 results,
		// odd ones to player 2
		result[0] = pwA;
		result[1] = pwB;
		int[] points = convertPoints(score.getPlayerOnePoints(),
				score.getPlayerTwoPoints());
		int pOne = points[0];
		int pTwo = points[1];
		pwgA = calculateGamePercent(pOne, pTwo, result[0], 1);
		pwgB = calculateGamePercent(pTwo, pOne, result[1], 1);

		if (server == PlayerEnum.PLAYER1) {
			result[2] = calculateGamePercent(pOne, pTwo, result[0], 1);
			result[3] = 1.0 - result[2];

			result[4] = calculateCurrentSetPercent(score.getCurrentSetScore()
					.getPlayerOneGames(), score.getCurrentSetScore()
					.getPlayerTwoGames(), pwA, pwB, 1);
			result[5] = 1 - result[4];

			result[6] = calculateWinMatch(result[4], score.getPlayerOneSets(),
					score.getPlayerTwoSets(), pwA, pwB, 1,
					score.getMaximumSetsPlayed(), TIEBREAK);
			result[7] = 1 - result[6];

			// ODDS: [8]-player1, [9] - player2
			result[8] = 1 / result[6];
			result[9] = 1 / result[7];
		} else {
			result[3] = calculateGamePercent(pTwo, pOne, result[1], 1);
			result[2] = 1.0 - result[3];

			result[5] = calculateCurrentSetPercent(score.getCurrentSetScore()
					.getPlayerOneGames(), score.getCurrentSetScore()
					.getPlayerTwoGames(), pwA, pwB, 0);
			result[4] = 1 - result[5];

			result[7] = calculateWinMatch(result[5], score.getPlayerOneSets(),
					score.getPlayerTwoSets(), pwA, pwB, 0,
					score.getMaximumSetsPlayed(), TIEBREAK);
			result[6] = 1 - result[7];

			// ODDS: [8]-player1, [9] - player2
			result[8] = 1 / result[6];
			result[9] = 1 / result[7];
		}

		DecimalFormat df = new DecimalFormat("#.###");
		for (int i = 0; i < 10; i++) {
			result[i] = Double.parseDouble(df.format(result[i]));
		}

		return result;
	}

	// Recalibrate model PWOS so it is closest to the average PWOS the actual
	// market odds would indicate
	// Models assumes that PWOS perceived by market changes only for the player
	// serving
	// avgPeriod should be 7s
	public void recalibratePWOS(Match match, int avgPeriod) {

		PlayerEnum server = match.getServer();

		double actualOdds = StatisticMeasures.Avg(getOddsInTimePeriod(match,
				avgPeriod));
		double oddsTemp = 0;
		double errorOld = actualOdds;

		// Assume that a current professional player's pwos can fluctuate with
		// +- 0.05 after a point
		// In prof tennis a player's statistical pw always > 0.05, so no
		// boundary conditions

		if (server == PlayerEnum.PLAYER1) {
			pwA = pwA - 0.05;

			// iterate 10 times, searching for the pwos giving the closest value
			// to the actual odds
			while (Math.abs(actualOdds - oddsTemp) <= errorOld) {
				errorOld = Math.abs(actualOdds - oddsTemp);
				// Calculate 1/match-winning probability(odds) for player 1:
				oddsTemp = calculate(match)[8];
				pwA = pwA + 0.01;
			}
		} else {
			pwB = pwB - 0.05;
			// iterate max 10 times, searching for the pwos giving the closest
			// value to the actual odds
			while (Math.abs(actualOdds - oddsTemp) <= errorOld) {
				errorOld = Math.abs(actualOdds - oddsTemp);
				// Calculate 1/match-winning probability(odds) for player 2:
				oddsTemp = calculate(match)[9];
				pwB = pwB + 0.01;
			}
		}
	}

	public static ArrayList<Double> getOddsInTimePeriod(Match match,
			int avgPeriod) {
		PlayerEnum server = match.getServer();
		List<MOddsMarketData> marketDatas = new ArrayList<MOddsMarketData>();
		ArrayList<Double> OddsLastSecs = new ArrayList<Double>();
		marketDatas = match.getMarketDatas();
		MOddsMarketData lastData = match.getLastMarketData();
		Iterator iter = marketDatas.iterator();

		// determine first odds data object within the avgPeriod of the last one
		while (iter.hasNext()) {
			MOddsMarketData data = (MOddsMarketData) iter.next();
			if ((lastData.getDelay() - data.getDelay()) <= avgPeriod) {
				OddsLastSecs.add(data.getLastPriceMatched(server));
				break;
			}
		}
		while (iter.hasNext()) {
			double value = ((MOddsMarketData) iter.next())
					.getLastPriceMatched(server);
			OddsLastSecs.add(value);
		}

		return OddsLastSecs;
	}

	// Convert from standard points (e.g. 15, 30 ... ) to short points (1,2..)
	private static int[] convertPoints(int pointsOne, int pointsTwo) {
		if (pointsOne >= 40 && pointsTwo >= 40) {
			int[] pts = { (int) Math.floor(pointsOne / 15.0),
					(int) Math.floor(pointsTwo / 15.0) };
			return pts;
		}
		int[] pts = { (int) Math.ceil(pointsOne / 15.0),
				(int) Math.ceil(pointsTwo / 15.0) };
		return pts;
	}

	// Probability of winning a point on service
	// P (win) = P (noFault)P (win|noFault) + (1 − P (noFault))P (win|Fault)
	private static double calculatePWOS(Statistics serverStats) {
		// for the case a player's statistics are not retrieved correctly
		if (serverStats.getFirstServePercent() >= 1
				|| serverStats.getFirstServePercent() <= 0)
			return 0.6;
		return (serverStats.getFirstServePercent()
				* serverStats.getFirstServeWins() + (1 - serverStats
				.getFirstServePercent()) * serverStats.getSecondServeWins());
	}

	// Calculate current game winning % prob
	public static double calculateGamePercent(int a, int b, double pw, int c) {
		if (a > 4 || b > 4)
			return -1; // not normally reachable code
		if (a == 4 && b <= 2)
			return c;
		if (b == 4 && a <= 2)
			return 1 - c;
		if (a == 3 && b == 3)
			return (Math.pow(pw, 2) / (Math.pow(pw, 2) + Math.pow(1 - pw, 2)));
		return (pw * calculateGamePercent(a + 1, b, pw, c) + (1 - pw)
				* calculateGamePercent(a, b + 1, pw, c));
	}

	// assumes player is serving in the tiebreak at the moment of calculation
	public static double calculateTiebreakerGamePercent(int a, int b,
			double pwA, double pwB, int c) {
		if (a == 7 && b >= 0 && b <= 5)
			return 1;
		else if (b == 7 && a >= 0 && a <= 5)
			return 0;
		else if (a == 6 && b == 6) {
			if (c == 1)
				return pwA * (1 - pwB) / (pwA * (1 - pwB) + (1 - pwA) * pwB);
			else
				return pwB * (1 - pwA) / (pwA * (1 - pwB) + (1 - pwA) * pwB);
		}
		// Enforcing Barnet*'s theorems: If player A is serving, he has the same
		// probability of winning
		// a tiebreaker game from all points (n + 1, n) or (n,n) or (n, n+1),
		// where n ≥ 5.
		else if (a + b > 12) {
			return -1;
		}

		if (c == 1)
			return pwA
					* calculateTiebreakerGamePercent(a + 1, b, pwA, pwB,
							(a + b) % 2)
					+ (1 - pwA)
					* calculateTiebreakerGamePercent(a, b + 1, pwA, pwB,
							(a + b) % 2);
		else
			return pwB
					* calculateTiebreakerGamePercent(a, b + 1, pwA, pwB,
							(a + b) % 2)
					+ (1 - pwB)
					* calculateTiebreakerGamePercent(a + 1, b, pwA, pwB,
							(a + b) % 2);
	}

	// A set with tiebreaker
	private static double calculateSetPercent(int c, int d, double pwA,
			double pwB, int s) {
		if ((c == 6 && d >= 0 && d <= 4) || (c == 7 && d == 5)) {
			return 1;
		}
		if ((d == 6 && c >= 0 && c <= 4) || (d == 7 && c == 5)) {
			return 0;
		}
		if (c == 6 && d == 6) {
			if (s == 1)
				return calculateTiebreakerGamePercent(0, 0, pwA, pwB, 1);
			else
				return 1.0 - calculateTiebreakerGamePercent(0, 0, pwB, pwA, 1);
		}
		if (s == 1) {
			return calculateGamePercent(0, 0, pwA, 1)
					* calculateSetPercent(c + 1, d, pwA, pwB, 0)
					+ (1 - calculateGamePercent(0, 0, pwA, 1))
					* calculateSetPercent(c, d + 1, pwA, pwB, 0);
		} else
			return calculateGamePercent(0, 0, pwB, 1)
					* (calculateSetPercent(c, d + 1, pwA, pwB, 1 - s))
					+ (1 - calculateGamePercent(0, 0, pwB, 1))
					* (calculateSetPercent(c + 1, d, pwA, pwB, 1 - s));
	}

	// An advantage set
	private static double calculateAdvSetPercent(int c, int d, double pwA,
			double pwB, int s) {
		if ((c == 6 && d >= 0 && d <= 4)) {
			return 1;
		}
		if ((d == 6 && c >= 0 && c <= 4)) {
			return 0;
		}
		if (c == 5 && d == 5) {
			if (s == 1) {
				return calculateGamePercent(0, 0, pwA, 1)
						* (1 - calculateGamePercent(0, 0, pwB, 1))
						/ (calculateGamePercent(0, 0, pwA, 1)
								* (1 - calculateGamePercent(0, 0, pwB, 1)) + calculateGamePercent(
								0, 0, pwB, 1)
								* (1 - calculateGamePercent(0, 0, pwA, 1)));
			} else {
				return 1
						- calculateGamePercent(0, 0, pwB, 1)
						* (1 - calculateGamePercent(0, 0, pwA, 1))
						/ (calculateGamePercent(0, 0, pwB, 1)
								* (1 - calculateGamePercent(0, 0, pwA, 1)) + calculateGamePercent(
								0, 0, pwA, 1)
								* (1 - calculateGamePercent(0, 0, pwB, 1)));
			}
		}
		if (c + d > 10)
			return -1;
		if (s == 1) {
			return calculateGamePercent(0, 0, pwA, 1)
					* (calculateAdvSetPercent(c + 1, d, pwA, pwB, 0))
					+ (1 - calculateGamePercent(0, 0, pwA, 1))
					* (calculateAdvSetPercent(c, d + 1, pwA, pwB, 0));
		} else {
			return calculateGamePercent(0, 0, pwB, 1)
					* (calculateAdvSetPercent(c, d + 1, pwA, pwB, 1))
					+ (1 - calculateGamePercent(0, 0, pwB, 1))
					* (calculateAdvSetPercent(c + 1, d, pwA, pwB, 1));
		}

	}

	// Calculate % prob of winning the current set
	public static double calculateCurrentSetPercent(int c, int d, double pwA,
			double pwB, int s) {
		double winSetGameWon, winSetGameLost, winGame;
		if (s == 1) {
			winSetGameWon = calculateSetPercent(c + 1, d, pwA, pwB, s);
			winSetGameLost = calculateSetPercent(c, d + 1, pwA, pwB, s);
			winGame = pwgA;
		} else {
			winSetGameWon = 1 - calculateSetPercent(c, d + 1, pwA, pwB, 1);
			winSetGameLost = 1 - calculateSetPercent(c + 1, d, pwA, pwB, 1);
			winGame = pwgB;
		}
		return winGame * winSetGameWon + (1 - winGame) * winSetGameLost;
	}

	// Computes the match percent of player to win the match
	private static double calculateMatchPercent(int setScoreOne,
			int setScoreTwo, double pwA, double pwB, int maxSets, int tiebreak) {
		int score = (int) Math.round(Math.floor(maxSets / 2) + 1);
		if (setScoreOne >= score && setScoreTwo >= score)
			return -1;
		if (setScoreOne >= score)
			return 1;
		if (setScoreTwo >= score)
			return 0;

		double winSet = calculateSetPercent(0, 0, pwA, pwB, 1);

		// Choose between advantage or tiebreak final set
		if (setScoreOne == score - 1 && setScoreTwo == score - 1) {
			if (tiebreak == 0)
				return calculateAdvSetPercent(0, 0, pwA, pwB, 1);
			else
				return calculateSetPercent(0, 0, pwA, pwB, 1);
		}
		return (winSet
				* calculateMatchPercent(setScoreOne + 1, setScoreTwo, pwA, pwB,
						maxSets, tiebreak) + (1 - winSet)
				* calculateMatchPercent(setScoreOne, setScoreTwo + 1, pwA, pwB,
						maxSets, tiebreak));
	}

	// Calculate inverted odds of match
	public static double calculateWinMatch(double winCurSet, int setScoreOne,
			int setScoreTwo, double pwA, double pwB, int s, int maxSets,
			int tiebreak) {
		double winMatchSetWon, winMatchSetLost;
		if (s == 1) {
			winMatchSetWon = calculateMatchPercent(setScoreOne + 1,
					setScoreTwo, pwA, pwB, maxSets, tiebreak);
			winMatchSetLost = calculateMatchPercent(setScoreOne,
					setScoreTwo + 1, pwA, pwB, maxSets, tiebreak);
		} else {
			winMatchSetWon = 1 - calculateMatchPercent(setScoreOne,
					setScoreTwo + 1, pwA, pwB, maxSets, tiebreak);
			winMatchSetLost = 1 - calculateMatchPercent(setScoreOne + 1,
					setScoreTwo, pwA, pwB, maxSets, tiebreak);
		}
		return winCurSet * winMatchSetWon + (1 - winCurSet) * winMatchSetLost;
	}

	// For testing purposes only
	public void setPWG(int s, double value) {
		if (s == 1)
			pwgA = value;
		else
			pwgB = value;
	}

}