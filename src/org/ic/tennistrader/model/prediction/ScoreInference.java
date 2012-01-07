package org.ic.tennistrader.model.prediction;

import java.util.Timer;

import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.score.PredictionCalculator;

// Dynamic odds bound checking for score inference
public class ScoreInference {
	
	// Need large odds fluctuations to identify points accurately
    //	Look at fluctuations of odds for the current server as these are likely to fluctuate more when a point is scored 
	// due to 2 changes: score and market perceived PWOS for the player serving
	// Receiving player has odds fluctuations due only to score.
	
	// First element of array is max bound, second is min bound
	private double[] getOddsBounds(Match match){
		double[] res = {0,0};
		Score curScore = match.getScore();
		PredictionCalculator calc = new PredictionCalculator(match);
		if (match.getServer() == PlayerEnum.PLAYER1){
			Score oneWins = curScore;
			Score oneLoses = curScore;
			oneWins.addPlayerOnePoint();
			oneLoses.addPlayerTwoPoint();
			
			match.setScore(oneWins);
			res[0] = calc.calculateOddsWithRecalibratedPWOS(match)[0];
			
			match.setScore(oneLoses);
			res[1] = calc.calculateOddsWithRecalibratedPWOS(match)[0];
			
		} else {
			Score twoWins = curScore;
			Score twoLoses = curScore;
			twoWins.addPlayerTwoPoint();
			twoLoses.addPlayerOnePoint();
			
			match.setScore(twoWins);
			res[0] = calc.calculateOddsWithRecalibratedPWOS(match)[1];
			
			match.setScore(twoLoses);
			res[1] = calc.calculateOddsWithRecalibratedPWOS(match)[1];
			
		}
		return res;
	}
	
	// Returns 0 if player 1 won the point, 1 if player 2 scored, and -1 if neither scored
	private int isPointScored(Match match, MOddsMarketData data){
		double[] maxMin = getOddsBounds(match);
		double max = maxMin[0];
		double min = maxMin[1];
		double actualOdds;
		if (match.getServer() == PlayerEnum.PLAYER1){
			actualOdds = data.getPl1LastMatchedPrice();
			if (actualOdds >= max){
				// player 1 lost the point to player 2
				return 1;
			}
			if (actualOdds <= min){
				// player 1 won the point
				return 0;
			}
		}
		else {
			actualOdds = data.getPl2LastMatchedPrice();
			if (actualOdds >= max){
				// player 2 lost the point to player 1
				return 0;
			}
			if (actualOdds <= min){
				// player 2 won the point
				return 1;
			}
		}		
		return -1;
	}
	
	// returns same values as isPointScored(), but introduces a timer
	// for checking to avoid falsely recognising post-scoring odds fluctuations
	// A timer value = 7s for the server is given in *Huang
	
	// Method must execute from at 7s intervals 
	// Heuristic 4: Averaging Odds During Fluctuation / page 46 *Huang paper
	//(can you please help me on this, Corina?)
	public int checkScoredPoint(Match match, MOddsMarketData data){
		//wait(7000);
		return isPointScored(match, data);
	}
	
	// Heuristic 5 not implemented yet. Need a way to record MarketOdds values over time to average them.
	
}
