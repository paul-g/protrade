package org.ic.protrade.domain.match;

import static org.ic.protrade.domain.match.PlayerEnum.casePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.markets.MatchScore;
import org.ic.protrade.exceptions.MatchNotFinishedException;

public abstract class Match {
	protected Player player1 = new Player();
	protected Player player2 = new Player();
	protected Score score = new Score();
	protected List<MOddsMarketData> marketDatas;
	protected String filename = null;
	protected String setBettingFilename = null;
	private int currentSet = -1;
	private List<MatchScore> impossibleScores = new ArrayList<MatchScore>();

	public abstract boolean isInPlay();

	public abstract String getName();

	public abstract PlayerEnum getWinner() throws MatchNotFinishedException;

	public abstract void addMarketData(MOddsMarketData data);

	public abstract boolean isFromFile();

	// match based statistics should be moved to a separate object
	private String[] playerOneWonLost;
	private String[] playerTwoWonLost;

	private Map<String, String[][]> statisticsMap = new HashMap<String, String[][]>();

	public Map<String, String[][]> getStatisticsMap() {
		return statisticsMap;
	}

	public void setStatisticsMap(Map<String, String[][]> statistics) {
		this.statisticsMap = statistics;
	}

	public String[] getPlayerOneWonLost() {
		return playerOneWonLost;
	}

	public void setPlayerOneWonLost(String[] playerOneWonLost) {
		this.playerOneWonLost = playerOneWonLost;
	}

	public String[] getPlayerTwoWonLost() {
		return playerTwoWonLost;
	}

	public void setPlayerTwoWonLost(String[] playerTwoWonLost) {
		this.playerTwoWonLost = playerTwoWonLost;
	}

	public Player getPlayer(PlayerEnum player) {
		return casePlayer(player, player1, player2);
	}

	public Player getPlayerOne() {
		return player1;
	}

	public Player getPlayerTwo() {
		return player2;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public MOddsMarketData getLastMarketData() {
		if (this.marketDatas.size() == 0)
			return null;
		return this.marketDatas.get(this.marketDatas.size() - 1);
	}

	public void setMarketDatas(List<MOddsMarketData> marketDatas) {
		this.marketDatas = marketDatas;
	}

	public List<MOddsMarketData> getMarketDatas() {
		return marketDatas;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getScoreAsString(PlayerEnum player) {
		return score.toString(player);
	}

	public void setPlayer1(Player player) {
		this.player1 = player;
	}

	public void setPlayer2(Player player) {
		this.player2 = player;
	}

	public PlayerEnum getServer() {
		return score.getServer();
	}

	public String getSetBettingFilename() {
		return setBettingFilename;
	}

	public void setSetBettingFilename(String setBettingFilename) {
		this.setBettingFilename = setBettingFilename;
	}

	public void setCurrentSet(int currentSet) {
		this.currentSet = currentSet;
	}

	public int getCurrentSet() {
		return currentSet;
	}

	public void setImpossibleScores(List<MatchScore> impossibleScores) {
		this.impossibleScores = impossibleScores;
	}

	public List<MatchScore> getImpossibleScores() {
		return impossibleScores;
	}

	public String getStatus() {
		return isInPlay() ? "In Progress" : "Not In Progress";
	}

}
