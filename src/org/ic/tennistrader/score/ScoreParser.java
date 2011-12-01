package org.ic.tennistrader.score;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;

public class ScoreParser extends SiteParser {
	
    private String scoreString;
    private Match match;
    
    public ScoreParser(String scoreString, Match match) {
		super();
		this.scoreString = scoreString;
		this.match = match;
	}

	public void parseAndSetScores() {
    	int[] playerOneGames = new int[6];
    	int[] playerTwoGames = new int[6];
    	int playerOnePoints = 0, playerTwoPoints = 0;
    	int matchStart = scoreString.indexOf("Tsonga Jo-Wilfried");
    	
    	//int matchStart = scoreString.indexOf(match.getPlayerOne().getLastname() + " " + match.getPlayerOne().getFirstname());
        
    	PlayerEnum server = PlayerEnum.PLAYER1;
        // display server
        if (matchStart >= 8
                && scoreString.substring(matchStart - 8, matchStart - 2).compareTo(
                        "SERVER") == 0) {
            // player 1 serves
            server = PlayerEnum.PLAYER1;
        }
 
        scoreString = scoreString.substring(matchStart, scoreString.length());
        //String player1 = scoreString.substring(0, scoreString.indexOf(")") + 1);
        scoreString = scoreString.substring(scoreString.indexOf("\n") + 1, scoreString.length());
        scoreString.trim();

        // Match has to be in play or finished
        if (!scoreString.startsWith(match.getPlayerTwo().getLastname() + " " + match.getPlayerTwo().getFirstname())) {
            // //////////////Player 1 data
            // Skip odds
            if (scoreString.charAt(0) != '\t')
                scoreString = skipLines(scoreString,1);
            
            // skip initial tab
            scoreString = scoreString.substring(1, scoreString.length());
            
            // for 5 sets max
            int pos = 0;
            for (int i = 0; i < 5; i++) {
            	playerOneGames[i] = Integer.parseInt(scoreString.substring(0, scoreString.indexOf("\t")));
                scoreString = scoreString.substring(scoreString.indexOf("\t") + 1,
                        scoreString.length());
                
                if (scoreString.startsWith("\n") || 
                	scoreString.startsWith("\t") || 
                	scoreString.startsWith(" "))
                	{ pos = i+1; i = 5; scoreString = scoreString.trim(); }
            }          
          
            // Points
            if (!scoreString.startsWith(match.getPlayerTwo().getLastname())) {
                if (scoreString.substring(0,2).equals("Ad"))
                    playerOnePoints = 50;
                else 
                    playerOnePoints = Integer.parseInt(scoreString.substring(0, 2));
                scoreString = scoreString.substring(2, scoreString.length());
                scoreString = skipEmptyLines(scoreString);
            } else {
            	scoreString = skipLines(scoreString,1);
            }
            // //////////////END of Player 1 data
        }

        if (scoreString.startsWith("SERVER")) {
            scoreString = skipLines(scoreString, 1);
            // player 2 serves
            server = PlayerEnum.PLAYER2;
        }
        
        scoreString.trim();

        // //////////////Player 2 data
        // Skip odds
        if (scoreString.charAt(0) != '\t' && !scoreString.startsWith("SERVER"))
            scoreString = scoreString
                    .substring(scoreString.indexOf("\n") + 1, scoreString.length());
        // skip initial tab
        scoreString = scoreString.substring(1, scoreString.length());
  
        // 5 sets
        int pos = 0;
        for (int i = 0; i < 5; i++) {
            try{
            playerTwoGames[i]= Integer.parseInt(scoreString.substring(0, scoreString.indexOf("\t")));
            scoreString = scoreString
                    .substring(scoreString.indexOf("\t") + 1, scoreString.length());
            
            if (scoreString.startsWith("\n") || 
                scoreString.startsWith("\t") || 
                scoreString.startsWith(" "))
        		{ pos = i+1; i = 5; scoreString = scoreString.trim();}
            }
            catch (Exception e){
                scoreString = scoreString.substring(scoreString.indexOf("\t"), scoreString.length());
                
                if (scoreString.startsWith("\n") || 
                        scoreString.startsWith("\t") || 
                        scoreString.startsWith(" "))
                        { scoreString = scoreString.trim();}
            }
        }
       
        // Points
        if (scoreString.substring(0,2).equals("Ad"))
            playerTwoPoints = 50;
        else 
            playerTwoPoints = Integer.parseInt(scoreString.substring(0, 2));
        // //////////////END of Player 2 data
        playerTwoGames[1] = 6;
        Score score = new Score();
        score.setSets(playerOneGames, playerTwoGames);
        score.setServer(server);
        score.setPlayerOnePoints(playerOnePoints);
        score.setPlayerTwoPoints(playerTwoPoints);
        match.setScore(score);
    }
}
