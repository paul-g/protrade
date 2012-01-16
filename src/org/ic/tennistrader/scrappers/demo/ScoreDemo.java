package org.ic.tennistrader.scrappers.demo;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.ic.tennistrader.scrappers.livexscores.LivexMatch;
import org.ic.tennistrader.scrappers.livexscores.LivexMatchType;
import org.ic.tennistrader.scrappers.livexscores.NoSuchMatchException;
import org.ic.tennistrader.scrappers.livexscores.ScoreFetcher;
import org.ic.tennistrader.scrappers.livexscores.SimpleScoreParser;

public class ScoreDemo {

	private static final int REPETITIONS = 1;
	private static final Logger log = Logger.getLogger(ScoreDemo.class);
	private static String player1 = "RufinGuillaume";
	private static String player2 = "PhauBjorn";

	public static void main(String args[]) {

		String scores = null;

		if (args.length == 2) {
			player1 = args[0];
			player2 = args[1];
		}
		log.info("Starting score fetch");
		for (int i = 0; i < REPETITIONS; i++)
			fetchScores(scores);
		log.info("Score fetched finished");

	}

	private static void fetchScores(String scores) {
		try {
			log.debug("Starting to fetch scores");
			scores = ScoreFetcher.fetchScores(LivexMatchType.YESTERDAY);
			log.debug("Scores fetched: " + scores);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (scores != null) {
			try {
				/*
				 * LivexMatch match = SimpleScoreParser.searchForMatch(
				 * "NieminenJarkko", "BenneteauJulien", scores);
				 */
				LivexMatch match = SimpleScoreParser.searchForMatch(player1,
						player2, scores);
				log.info("Parsed score: " + match.toString());
			} catch (NoSuchMatchException e) {
				e.printStackTrace();
			}
		}
	}
}
