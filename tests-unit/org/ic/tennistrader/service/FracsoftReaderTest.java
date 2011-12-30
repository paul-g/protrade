package org.ic.tennistrader.service;

import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.service.threads.file_readers.FracsoftMatchOddsReader;
import org.ic.tennistrader.service.threads.file_readers.FracsoftSetBettingReader;
import org.junit.Test;

import static org.junit.Assert.*;

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
