package org.ic.tennistrader.service.threads.file_readers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.markets.MarketPrices;
import org.ic.tennistrader.domain.markets.MatchScore;
import org.ic.tennistrader.domain.markets.SetBettingMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.exceptions.EndOfFracsoftFileException;

public class FracsoftSetBettingReader extends FracsoftReader<SetBettingMarketData> {
	private static Logger log = Logger.getLogger(FracsoftSetBettingReader.class);
	
	private static final int HEADER_NO_LINES = 4;
	private static final int TIMESTAMP_OFFSET = 0;
	private static final int DELAY_OFFSET = 1;
	//private static final int STATUS_OFFSET = 3;
	private static final int NAME_OFFSET = 5;
	private static final int BACK_OFFSET = 6;
	private static final int LAY_OFFSET = 12;
	//private static final int AMOUNT_OFFSET = 18;
	//private static final int LPM_OFFSET = 19;

	public FracsoftSetBettingReader(Match match, String filename)
			throws FileNotFoundException {
		this.match = match;
		log.info("Creating set betting fracsoft reader from file " + filename);
		System.out.println("Creating set betting fracsoft reader from file " + filename);
		String line1;
		Scanner scanner = null;
		int i = 0;
		try {
			scanner = new Scanner(new FileInputStream(filename));
			skipHeader(scanner, HEADER_NO_LINES);
			SetBettingMarketData data = new SetBettingMarketData();
			String currentTimestamp = null;

			while (scanner.hasNextLine()) {
				line1 = scanner.nextLine();
				String[] lines1 = line1.split(",");

				trim(lines1);
				if (lines1.length > 1) {
					if (currentTimestamp == null) {
						data = new SetBettingMarketData();
						currentTimestamp = lines1[TIMESTAMP_OFFSET];
					} else if (!currentTimestamp
							.equals(lines1[TIMESTAMP_OFFSET])) {
						matchDataList.add(data);
						data = new SetBettingMarketData();
						currentTimestamp = lines1[TIMESTAMP_OFFSET];
						// System.out.println(line1);
						if ((Integer.parseInt(lines1[DELAY_OFFSET]) != 0)
								&& (inPlayPointer == -1))
							inPlayPointer = i;
						i++;
					}

					MatchScore matchScore = MatchScore
							.getMatchScore(lines1[NAME_OFFSET]);
					if (matchScore.getFirstPlayerLastName().equals(
							match.getPlayerTwo().getLastname()))
						matchScore = new MatchScore(matchScore
								.getSecondPlayerScore(), matchScore
								.getFirstPlayerScore());

					MarketPrices marketPrices = new MarketPrices();
					marketPrices.setBackPrices(getOdds(lines1, BACK_OFFSET));
					marketPrices.setLayPrices(getOdds(lines1, LAY_OFFSET));

					data.addMatchScoreMarketPrices(matchScore, marketPrices);
				}
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
	}

	@Override
	protected void runFileReader() {
		try {
			// LiveDataFetcher.handleFileEvent(this.match, getMarketData());
			getMarketData();
			/*
			SetBettingMarketData data = getMarketData();
			System.out.println("New set betting update");
			for (MatchScore score : data.getMatchScoreMarketData().keySet()) {
				System.out.println("for match score " + score.toString()
						+ ", market " + data.getMatchScoreMarketPrices(score));
			}
			*/
		} catch (EndOfFracsoftFileException e1) {
			// LiveDataFetcher.handleEndOfFile(this.match);
			this.setStop();
		}
		try {
			Thread.sleep(1000 / this.updatesPerSecond);
		} catch (InterruptedException e) {
			log.info("Fracsoft set betting thread interrupted");
		}
	}	
}
