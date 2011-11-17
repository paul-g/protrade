package org.ic.tennistrader.domain;

import org.ic.tennistrader.domain.match.SetScore;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetScoreTest extends ScoringTest{

    SetScore score = new SetScore();
    
    @Test
    public void sixZeroWin(){
        
        
        for (int i=0;i<5; i++) {
            score.addPlayerOneGame();
            assertFalse(score.isFinished());
        }
        
        assertSetScoreIs(score, 5 , 0);
        
        score.addPlayerOneGame();
        assertTrue(score.isFinished());
    }
    
    @Test
    public void sevenFiveWin(){
        
        for (int i=0;i<5; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(score.isFinished());
        }

        assertSetScoreIs(score, 5 , 5);
        
        score.addPlayerOneGame();
        assertFalse(score.isFinished());
        
        score.addPlayerOneGame();
        assertTrue(score.isFinished());
    }
    
    
    @Test
    public void tiebreakTest(){
        
        for (int i=0;i<6; i++) {
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            assertFalse(score.isFinished());
        }
        
        assertSetScoreIs(score, 6 , 6);
        
        for (int i=0;i<6; i++) {
            assertTiebrakeScoreIs(score, i, i);
            score.addPlayerOneGame();
            score.addPlayerTwoGame();
            //assertFalse(score.isFinished());
        }
        
        //assertTiebrakeScoreIs(score, i, i);
        
        score.addPlayerOneGame();
        assertFalse(score.isFinished());
        
        score.addPlayerOneGame();
        assertSetScoreIs(score,7,6);
        assertTrue(score.isFinished());
    }
    
}
