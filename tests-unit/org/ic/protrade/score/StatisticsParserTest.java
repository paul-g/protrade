package org.ic.protrade.score;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.data.match.Player;
import org.ic.protrade.scrappers.tennisinsight.StatisticsParser;
import org.junit.Test;

public class StatisticsParserTest {

	// private static final String TEST_STRING = "";

	@Test
	public void parseAndSetStatistics() {

		String testString = getTestString("data/test/tennisinsight-tso-fed.dat");

		Player player1 = new Player();
		Player player2 = new Player();
		Match match = new HistoricalMatch(player1, player2);
		new StatisticsParser(testString, match).parseAndSetStatistics();
		assertEquals(player1.getCountry().trim(), "France");
		assertEquals(player2.getCountry().trim(), "Switzerland");
	}

	private static String getTestString(String filename) {

		Scanner scanner;
		String test = "";

		try {
			scanner = new Scanner(new FileInputStream(filename));

			while (scanner.hasNext()) {
				test += scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			// log.error(e.getMessage());
		}

		return test;
	}
}
