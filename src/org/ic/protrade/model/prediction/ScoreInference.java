package org.ic.protrade.model.prediction;

import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.domain.match.Score;
import org.ic.protrade.score.PredictionCalculator;


// Dynamic odds checking for score inference
public class ScoreInference {
	
	private Score currentGameInferredScore = new Score();
	
	// when calling use avgPeriod = 7s.
	public void runPointCheck(Match match, int avgPeriod){
		int point = isPointScored(match, avgPeriod);
		
		
		// Shahin, can you make updates on the chart with the point events, gamesWon? 
		// (once Corina creates a thread running this)
			
		
		// player 1 won the point
		if (point == 0){
			currentGameInferredScore.addPlayerOnePoint();
			// put updated inferred score on chart
		}
		// player 2 won the point
		else{
			currentGameInferredScore.addPlayerTwoPoint();
			// put point on chart
		}

	}	
	
    /* For checking to avoid falsely recognising post-scoring odds fluctuations.
	 * a timer value(avgPeriod) = 7s for the server is given in *Huang
	 * 
	 *  Method must wait 7s for odds to stabilize and after wait for new market data;
	 * related to Heuristic 4: Averaging Odds During Fluctuation / page 46 *Huang paper
	 *
	 * Returns 0 if player 1 won the point, 1 if player 2 scored
	 */
	public int isPointScored(Match match, int avgPeriod){
		int threshold = -1;
		int change = -1;
		int changeOverManyPoints = -1;
		
		// wait 7s(avgPeriod)
		//  Can you please help me on this, Corina? I'd need a separate thread that runs this method
		//  and put it to sleep for 7s
		
		
		double[] minMax = getOddsBounds(match, avgPeriod);
		double largeChange = StatisticMeasures.InterQuartileRange(
				PredictionCalculator.getOddsInTimePeriod(match, avgPeriod));
		double startOdds = match.getLastMarketData().getLastPriceMatched(match.getServer());
		
		// Keep checking odds till a point is score by either player
		while (change == -1 && threshold== -1 && changeOverManyPoints== -1){
			double prevOdds = match.getLastMarketData().getLastPriceMatched(match.getServer());		
			
			
			// wait for new MOdds
			//  thread sleep till new MOdds arrive
			//	 Can you please help me on this, Corina ?
			// After separate thread is done, Shahin can plot point events on chart 
			
			
			double actualOdds = match.getLastMarketData().getLastPriceMatched(match.getServer());
			
			threshold = checkPointByThreshold(actualOdds, minMax[0], minMax[1], match.getServer());
			change = checkPointByLargeChange(actualOdds,  prevOdds, largeChange, 
												minMax[0], minMax[1], match.getServer());
			changeOverManyPoints = checkPointByLargeChange(actualOdds,  startOdds, largeChange, 
															minMax[0], minMax[1], match.getServer());
		}
		// Threshold check takes priority. If threshold values not attaines, check for major odds change
		// to cover up the remaining cases in which points are scored
		if(threshold != -1)
			return threshold;
		if(change != -1)
			return change;
		
		return changeOverManyPoints;
	}
	
	/* Check if threshold min/max are exceeded by new market odds
	 * Returns 0 if player 1 won the point, 1 if player 2 scored, and -1 if neither scored
	 */
	private int checkPointByThreshold(double actualOdds, double min, double max, PlayerEnum server){	
		if (actualOdds >= max){
			if (server == PlayerEnum.PLAYER1)
				// player 1 lost the point to player 2
				return 1;
			else
				// player 1 won the point
				return 0;
		}
		else if(actualOdds <= min){
			if (server == PlayerEnum.PLAYER1)
				// player 2 lost the point to player 1
				return 0;
			else
				// player 2 won the point
				return 1;
		}	
		// in case threshold values are not touched
		return -1;
	}	
	
	/* Check if large change has occured with new market odds singly or over multiple points
	 * Returns 0 if player 1 won the point, 1 if player 2 scored, and -1 if neither scored
	 */
	public int checkPointByLargeChange(double actualOdds, double prevOdds, double largeChange, 
									  	double min, double max, PlayerEnum server){		
		if (Math.abs(actualOdds - prevOdds) >= largeChange){
			if (actualOdds > prevOdds){
				if (server == PlayerEnum.PLAYER1)
					// player 1 lost the point to player 2
					return 1;
				else
					// player 1 won the point
					return 0;
			}
			else{
				if (server == PlayerEnum.PLAYER1)
					// player 2 lost the point to player 1
					return 0;
				else
					// player 2 won the point
					return 1;
			}
		}
		// in case no large change is recorded
		return -1;
	}
	
	// Need large odds fluctuations to identify points accurately
    // Look at fluctuations of odds for the current server as these are likely to fluctuate more when a point is scored 
	// due to 2 changes: score and market perceived PWOS for the player serving
	// Receiving player has odds fluctuations due only to score.
	
	// First element of array is max bound, second is min bound
	private double[] getOddsBounds(Match match, int avgPeriod){
		double[] res = {0,0};
		Score curScore = match.getScore();
		PredictionCalculator calc = new PredictionCalculator(match);
		if (match.getServer() == PlayerEnum.PLAYER1){
			Score oneWins = curScore;
			Score oneLoses = curScore;
			oneWins.addPlayerOnePoint();
			oneLoses.addPlayerTwoPoint();
			
			match.setScore(oneWins);
			res[0] = calc.calculateOddsWithRecalibratedPWOS(match, avgPeriod)[0];
			
			match.setScore(oneLoses);
			res[1] = calc.calculateOddsWithRecalibratedPWOS(match, avgPeriod)[0];
			
		} else {
			Score twoWins = curScore;
			Score twoLoses = curScore;
			twoWins.addPlayerTwoPoint();
			twoLoses.addPlayerOnePoint();
			
			match.setScore(twoWins);
			res[0] = calc.calculateOddsWithRecalibratedPWOS(match, avgPeriod)[1];
			
			match.setScore(twoLoses);
			res[1] = calc.calculateOddsWithRecalibratedPWOS(match, avgPeriod)[1];
			
		}
		return res;
	}
}