package org.ic.tennistrader.score;

import java.text.DecimalFormat;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;

public class PredictionCalculator {

	private static double pwosA;
	private static double pwosB;
	private static double pwgOnAverageA;
	private static double pwgOnAverageB;
	private static double pwgA;
	private static double pwgB;
	
	public static double[] getP1Data(double result[]){
	    double [] res = new double[4];
        for (int i=0;i<res.length;i++){
            res[i] = result[2*i];
        }
        return res;
	}
	
public static double[] getP2Data(double result[]){
    double [] res = new double[4];
    for (int i=0;i<res.length;i++){
        res[i] = result[2*i+1];
    }
    return res;
    }
	
	public static double[] calculate(Match match) {
		double[] result = new double[8];

		Statistics playerOneStats = match.getPlayerOne().getStatistics();
		Statistics playerTwoStats = match.getPlayerTwo().getStatistics();
		
		
		PlayerEnum server = match.getServer();
		Score score = match.getScore();

		// Probability of player one/two to win on their serve in a game by
		// ability
		pwgOnAverageA = calculatePWG(calculatePWOS(playerOneStats));
		pwgOnAverageB = calculatePWG(calculatePWOS(playerTwoStats));
		///System.out.println("AAAAA" + pwgA);
		///System.out.println("BBBBB" + pwgB);

		// Even indices correspond to player 1, odd ones to player 2;
		result[0] = pwosA = calculatePWOS(playerOneStats);
		result[1] = pwosB = calculatePWOS(playerTwoStats);
		
		int [] points = convertPoints(score.getPlayerOnePoints(), score.getPlayerTwoPoints());
		int pointsOne = points[0];
		int pointsTwo = points[1];
		
		if (server == PlayerEnum.PLAYER1) {
			result[2] = pwgA = calculateGamePercent(pointsOne,pointsTwo, result[0], 1);
			result[3] = pwgB = 1.0 - result[2];
			//System.out.println("GAME score 1: " + score.getCurrentSetScore().getPlayerOneGames());
			//System.out.println("GAME score 2: " + score.getCurrentSetScore().getPlayerTwoGames());
			//System.out.println("Points One: " + pointsOne);
			//System.out.println("Points Two: " + pointsTwo);
			//System.out.println(score.getPlayerTwoSets());
			result[4] = calculateSetPercent(pointsOne, pointsTwo, 
							score.getCurrentSetScore().getPlayerOneGames(), 
							score.getCurrentSetScore().getPlayerTwoGames(), 
							1, 1);
			result[5] = 1.0 - result[4];
			result[6] = calculateMatchPercent(result[4]);
			result[7] = 1-result[6];
		} else {
            result[3] = pwgB = calculateGamePercent(pointsOne,pointsTwo,  1 - result[1], 0);
		    result[2] = pwgA = 1.0 - result[3];
		    result[5] = calculateSetPercent(pointsOne, pointsTwo, 
		    				score.getCurrentSetScore().getPlayerOneGames(), 
		    				score.getCurrentSetScore().getPlayerTwoGames(),
		    				0, 1);
			result[4] = 1.0 - result[5];
			result[7] = calculateMatchPercent(result[5]);
			result[6] = 1-result[7];
		}
		
		DecimalFormat df = new DecimalFormat("#.###");
		for (int i =0; i<8; i++){
			
			result[i] = Double.parseDouble(df.format(result[i]));
		}
		
		return result;
	}

	// convert from standard points (e.g. 15, 30 ... ) to short points (1,2..)
	private static int[] convertPoints(int pointsOne, int pointsTwo) {
		if ( pointsOne >=  40 && pointsTwo >= 40)
		{
			int[] pts= {(int)Math.floor(pointsOne/15.0),(int)Math.floor(pointsTwo/15.0)};
			return pts;
		}
		int[] pts= {(int)Math.ceil(pointsOne/15.0),(int)Math.ceil(pointsTwo/15.0)};
		return pts;
	}

	// Probability of winning a point on service
	// P (win) = P (noFault)P (win|noFault) + (1 − P (noFault))P (win|Fault)
	private static double calculatePWOS(Statistics serverStats) {
		return (serverStats.getFirstServePercent()
				* serverStats.getFirstServeWins() + (1 - serverStats
				.getFirstServePercent()) * serverStats.getSecondServeWins());
	}

