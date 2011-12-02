package org.ic.tennistrader.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.exceptions.EndOfFracsoftFileException;

import org.ic.tennistrader.service.threads.MatchUpdaterThread;
import org.ic.tennistrader.utils.Pair;
import static org.ic.tennistrader.utils.Pair.pair;

/**
 * Reads data in Fracsoft format from a given file
 * 
 * @author Paul Grigoras
 * 
 */
public class FracsoftReader extends MatchUpdaterThread {

	private static Logger log = Logger.getLogger(FracsoftReader.class);

	private List<Pair<MOddsMarketData, Score>> matchDataList = new ArrayList<Pair<MOddsMarketData, Score>>();

	private Iterator<Pair<MOddsMarketData, Score>> pointer = null;

	private int inPlayPointer = -1;

	private int updatesPerSecond = 1;

	private static final int DELAY_OFFSET = 1;
	private static final int STATUS_OFFSET = 2;
	private static final int NAME_OFFSET = 4;
	private static final int BACK_OFFSET = 5;
	private static final int LAY_OFFSET = 11;

	private static int AMOUNT_OFFSET = 17;
	private static int LPM_OFFSET = 18;
	private static int GAMES_OFFSET = 19;
	private static int POINTS_OFFSET = 22;

	public FracsoftReader(Match match, String filename)
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

			skipHeader(scanner);

			while (scanner.hasNextLine()) {
				line1 = scanner.nextLine();
				line2 = scanner.nextLine();
				String[] lines1 = line1.split(",");
				String[] lines2 = line2.split(",");

				trim(lines1, lines2);

				MOddsMarketData data = new MOddsMarketData();
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
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (NumberFormatException e){
			e.printStackTrace();
			System.out.println(i);
		}

		finally {
			if (scanner != null)
				scanner.close();
		}

		// pointer = matchDataList.iterator();
		pointer = matchDataList.listIterator(inPlayPointer);
	}

	private static void skipHeader(Scanner scanner) {
		for (int i = 0; i < 3; i++) {
			scanner.nextLine();
		}
	}

	private void trim(String[] lines1, String[] lines2) {
		for (int kk = 0; kk < lines1.length; kk++){
			lines1[kk] = lines1[kk].trim();
			lines2[kk] = lines2[kk].trim();
		}
	}

	private RecordedDataFormat detectFormat(String filename) {

		Scanner scanner = null;

		try {
			scanner = new Scanner(new FileInputStream(filename));
			skipHeader(scanner);
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

	private ArrayList<Pair<Double, Double>> getOdds(String[] lines1, int offset) {
		ArrayList<Pair<Double, Double>> pl1Backs = new ArrayList<Pair<Double, Double>>();
		for (int i = 0; i < 3; i++) {
			double odds = Double.parseDouble(lines1[offset + i * 2]);
			double amount = Double.parseDouble(lines1[offset + i * 2 + 1]);
			Pair<Double, Double> p = pair(odds, amount);
			pl1Backs.add(p);
		}
		return pl1Backs;
	}

	@Override
	public void runBody() {
		try {
			LiveDataFetcher.handleFileEvent(this.match, getMarketData());
		} catch (EndOfFracsoftFileException e1) {
			LiveDataFetcher.handleEndOfFile(this.match);
			this.setStop();
		}
		try {
			Thread.sleep(1000 / this.updatesPerSecond);
		} catch (InterruptedException e) {
			log.info("Fracsoft thread interrupted");
		}
	}

	public Pair<MOddsMarketData, Score> getMarketData()
			throws EndOfFracsoftFileException {
		if (pointer.hasNext())
			return pointer.next();
		throw new EndOfFracsoftFileException();
	}

	@Override
	public void setMatch(RealMatch match) {
		this.match = match;
	}

	public static Pair<String, String> getPlayerNames(String filename) {
		String player1 = "";
		String player2 = "";

		Scanner scanner;

		try {
			scanner = new Scanner(new FileInputStream(filename));

			skipHeader(scanner);

			String line1 = scanner.nextLine();
			String line2 = scanner.nextLine();

			player1 = line1.split(",")[NAME_OFFSET];
			player2 = line2.split(",")[NAME_OFFSET];

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}

		return pair(player1, player2);
	}

	public void setUpdatesPerSecond(int updates) {
		this.updatesPerSecond = updates;
	}
}