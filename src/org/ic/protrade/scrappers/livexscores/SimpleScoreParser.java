package org.ic.protrade.scrappers.livexscores;

import org.apache.log4j.Logger;

public class SimpleScoreParser {

	private static final Logger log = Logger.getLogger(SimpleScoreParser.class);
	private static final int SKIP_TO_SCORE = 5;

	public static LivexMatch searchForMatch(String player1Name,
			String player2Name, String scoresString)
			throws NoSuchMatchException {

		String tournament = null;

		String[] all = splitAll(scoresString);

		log.debug("Split scores. Searching for match...");
		for (String string : all) {
			log.debug(string);
			if (searchPlayerName(player1Name, string)
					&& searchPlayerName(player2Name, string)) {
				tournament = string;
			}
		}

		if (tournament == null)
			throw new NoSuchMatchException();

		log.debug("Match search finised. Match found!");

		String[] data = tournament.split("\\*");

		String tournamentName = data[1];

		log.debug("Tournament: " + tournamentName);

		String player1 = data[2];
		log.debug("Player 1: " + player1);

		String player2 = data[3];
		log.debug("Player 2:" + player2);

		StringBuilder player1Score = new StringBuilder();
		StringBuilder player2Score = new StringBuilder();

		for (int i = 4 + SKIP_TO_SCORE; i < 4 + SKIP_TO_SCORE + 10; i += 2) {
			player1Score.append(data[i]);
			player2Score.append(data[i + 1]);
		}

		log.debug("Player1 score : " + player1Score.toString());
		log.debug("Player2 score : " + player2Score.toString());

		return new LivexMatch(player1, player2, player1Score.toString(),
				player2Score.toString());
	}

	private static boolean searchPlayerName(String player1Name, String string) {
		return string.indexOf(player1Name) > -1;
	}

	public static String[] splitAll(String scoresString) {
		return scoresString.split("@");
	}
}
