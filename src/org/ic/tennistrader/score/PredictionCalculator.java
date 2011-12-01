package org.ic.tennistrader.score;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;

public class PredictionCalculator {

	public static double[] calculate(Match match) {
		double[] result = new double[8];

		Statistics playerOneStats = match.getPlayerOne().getStatistics();
		Statistics playerTwoStats = match.getPlayerTwo().getStatistics();
		PlayerEnum server = match.getServer();
		Score score = match.getScore();

		// Probability of player one/two to win on their serve in a game by
		// ability
		result[4] = calculatePWG(calculatePWOS(playerOneStats));
		result[5] = calculatePWG(calculatePWOS(playerTwoStats));

		// Even indices correspond to player 1, odd ones to player 2;
		result[0] = calculatePWOS(playerOneStats);
		result[1] = calculatePWOS(playerTwoStats);

		if (server == PlayerEnum.PLAYER1) {
			result[2] = calculateGamePercent(convertPoints(score.getPlayerOnePoints()),
					convertPoints(score.getPlayerTwoPoints()), result[0], 1);
			result[3] = 1.0 - result[2];
		} else {
            result[3] = calculateGamePercent(convertPoints(score.getPlayerOnePoints()),
                    convertPoints(score.getPlayerTwoPoints()),  1 - result[1], 0);
		    result[2] = 1.0 - result[3];
		}

		return result;
	}

	// convert from standard points (e.g. 15, 30 ... ) to short points (1,2..)
	private static int convertPoints(int points) {
		return (int)Math.floor(points/15.0);
	}

	// Probability of winning a point on service
	// P (win) = P (noFault)P (win|noFault) + (1 − P (noFault))P (win|Fault)
	private static double calculatePWOS(Statistics serverStats) {
		return (serverStats.getFirstServePercent()
				* serverStats.getFirstServeWins() + (1 - serverStats
				.getFirstServePercent()) * serverStats.getSecondServeWins());
	}

	private static double po(double a, double b){
	    return Math.pow(a,b);
	}
	// Calculating the chance of one player to win a game he/she's serving for
	// based on ability (ind. of scoreline)
	private static double calculatePWG(double p) {
		double result = 0;
		//result = po(p, 4) + 4 * po(p,4)
		result = 15 - 24 * p + 10 * Math.pow(p, 2)
				+ (20 * p * Math.pow(1 - p, 3) / (1 - 2 * p * (1 - p)));
		return (result * Math.pow(p, 4));
	}

	private static double calculateGamePercent(int a, int b, double pwg, int c) {
		// pwg = (server == PlayerEnum.PLAYER1)? playerOnePWG : playerTwoPWG;
		if (a == 4 && b <= 2)
			return c;
		if (b == 4 && a <= 2)
			return 1-c;
		if (a == 3 && b == 3)
			return (Math.pow(pwg, 2) / (Math.pow(pwg, 2) + Math.pow(1 - pwg, 2)));
		return (pwg * calculateGamePercent(a + 1, b, pwg,c) + (1 - pwg)
				* calculateGamePercent(a, b + 1, pwg,c));
	}

	private static int calculateSetPercent(int a, int b) {
		return 0;
	}

	private static int calculateMatchPercent() {
		return 0;
	}
}
