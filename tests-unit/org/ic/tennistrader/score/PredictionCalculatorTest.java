package org.ic.tennistrader.score;

import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PredictionCalculatorTest {

    private Match match;

    @Before
    public void setUp() {
        String filename = "data/test/fracsoft-reader/tso-fed.csv";
        match = new HistoricalMatch(filename);
        Statistics statsOne = new Statistics(0.615, 0.781, 0.525);
        match.getPlayerOne().setStatistics(statsOne);
        Statistics statsTwo = new Statistics(0.634, 0.785, 0.563);
        match.getPlayerTwo().setStatistics(statsTwo);
    }

    @After
    public void tearDown() {
    	match = null;
    }
    
    @Test
    public void pointPrediction() {
    	double[] expected = {0.682, 0.704};
    	assertEquals(0, Double.compare(PredictionCalculator.calculate(match)[0], expected[0]));
    	assertEquals(0, Double.compare(PredictionCalculator.calculate(match)[1], expected[1]));
    }

    @Test 
    public void GamePrediction(){
    	double[] expected = {0.943, 0.057};
    	Score score = new Score();   	
    	score.setPlayerOnePoints(40);
    	score.setPlayerTwoPoints(30);
    	score.setServer(PlayerEnum.PLAYER1);
    	match.setScore(score);
    	
    	double[] result = PredictionCalculator.calculate(match);
    	assertEquals(0, Double.compare(result[2], expected[0]) );
    	assertEquals(0, Double.compare(result[3], expected[1]) );
     }
    
     @Test 
     public void SetPrediction(){
    	double[] expected = {0.846, 0.154};
     	Score score = new Score(3);
     	score.setServer(PlayerEnum.PLAYER1);
     	score.setPlayerOnePoints(40);
     	score.setPlayerTwoPoints(15);
     	int [] playerOneSet = {4,7,3};
     	int [] playerTwoSet = {6,5,2};
     	score.setSets(playerOneSet, playerTwoSet);    	
     	match.setScore(score);
     	
     	double[] result = PredictionCalculator.calculate(match);
     	assertEquals(0, Double.compare(result[4], expected[0]) );
     	assertEquals(0, Double.compare(result[5], expected[1]) );
     }
     
     @Test 
     public void MatchPrediction(){
    	double[] expected = {0.989, 0.011};
     	Score score = new Score(3);
     	score.setServer(PlayerEnum.PLAYER1);
     	score.setPlayerOnePoints(40);
     	score.setPlayerTwoPoints(0);
     	int [] playerOneSet = {4,7,5};
     	int [] playerTwoSet = {6,5,4};
     	score.setSets(playerOneSet, playerTwoSet);    	
     	match.setScore(score);
     	
     	double[] result = PredictionCalculator.calculate(match);
     	assertEquals(0, Double.compare(result[6], expected[0]) );
     	assertEquals(0, Double.compare(result[7], expected[1]) );
     }
}