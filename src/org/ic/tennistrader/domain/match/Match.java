package org.ic.tennistrader.domain.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;

public abstract class Match {    
    
    protected Player player1 = new Player();
    protected Player player2 = new Player();
    protected Score score = new Score();    
    protected List<MOddsMarketData> marketDatas;    
    protected String filename = null;
    
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
        switch (player){
            case PLAYER1: return player1;
            case PLAYER2: return player2;
        }
        return null;
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
        int [] scores = score.getPlayerScores(player);
        
        String scoresString = "";
        for (int i=0;i<scores.length;i++)
            scoresString += scores[i] + ",";
        scoresString += score.getPlayerPoints(player);
        return scoresString;
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
}
