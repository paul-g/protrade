package org.ic.tennistrader.score;

import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PredictionCalculatorTest {

    private Match match;
    private PredictionCalculator pc;

    // private

    @Before
    public void setUp() {
        String filename = "data/test/fracsoft-reader/tso-fed.csv";
        match = new HistoricalMatch(filename);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void pointPrediction() {

    }

    /*
     * @Test public void pointPrediction(){
     * 
     * }
     * 
     * 
     * @Test public void pointPrediction(){
     * 
     * }
     */
}
