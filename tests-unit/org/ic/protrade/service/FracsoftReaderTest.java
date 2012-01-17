package org.ic.protrade.service;

import static org.junit.Assert.assertEquals;

import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.data.match.Player;
import org.ic.protrade.service.threads.file_readers.FracsoftMatchOddsReader;
import org.ic.protrade.service.threads.file_readers.FracsoftSetBettingReader;
import org.junit.Test;

public class FracsoftReaderTest {

	@Test
	public void fullDataFormat() {

		String filename = "data/test/fracsoft-reader/tso-fed.csv";
		String setFilename = "data/test/fracsoft-reader/tso-fed_set.csv";

		Match match = new HistoricalMatch(filename, setFilename);

		Player player1 = match.getPlayerOne();
		Player player2 = match.getPlayerTwo();
		assertEquals("Jo Wilfried", player1.getFirstname());
		assertEquals("Tsonga", player1.getLastname());
		assertEquals("Roger", player2.getFirstname());
		assertEquals("Federer", player2.getLastname());

		try {
			new FracsoftMatchOddsReader(match, filename);
			new FracsoftSetBettingReader(match, setFilename);
		} catch (Exception e) {

		}

	}

	@Test
	public void fracsoftFormat() {

	}

}