	private static double po(double a, double b){
	    return Math.pow(a,b);
	}
	// Calculating the chance of one player to win a game he/she's serving for
	// based on ability (ind. of scoreline)
	private static double calculatePWG(double p) {
		double result = 0;
		//result = po(p, 4) + 4 * po(p,4)
		result = 15 - 24 * p + 10 * Math.pow(p, 2)
				+ (20 * p * Math.pow(1 - p, 3) / (1 - 2 * p * (1 - p)));
		return (result * Math.pow(p, 4));
	}

	private static double calculateGamePercent(int a, int b, double pwg, int c) {
		// pwg = (server == PlayerEnum.PLAYER1)? playerOnePWG : playerTwoPWG;
		if (a == 4 && b <= 2)
			return c;
		if (b == 4 && a <= 2)
			return 1-c;
		if (a == 3 && b == 3)
			return (Math.pow(pwg, 2) / (Math.pow(pwg, 2) + Math.pow(1 - pwg, 2)));
		return (pwg * calculateGamePercent(a + 1, b, pwg,c) + (1 - pwg)
				* calculateGamePercent(a, b + 1, pwg,c));
	}
	
	private static double calculateTiebreakerGamePercent(int a, int b, int c) {
		if(a == 7 && b >= 0  && b <= 5) 
			return c;
		if(b == 7 && a >= 0  && a <= 5 )
			return 1-c;
		if ( a == 6 && b == 6){
			if(c == 1 ) 
				return pwosA*(1-pwosB)/(pwosA*(1-pwosB)+(1-pwosA)*pwosB);
			else 
				return pwosB*(1-pwosA)/(pwosA*(1-pwosB)+(1-pwosA)*pwosB);
		}
		if( c == 1)
			return pwosA*calculateTiebreakerGamePercent(a + 1, b, (a+b)%2) + 
					(1-pwosA)*calculateTiebreakerGamePercent(a, b + 1, (a+b)%2 );
		else 
			return pwosB*calculateTiebreakerGamePercent(a, b + 1, 1 - (a+b)%2) + 
				(1-pwosB)*calculateTiebreakerGamePercent(a+1, b, 1 -(a+b)%2 );
	}

	private static double calculateSetPercent(int a, int b, int c, int d, int s, int firstRun) {
		if( (c == 6 && d >= 0  && d <= 4) ||  (c == 7 && d == 5) ){
			if(firstRun == 1)
				return s;
			else
				return firstRun - 5;
		}
		if( (d == 6 && c >= 0  && c <= 4) ||  (d == 7 && c == 5) ){
			if(firstRun == 1)
				return 1-s;
			else 
				return 1- (firstRun - 5);			
		}
		if( c == 6 && d == 6 ){
			double tb = 0;
			if(firstRun == 1)
			{
				firstRun = s+5;
				tb = calculateTiebreakerGamePercent(a, b, s);
			}else {
				tb = calculateTiebreakerGamePercent(0, 0, s);
			}
			return tb;
		}
		if (firstRun == 1)
			firstRun = s+5;
		if(s == 1 ){
			if(firstRun == 1)
				return pwgA*calculateSetPercent(a, b, c + 1, d, 1 - s, firstRun) +
				(1- pwgA) * calculateSetPercent(a, b, c, d + 1, 1 - s, firstRun);
			else
				return pwgOnAverageA*calculateSetPercent(a, b, c + 1, d, 1 - s, firstRun) +
				(1- pwgOnAverageA) * calculateSetPercent(a, b, c, d + 1, 1 - s, firstRun);
						
		} else {
			if(firstRun == 1)
				return pwgB*calculateSetPercent(a, b , c, d + 1 , 1 - s, firstRun) +
				(1- pwgB) * calculateSetPercent(a, b, c + 1, d, 1 - s, firstRun);
			else
				return pwgOnAverageB*calculateSetPercent(a, b , c, d + 1 , 1 - s, firstRun) +
				(1- pwgOnAverageB) * calculateSetPercent(a, b, c + 1, d, 1 - s, firstRun);
		}
	}

	private static double calculateMatchPercent(double set ) {
		return po(set,2)*(3 - 2*set);
	}
}
