package org.ic.protrade.scrappers.tennisinsight;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.data.match.Player;
import org.ic.protrade.data.match.Statistics;
import org.ic.protrade.scrappers.SiteParser;

public class StatisticsParser extends SiteParser {

	private static final int N_STATS_LINE = 4;

	private String stats;
	private final Match match;

	public StatisticsParser(String stats, Match match) {
		this.stats = stats;
		this.match = match;
	}

	public void parseAndSetStatistics() {
		Player playerOne = match.getPlayerOne();
		Player playerTwo = match.getPlayerTwo();

		String[] playerOneWonLost = new String[6];
		String[] playerTwoWonLost = new String[6];

		stats = stats.substring(stats.indexOf("Head to Head Match Preview"),
				stats.indexOf("Player Comparison"));
		stats = stats.substring(stats.indexOf("stats\n") + 6, stats.length());

		// Fill get player images
		stats = getImages(stats);
		stats = skipLines(stats, 2);

		stats = skipEmptyLines(stats);
		// Fill in stats (dob, country, age etc.)
		setPlayerInfo(playerOne);

		stats = skipEmptyLines(stats);
		stats = skipLines(stats, 6);
		stats = skipEmptyLines(stats);
		stats = skipLines(stats, 1);
		setPlayerInfo(playerTwo);

		stats = skipEmptyLines(stats);
		stats = stats.substring(stats.indexOf("Match Statistics\t") + 17,
				stats.length());
		stats = skipEmptyLines(stats);

		int statistic = 0;
		// Matches, sets, games
		for (int i = 0; i < 3; i++) {
			getSkipUntil(playerOneWonLost, playerTwoWonLost, statistic++, ")",
					")", "W/L", 3);
		}

		// Points
		getSkipUntil(playerOneWonLost, playerTwoWonLost, statistic++, "%", "%",
				"W/L", 3);

		// Tiebreaks
		getSkipUntil(playerOneWonLost, playerTwoWonLost, statistic++, ")", ")",
				"W/L", 3);

		getSkipUntil(playerOneWonLost, playerTwoWonLost, statistic++, "\t",
				"\n", "Set", 4);

		// Service Statistics
		Map<String, String[][]> statisticsMap = new HashMap<String, String[][]>();
		int[] sizes = { 6, 4, 6, 4 };

		Statistics playerOneStats = new Statistics();
		Statistics playerTwoStats = new Statistics();
		for (int i = 0; i < N_STATS_LINE; i++) {

			String statsName = getAndSkip();
			String[][] values = new String[sizes[i]][3];

			for (int j = 0; j < sizes[i]; j++) {
				values[j][0] = stats.substring(0, stats.indexOf("\t"));
				stats = stats
						.substring(stats.indexOf("\t") + 1, stats.length());
				values[j][1] = stats.substring(0, stats.indexOf("\t"));
				stats = stats
						.substring(stats.indexOf("\t") + 1, stats.length());
				values[j][2] = stats.substring(0, stats.indexOf("\n"));
				stats = stats
						.substring(stats.indexOf("\n") + 1, stats.length());

				if (i == 0 && j == 2) {
					String s = values[j][0].substring(0,
							values[j][0].length() - 1);
					double d = Double.parseDouble(s);
					playerOneStats.setFirstServePercent(d / 100);

					playerTwoStats.setFirstServePercent(Double
							.parseDouble(values[j][2].substring(0,
									values[j][2].length() - 1)) / 100);
				}

				if (i == 0 && j == 3) {
					playerOneStats.setFirstServeWins(Double
							.parseDouble(values[j][0].substring(0,
									values[j][0].length() - 1)) / 100);

					playerTwoStats.setFirstServeWins(Double
							.parseDouble(values[j][2].substring(0,
									values[j][2].length() - 1)) / 100);
				}

				if (i == 0 && j == 4) {
					playerOneStats.setSecondServeWins(Double
							.parseDouble(values[j][0].substring(0,
									values[j][0].length() - 1)) / 100);

					playerTwoStats.setSecondServeWins(Double
							.parseDouble(values[j][2].substring(0,
									values[j][2].length() - 1)) / 100);

				}
			}

			statisticsMap.put(statsName, values);
		}

		match.setStatisticsMap(statisticsMap);
		match.getPlayerOne().setStatistics(playerOneStats);
		match.getPlayerTwo().setStatistics(playerTwoStats);
		match.setPlayerOneWonLost(playerOneWonLost);
		match.setPlayerTwoWonLost(playerTwoWonLost);
	}

	private void getSkipUntil(String[] playerOneWonLost,
			String[] playerTwoWonLost, int i, String getUntilToken,
			String skipBeforeToken, String internalToken, int inc) {
		playerOneWonLost[i] = stats.substring(0,
				stats.indexOf(getUntilToken) + 1);
		stats = stats.substring(stats.indexOf(internalToken) + inc,
				stats.length());
		stats = skipEmptyLines(stats);
		playerTwoWonLost[i] = stats.substring(0,
				stats.indexOf(skipBeforeToken) + 1);
		stats = skipLines(stats, 1);
	}

	private void setPlayerInfo(Player playerOne) {
		playerOne.setCountry(getAndSkip());
		playerOne.setDob(getAndSkip());
		String heightAndPlays = getAndSkip();
		String[] values = heightAndPlays.split("/");
		playerOne.setHeight(values[0].trim());
		playerOne.setPlays(values[1].trim());
		playerOne.setWonLost(getAndSkip());
		playerOne.setTitles(getAndSkip());
		playerOne.setRank(getAndSkip());
	}

	private String getAndSkip() {
		String element = stats.substring(0, stats.indexOf('\n'));
		this.stats = skipLines(stats, 1);
		return element;
	}

	private String getImages(String stats) {
		String player1 = stats.substring(0, stats.indexOf('\n'));
		// table.getColumn(0).setText(player1);
		int index = 0;
		String imagePlayer = "";
		String cPlayer = player1;
		while (cPlayer.indexOf(' ') > -1) {
			imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
					+ "%20";
			cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1,
					cPlayer.length());
		}
		imagePlayer += cPlayer;

		Image imgPlayer = getImage("http://www.tennisinsight.com/images/"
				+ imagePlayer + ".jpg");
		try {
			// table.getColumn(0).setImage(imgPlayer);
			imgPlayer.dispose();
		} catch (NullPointerException ex) {
			// table
			// .getColumn(0)
			// .setImage(
			// getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
		}
		stats = skipLines(stats, 2);

		String player2 = stats.substring(0, stats.indexOf('\n'));
		// table.getColumn(2).setText(player2);
		imagePlayer = "";
		cPlayer = player2;
		while (cPlayer.indexOf(' ') > -1) {
			imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
					+ "%20";
			cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1,
					cPlayer.length());
		}
		imagePlayer += cPlayer;

		imgPlayer = getImage("http://www.tennisinsight.com/images/"
				+ imagePlayer + ".jpg");
		try {
			// table.getColumn(2).setImage(imgPlayer);
			imgPlayer.dispose();
		} catch (NullPointerException ex) {
			// table
			// .getColumn(2)
			// .setImage(
			// getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
		}
		return stats;
	}

	public static Image getImage(String url) {
		Image img = null;
		/*
		 * try { URL web = new URL(url); InputStream stream = web.openStream();
		 * ImageLoader loader = new ImageLoader(); ImageData imgData =
		 * loader.load(stream)[0]; img = new Image(Display.getDefault(),
		 * imgData); } catch (Exception e) { // System.err.println("No image " +
		 * url + ", " + e); return null; }
		 */
		return img;
	}
}
