package org.ic.tennistrader.scrappers.livexscores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class ScoreFetcher {

	private static final Logger log = Logger.getLogger(ScoreFetcher.class);

	private static final String FINISHED_URL = "http://www.livexscores.com/xml/tfinished.txt";
	private static final String ALL_URL = "http://www.livexscores.com/xml/tall.txt";
	private static final String YESTERDAY_URL = "http://www.livexscores.com/xml/tyesterday.txt";

	public static void main(String args[]) {
		try {
			log.info("Starting to fetch scores");
			String scores = fetchScores(LivexMatchType.YESTERDAY);
			log.info("Scores fetched: " + scores);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String fetchScores(LivexMatchType matchType)
			throws IOException {

		String address;
		switch (matchType) {
		case YESTERDAY:
			address = YESTERDAY_URL;
			break;
		case FINISHED:
			address = FINISHED_URL;
			break;
		default:
			address = ALL_URL;
			break;
		}

		URL url = new URL(address);
		URLConnection conn = url.openConnection();

		StringBuilder scoresBuilder = new StringBuilder();
		InputStream is = conn.getInputStream();
		Scanner sc = new Scanner(is);
		while (sc.hasNext()) {
			scoresBuilder.append(sc.next());
		}

		return scoresBuilder.toString();
	}
}
