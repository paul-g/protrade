package org.ic.protrade.service.threads.file_readers;

import static org.ic.protrade.data.utils.Pair.pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.match.LiveMatch;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.data.match.Score;
import org.ic.protrade.data.utils.Pair;
import org.ic.protrade.exceptions.EndOfFracsoftFileException;
import org.ic.protrade.service.DataManager;

/**
 * Reads data in Fracsoft format from a given file
 * 
 * @author Paul Grigoras
 * 
 */
public class FracsoftMatchOddsReader extends
		FracsoftReader<Pair<MOddsMarketData, Score>> {
	// private FracsoftSetBettingReader setBettingReader = null;

	private static Logger log = Logger.getLogger(FracsoftMatchOddsReader.class);

	private static final int TIME_OFFSET = 0;
	private static final int HEADER_NO_LINES = 3;
	private static final int DELAY_OFFSET = 1;
	private static final int STATUS_OFFSET = 2;
	private static final int NAME_OFFSET = 4;
	private static final int BACK_OFFSET = 5;
	private static final int LAY_OFFSET = 11;

	private int AMOUNT_OFFSET = 17;
	private int LPM_OFFSET = 18;
	private int GAMES_OFFSET = 19;
	private int POINTS_OFFSET = 22;

	public FracsoftMatchOddsReader(Match match, String filename)
			throws FileNotFoundException {
		this.match = match;
		log.info("Creating fracsoft reader from file " + filename);
		String line1, line2;
		Scanner scanner = null;

		RecordedDataFormat format = detectFormat(filename);
		adjustOffsets(format);

		int i = 0;
		try {
			scanner = new Scanner(new FileInputStream(filename));

			skipHeader(scanner, HEADER_NO_LINES);

			while (scanner.hasNextLine()) {
				line1 = scanner.nextLine();
				line2 = scanner.nextLine();
				String[] lines1 = line1.split(",");
				String[] lines2 = line2.split(",");

				trim(lines1);
				trim(lines2);

				MOddsMarketData data = new MOddsMarketData();
				// data.setDate(new Date(Long.parseLong(lines1[TIME_OFFSET])));
				data.setTime(new Date(Long.parseLong(lines1[TIME_OFFSET])));

				data.setDelay(Integer.parseInt(lines1[DELAY_OFFSET]));
				data.setMatchStatus(lines1[STATUS_OFFSET]);
				// player 1 data
				data.setPlayer1(lines1[NAME_OFFSET]);
				data.setPl1Back(getOdds(lines1, BACK_OFFSET));
				data.setPl1Lay(getOdds(lines1, LAY_OFFSET));

				data.setPl1LastMatchedPrice(Double
						.parseDouble(lines1[LPM_OFFSET]));
				data.setPlayer1TotalAmountMatched((Double
						.parseDouble(lines1[AMOUNT_OFFSET])));

				// player 2 data
				data.setPlayer2(lines2[NAME_OFFSET]);
				data.setPl2Back(getOdds(lines2, BACK_OFFSET));
				data.setPl2Lay(getOdds(lines2, LAY_OFFSET));
				data.setPl2LastMatchedPrice(Double
						.parseDouble(lines2[LPM_OFFSET]));
				data.setPlayer2TotalAmountMatched((Double
						.parseDouble(lines2[AMOUNT_OFFSET])));

				Score s = new Score();

				if (format != RecordedDataFormat.FRACSOFT_SIMPLE) {
					// score data is provided
					int pl1Points = Integer.parseInt(lines1[POINTS_OFFSET]);
					int pl2Points = Integer.parseInt(lines2[POINTS_OFFSET]);

					s.setPlayerOnePoints(pl1Points);
					s.setPlayerTwoPoints(pl2Points);

					int pl1games[] = getGames(lines1);
					int pl2games[] = getGames(lines2);

					s.setSets(pl1games, pl2games);
				}

				matchDataList.add(pair(data, s));
				if (data.getDelay() != 0 && inPlayPointer == -1)
					inPlayPointer = i;
				i++;

			}

			if (inPlayPointer != -1)
				pointer = matchDataList.listIterator(inPlayPointer);
			else
				pointer = matchDataList.iterator();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println(i);
		}

		finally {
			if (scanner != null)
				scanner.close();
		}
		/*
		 * if (match.getSetBettingFilename() != null) { setBettingReader = new
		 * FracsoftSetBettingReader(match, match.getSetBettingFilename()); }
		 */
	}

	private RecordedDataFormat detectFormat(String filename) {

		Scanner scanner = null;

		try {
			scanner = new Scanner(new FileInputStream(filename));
			skipHeader(scanner, HEADER_NO_LINES);
			String[] elements = scanner.nextLine().split(",");
			if (elements.length == RecordedDataFormat.FRACSOFT_SIMPLE
					.getNElements())
				return RecordedDataFormat.FRACSOFT_SIMPLE;
			else if (elements.length == RecordedDataFormat.FRACSOFT_FULL
					.getNElements())
				return RecordedDataFormat.FRACSOFT_FULL;

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		finally {
			if (scanner != null)
				scanner.close();
		}

		return RecordedDataFormat.RECORDED_NO_TOTAL_MATCHED;
	}

	private void adjustOffsets(RecordedDataFormat format) {
		int offset = 0;

		switch (format) {
		case FRACSOFT_FULL:
		case FRACSOFT_SIMPLE:
			break;
		case RECORDED_NO_TOTAL_MATCHED:
			offset = 1;
			break;
		}

		AMOUNT_OFFSET -= offset;
		LPM_OFFSET -= offset;
		GAMES_OFFSET -= offset;
		POINTS_OFFSET -= offset;
	}

	private int[] getGames(String[] lines) {
		int[] games = new int[3];
		for (int i = 0; i < 3; i++) {
			games[i] = Integer.parseInt(lines[GAMES_OFFSET + i]);
		}
		return games;
	}

	@Override
	protected void runFileReader() {
		try {
			DataManager.handleFileEvent(this.match, getMarketData());
			// setBettingReader.runFileReader();
		} catch (EndOfFracsoftFileException e1) {
			DataManager.handleEndOfFile(this.match);
			this.setStop();
		}
		try {
			Thread.sleep(1000 / this.updatesPerSecond);
		} catch (InterruptedException e) {
			log.info("Fracsoft thread interrupted");
		}
	}

	@Override
	public void setMatch(LiveMatch match) {
		this.match = match;
	}

	public static Pair<String, String> getPlayerNames(String filename) {
		String player1 = "";
		String player2 = "";

		Scanner scanner;

		try {
			scanner = new Scanner(new FileInputStream(filename));

			skipHeader(scanner, HEADER_NO_LINES);

			String line1 = scanner.nextLine();
			String line2 = scanner.nextLine();

			player1 = line1.split(",")[NAME_OFFSET];
			player2 = line2.split(",")[NAME_OFFSET];

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return pair(player1, player2);
	}
}