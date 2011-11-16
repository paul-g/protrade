package org.ic.tennistrader.domain.match;

import java.util.ArrayList;
import java.util.List;

public class Score {
    
    private enum Player{
        PLAYER1, PLAYER2;
    }
    
    // AD = 50
    private int playerOnePoints;
    private int playerTwoPoints;
    
    private List<SetScore> scores = new ArrayList<SetScore>();
    private SetScore currentSet;
    
    public Score(){
        currentSet = new SetScore();
        scores.add(currentSet);
    }
    
    public void addPlayerOnePoint(){
        if (currentSet.isTiebreak())
            gameWon(Player.PLAYER1);
        
        switch (playerOnePoints){
            case 0 :
            case 15 : playerOnePoints += 15; break; 
            case 30 : playerOnePoints += 10; break;
            case 40 : 
                if (playerTwoPoints < 40)
                    currentSet.addPlayerOneGame();
                else { 
                    if (playerTwoPoints == 50 )
                        playerOnePoints = playerTwoPoints = 40;
                    else 
                        currentSet.addPlayerOneGame();
                }
        }
    }
    
    public void addPlayerTwoPoint(){
        if (currentSet.isTiebreak())
            gameWon(Player.PLAYER2);
        
        switch (playerTwoPoints){
            case 0 :
            case 15 : playerTwoPoints += 15; break; 
            case 30 : playerTwoPoints += 10; break;
            case 40 : 
                if (playerOnePoints < 40)
                    gameWon(Player.PLAYER2);
                else { 
                    if (playerOnePoints == 50 )
                        playerOnePoints = playerTwoPoints = 40;
                    else 
                        gameWon(Player.PLAYER2);
                }
        }
    }
    
    private void gameWon(Player player){
        switch (player){
            case PLAYER1: currentSet.addPlayerOneGame(); break;
            case PLAYER2: currentSet.addPlayerTwoGame(); break;
        }
        
        playerOnePoints = 0;
        playerTwoPoints = 0;
        
        //if (currentSet.isFinished())
        
        
    }
}
