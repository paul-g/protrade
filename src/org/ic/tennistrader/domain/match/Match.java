package org.ic.tennistrader.domain.match;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;

import static org.ic.tennistrader.domain.match.PlayerEnum.casePlayer;

public abstract class Match {    
    protected Player player1 = new Player();
    protected Player player2 = new Player();
    protected Score score = new Score();    
    protected List<MOddsMarketData> marketDatas;    
    protected String filename = null;
    protected String setBettingFilename = null;
    private int currentSet = -1;
   
	public abstract boolean isInPlay();
    
    public abstract String getName();
    
    public abstract PlayerEnum getWinner() throws MatchNotFinishedException;
    
    public abstract void addMarketData(MOddsMarketData data);

    public abstract boolean isFromFile();
    
    // match based statistics should be moved to a separate object
    private String [] playerOneWonLost;
    private String [] playerTwoWonLost;
    
    private Map<String, String [][]> statisticsMap = new HashMap<String, String[][]>();
    
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

    public Player getPlayer(PlayerEnum player){
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
    
    public String getFilename(){
        return this.filename;
    }
    
    public String getScoreAsString(PlayerEnum player) {
        final int [] scores = score.getPlayerScores(player);
        
        StringBuffer scoresString = new StringBuffer();
        
        for (int i=0;i<scores.length;i++) {
            scoresString.append(scores[i] + ",");
        }
        
        scoresString.append( score.getPlayerPoints(player) );
        
        return scoresString.toString();
    }
    
    public void setPlayer1(Player player){
        this.player1 = player;
    }
    
    public void setPlayer2(Player player){
        this.player2 = player;
    }
    
    public PlayerEnum getServer(){
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
}
