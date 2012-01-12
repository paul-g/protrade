package org.ic.tennistrader.score;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PredictionCalculatorTest {

	private Match match;

	@Before
	public void setUp() {
		String filename = "data/test/fracsoft-reader/tso-fed.csv";
		match = new HistoricalMatch(filename);
		Statistics statsOne = new Statistics(0.615, 0.781, 0.525);
		match.getPlayerOne().setStatistics(statsOne);
		Statistics statsTwo = new Statistics(0.634, 0.785, 0.563);
		match.getPlayerTwo().setStatistics(statsTwo);
	}

	@After
	public void tearDown() {
		match = null;
	}

	@Test
	public void pointPrediction() {
		double[] expected = { 0.682, 0.704 };
		assertEquals(0, Double.compare(
				new PredictionCalculator(match).calculate(match)[0],
				expected[0]));
		assertEquals(0, Double.compare(
				new PredictionCalculator(match).calculate(match)[1],
				expected[1]));
	}

	@Test
	public void GamePrediction() {
		// Tables of expected values from *Wozniak, *Barnett
		// expected values for a game at any score with first player serving
		double[] GameExpMatrix = { 0.781, 0.629, 0.417, 0.177, 0, 0.873, 0.757,
				0.562, 0.284, 0, 0.944, 0.875, 0.731, 0.455, 0, 0.986, 0.962,
				0.899, 0.731, -1, 1, 1, 1, -1, -1, };
		// first player serves in tiebreak
		double[] TbGameExpMatrix = { 0.53, 0.44, 0.29, 0.20, 0.10, 0.04, 0.01,
				0, 0.67, 0.53, 0.43, 0.27, 0.17, 0.07, 0.02, 0, 0.76, 0.68,
				0.53, 0.42, 0.24, 0.13, 0.03, 0, 0.87, 0.77, 0.69, 0.53, 0.40,
				0.20, 0.08, 0, 0.93, 0.89, 0.80, 0.72, 0.52, 0.37, 0.13, 0,
				0.98, 0.95, 0.92, 0.83, 0.75, 0.52, 0.32, 0, 0.99, 0.99, 0.98,
				0.96, 0.89, 0.82, 0.52, -1, 1, 1, 1, 1, 1, 1, -1, -1 };
		// second player serves in tiebreak
		double[] TbGameExpMatrixSnd = { 0.53, 0.39, 0.29, 0.17, 0.10, 0.03,
				0.01, 0, 0.62, 0.53, 0.37, 0.27, 0.14, 0.07, 0.01, 0, 0.76,
				0.63, 0.53, 0.35, 0.24, 0.10, 0.03, 0, 0.83, 0.77, 0.63, 0.53,
				0.33, 0.20, 0.05, 0, 0.93, 0.86, 0.80, 0.65, 0.52, 0.29, 0.13,
				0, 0.97, 0.95, 0.89, 0.83, 0.67, 0.52, 0.21, 0, 0.99, 0.99,
				0.98, 0.93, 0.89, 0.71, 0.52, 2, 1, 1, 1, 1, 1, 1, 2, 2 };
		DecimalFormat df = new DecimalFormat("#.###");
		DecimalFormat df2 = new DecimalFormat("#.##");

		// Testing library functions: calculateGamePercent,
		// calculateTiebreakerGamePercent
		try {
			PredictionCalculator calc = new PredictionCalculator(match);
			Class[] args1 = new Class[4];
			args1[0] = Integer.TYPE;
			args1[1] = Integer.TYPE;
			args1[2] = Double.TYPE;
			args1[3] = Integer.TYPE;
			Method method = PredictionCalculator.class.getDeclaredMethod(
					"calculateGamePercent", args1);
			method.setAccessible(true);
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					Object[] values = { new Integer(i), new Integer(j),
							new Double(0.6227), new Integer(1) };
					Double res = Double.parseDouble(df.format(method.invoke(
							calc, values)));
					// System.out.println("RES"+res +
					// "EXP"+GameExpMatrix[i*5+j]);
					assertEquals(0,
							Double.compare(res, GameExpMatrix[i * 5 + j]));
				}
			}

			Object[] value = { new Integer(0), new Integer(1),
					new Double(0.60), new Integer(1) };
			Double res2 = Double.parseDouble(df.format(method.invoke(calc,
					value)));
			assertEquals(0, Double.compare(res2, 0.576));

			Class[] args2 = new Class[5];
			args2[0] = Integer.TYPE;
			args2[1] = Integer.TYPE;
			args2[2] = Double.TYPE;
			args2[3] = Double.TYPE;
			args2[4] = Integer.TYPE;
			Method method2 = PredictionCalculator.class.getDeclaredMethod(
					"calculateTiebreakerGamePercent", args2);

			// The conditional probabilities of player A winning the tiebreaker
			// game
			// from various score lines for pA = 0.62 and pB = 0.60, and player
			// A serving
			method.setAccessible(true);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Object[] values = { new Integer(i), new Integer(j),
							new Double(0.62), new Double(0.60), new Integer(1) };
					Double res = Double.parseDouble(df2.format(method2.invoke(
							new PredictionCalculator(match), values)));
					// System.out.println("RES"+res +
					// "EXP"+TbGameExpMatrix[i*8+j]);
					assertEquals(0,
							Double.compare(res, TbGameExpMatrix[i * 8 + j]));
				}
			}
			// The conditional probabilities of player A winning the tiebreaker
			// game
			// from various score lines for pA = 0.62 and pB = 0.60, and player
			// B serving
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Object[] values = { new Integer(j), new Integer(i),
							new Double(0.60), new Double(0.62), new Integer(1) };
					Double res = Double.parseDouble(df2
							.format(1 - (Double) method2.invoke(
									new PredictionCalculator(match), values)));
					// System.out.println("RES"+res +
					// "EXP"+TbGameExpMatrixSnd[i*8+j]);
					assertEquals(0,
							Double.compare(res, TbGameExpMatrixSnd[i * 8 + j]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void SetPrediction() {
		double[] expMatrix = { 0.456, 0.232, 0.178, 0.05, 0.026, 0.002, 0,
				0.518, 0.459, 0.213, 0.153, 0.032, 0.011, 0, 0.764, 0.528,
				0.463, 0.187, 0.12, 0.014, 0, 0.83, 0.799, 0.54, 0.466, 0.149,
				0.072, 0, 0.962, 0.871, 0.844, 0.555, 0.47, 0.092, 0, 0.987,
				0.984, 0.925, 0.907, 0.576, 0.473, 0.093, 1, 1, 1, 1, 1, 0.58,
				0.478 };
		DecimalFormat df = new DecimalFormat("#.###");

		// Testing library function
		try {
			PredictionCalculator calc = new PredictionCalculator(match);
			Class[] args = new Class[5];
			args[0] = Integer.TYPE;
			args[1] = Integer.TYPE;
			args[2] = Double.TYPE;
			args[3] = Double.TYPE;
			args[4] = Integer.TYPE;
			Method method = PredictionCalculator.class.getDeclaredMethod(
					"calculateSetPercent", args);
			method.setAccessible(true);
			// Player 1 starts the set, therefore at odd scores i+j he is
			// serving
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					Object[] values = { new Integer(i), new Integer(j),
							new Double(0.6227), new Double(0.6359),
							new Integer(1 - (i + j) % 2) };
					Double res = Double.parseDouble(df.format(method.invoke(
							calc, values)));
					// System.out.println("At score "+i+"-"+j+ " RES ="+res +
					// " EXP="+expMatrix[i*7+j]);
					assertEquals(0, Double.compare(res, expMatrix[i * 7 + j]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void AdvSetPrediction() {
		double[] expMatrix = { 0.57, 0.50, 0.27, 0.19, 0.05, 0.02, 0, 0.77,
				0.57, 0.49, 0.23, 0.15, 0.02, 0, 0.83, 0.78, 0.56, 0.47, 0.19,
				0.09, 0, 0.94, 0.85, 0.81, 0.56, 0.46, 0.11, 0, 0.97, 0.96,
				0.88, 0.84, 0.55, 0.43, 0, 1.00, 0.99, 0.98, 0.93, 0.90, 0.55,
				-1, 1, 1, 1, 1, 1, -1, -1 };
		DecimalFormat df = new DecimalFormat("#.##");

		// Testing library function
		try {
			PredictionCalculator calc = new PredictionCalculator(match);
			Class[] args = new Class[5];
			args[0] = Integer.TYPE;
			args[1] = Integer.TYPE;
			args[2] = Double.TYPE;
			args[3] = Double.TYPE;
			args[4] = Integer.TYPE;
			Method method = PredictionCalculator.class.getDeclaredMethod(
					"calculateAdvSetPercent", args);
			method.setAccessible(true);
			// Player 1 starts the set, therefore at odd scores i+j he is
			// serving
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					Object[] values = { new Integer(i), new Integer(j),
							new Double(0.62), new Double(0.60), new Integer(1) };
					Double res = Double.parseDouble(df.format(method.invoke(
							calc, values)));
					// System.out.println("At score "+i+"-"+j+ " RES ="+res +
					// " EXP="+expMatrix[i*7+j]);
					assertEquals(0, Double.compare(res, expMatrix[i * 7 + j]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void MatchPrediction() {
		// Barnett*
		double[] expMatrix = { 0.63, 0.42, 0.18, 0, 0.78, 0.60, 0.32, 0, 0.92,
				0.81, 0.57, 0, 1, 1, 1, -1 };
		DecimalFormat df = new DecimalFormat("#.##");

		// Testing library function
		try {
			PredictionCalculator calc = new PredictionCalculator(match);
			Class[] args = new Class[6];
			args[0] = Integer.TYPE;
			args[1] = Integer.TYPE;
			args[2] = Double.TYPE;
			args[3] = Double.TYPE;
			args[4] = Integer.TYPE;
			args[5] = Integer.TYPE;
			Method method = PredictionCalculator.class.getDeclaredMethod(
					"calculateMatchPercent", args);
			method.setAccessible(true);
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					Object[] values = { new Integer(i), new Integer(j),
							new Double(0.62), new Double(0.60), 5, 1 };
					Double res = Double.parseDouble(df.format(method.invoke(
							calc, values)));
					// System.out.println("At score "+i+"-"+j+ " RES ="+res +
					// " EXP="+expMatrix[i*4+j]);
					assertEquals(0, Double.compare(res, expMatrix[i * 4 + j]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void allPrediction() {
		// Test cases from GUI in Wozniak - player 2 serving, 00-00(points),
		// 4-4(games), 1-0(sets),
		double pw1 = 0.6527;
		double pw2 = 0.6452;
		int p1 = 0;
		int p2 = 0;
		DecimalFormat df = new DecimalFormat("#.###");
		PredictionCalculator calc = new PredictionCalculator(match);

		// Current game%
		double winCurGamePlayer2 = Double.parseDouble(df.format(calc
				.calculateGamePercent(p1, p2, pw2, 1)));
		double winCurGamePlayer1 = Double.parseDouble(df
				.format(1 - winCurGamePlayer2));
		assertEquals(0, Double.compare(winCurGamePlayer1, 0.178));
		assertEquals(0, Double.compare(winCurGamePlayer2, 0.822));

		// Current set%
		calc.setPWG(1, calc.calculateGamePercent(p1, p2, pw1, 1));
		calc.setPWG(0, calc.calculateGamePercent(p2, p1, pw2, 1));
		// second player serves
		double winCurSetPlayer2 = Double.parseDouble(df.format(calc
				.calculateCurrentSetPercent(4, 4, pw1, pw2, 0)));
		double winCurSetPlayer1 = Double.parseDouble(df
				.format(1 - winCurSetPlayer2));
		// System.out.println(winCurSetPlayer2);
		assertEquals(0, Double.compare(winCurSetPlayer2, 0.483));
		assertEquals(0, Double.compare(winCurSetPlayer1, 0.517));

		// Current match%
		// A 5 set match with final set tiebreak
		double winCurMatchPlayer2 = Double.parseDouble(df.format(calc
				.calculateWinMatch(winCurSetPlayer2, 1, 0, pw1, pw2, 0, 5, 1)));
		double winCurMatchPlayer1 = Double.parseDouble(df
				.format(1 - winCurMatchPlayer2));
		// System.out.println(winCurMatchPlayer2);
		assertEquals(0, Double.compare(winCurMatchPlayer1, 0.721));
		assertEquals(0, Double.compare(winCurMatchPlayer2, 0.279));
	}

	@Test
	public void PredictionInterface() {
		// Testing interfacing result in calculate()
		// Test case: Wozniak* page 23
		double[] expected = {// p1 p2
		0.653, 0.645, // pwos
				0.178, 0.822, // game %
				0.517, 0.483, // set %
				0.721, 0.279, // match %
				1.387, 3.582 // odds
		};
		Score score = new Score(5);
		score.setServer(PlayerEnum.PLAYER2);
		score.setPlayerOnePoints(0);
		score.setPlayerTwoPoints(0);
		int[] playerOneSet = { 6, 4 };
		int[] playerTwoSet = { 4, 4 };
		score.setSetsVariable(playerOneSet, playerTwoSet);
		match.setScore(score);

		Statistics stats1 = new Statistics(1, 0.6527, 0);
		Statistics stats2 = new Statistics(1, 0.6452, 0);
		Player player1 = new Player();
		Player player2 = new Player();
		player1.setStatistics(stats1);
		player2.setStatistics(stats2);
		match.setPlayer1(player1);
		match.setPlayer2(player2);

		double[] result = new PredictionCalculator(match).calculate(match);

		/*
		 * for (double res:result){ System.out.println(res); }
		 */

		assertEquals(0, Double.compare(result[0], expected[0]));
		assertEquals(0, Double.compare(result[1], expected[1]));
		assertEquals(0, Double.compare(result[2], expected[2]));
		assertEquals(0, Double.compare(result[3], expected[3]));
		assertEquals(0, Double.compare(result[4], expected[4]));
		assertEquals(0, Double.compare(result[5], expected[5]));
		assertEquals(0, Double.compare(result[6], expected[6]));
		assertEquals(0, Double.compare(result[7], expected[7]));
		assertEquals(0, Double.compare(result[8], expected[8]));
		assertEquals(0, Double.compare(result[9], expected[9]));
	}
}