package org.ic.tennistrader.score;

import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;

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
				PredictionCalculator.calculate(match)[0], expected[0]));
		assertEquals(0, Double.compare(
				PredictionCalculator.calculate(match)[1], expected[1]));
	}

	@Test
	public void GamePrediction() {
		double[] expected = { 0.943, 0.057 };
		// Table of expected values from *Wozniak
		double[] expMatrix = { 0.781, 0.629, 0.417, 0.177, 0, 0.873, 0.757,
				0.562, 0.284, 0, 0.944, 0.875, 0.731, 0.455, 0, 0.986, 0.962,
				0.899, 0.731, -1, 1, 1, 1, -1, -1, };
		DecimalFormat df = new DecimalFormat("#.###");

		// Testing library function
		try {
			PredictionCalculator calc = new PredictionCalculator();
			Class[] args = new Class[4];
			args[0] = Integer.TYPE;
			args[1] = Integer.TYPE;
			args[2] = Double.TYPE;
			args[3] = Integer.TYPE;
			Method method = PredictionCalculator.class.getDeclaredMethod(
					"calculateGamePercent", args);
			method.setAccessible(true);
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					Object[] values = { new Integer(i), new Integer(j),
							new Double(0.6227), new Integer(1) };
					Double res = Double.parseDouble(df.format((Double) method
							.invoke(calc, (Object[]) values)));
					System.out.println("RES" + res + "EXP"
							+ expMatrix[i * 5 + j]);

					assertEquals(0, Double.compare(res, expMatrix[i * 5 + j]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Testing interfacing function
		Score score = new Score();
		score.setPlayerOnePoints(40);
		score.setPlayerTwoPoints(30);
		score.setServer(PlayerEnum.PLAYER1);
		match.setScore(score);

		double[] result = PredictionCalculator.calculate(match);
		for (double res : result) {
			System.out.println(res);
		}
		assertEquals(0, Double.compare(result[2], expected[0]));
		assertEquals(0, Double.compare(result[3], expected[1]));
	}

	@Ignore
	@Test
	public void SetPrediction() {
		double[] expected = { 0.846, 0.154 };
		Score score = new Score(3);
		score.setServer(PlayerEnum.PLAYER1);
		score.setPlayerOnePoints(40);
		score.setPlayerTwoPoints(15);
		int[] playerOneSet = { 4, 7, 3 };
		int[] playerTwoSet = { 6, 5, 2 };
		score.setSets(playerOneSet, playerTwoSet);
		match.setScore(score);

		double[] result = PredictionCalculator.calculate(match);
		assertEquals(0, Double.compare(result[4], expected[0]));
		assertEquals(0, Double.compare(result[5], expected[1]));
	}

	@Ignore
	@Test
	public void MatchPrediction() {
		double[] expected = { 0.989, 0.011 };
		Score score = new Score(3);
		score.setServer(PlayerEnum.PLAYER1);
		score.setPlayerOnePoints(40);
		score.setPlayerTwoPoints(0);
		int[] playerOneSet = { 4, 7, 5 };
		int[] playerTwoSet = { 6, 5, 4 };
		score.setSets(playerOneSet, playerTwoSet);
		match.setScore(score);

		double[] result = PredictionCalculator.calculate(match);
		assertEquals(0, Double.compare(result[6], expected[0]));
		assertEquals(0, Double.compare(result[7], expected[1]));
	}
}