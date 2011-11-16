package org.ic.tennistrader.domain;

import org.ic.tennistrader.domain.match.SetScore;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetScoreTest {

    @Test
    public void sixZeroWin(){
        SetScore score = new SetScore();
        
        for (int i=0;i<5; i++) {
            score.addPlayerOneGame();
            assertFalse(score.isFinished());
        }
        
        score.addPlayerOneGame();
        assertTrue(score.isFinished());
    }
    
    @Test
    public void sevenFiveWin(){
        SetScore score = new SetScore();
        
        for (int i=0;i<5; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(score.isFinished());
        }
        
        score.addPlayerOneGame();
        assertFalse(score.isFinished());
        
        score.addPlayerOneGame();
        assertTrue(score.isFinished());
    }
    
    
    @Test
    public void tiebreakTest(){
        SetScore score = new SetScore();
        
        for (int i=0;i<6; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(score.isFinished());
        }
        
        for (int i=0;i<6; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(score.isFinished());
        }
        
        score.addPlayerOneGame();
        assertFalse(score.isFinished());
        
        score.addPlayerOneGame();
        assertTrue(score.isFinished());
    }
    
}
