package org.ic.tennistrader.score;

import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.SetScore;
import org.ic.tennistrader.domain.match.Statistics;

public class PredictionCalculator {
	
	private Score score;
	private Statistics playerOneStats;
	private Statistics playerTwoStats;
	private PlayerEnum server;	
	
	// Probability of player one/two to win on their serve in a game by ability
	private double playerOnePWG = 0;
	private double playerTwoPWG = 0;
	
	public PredictionCalculator() {
	        init();
	}
	
	private void init() {
        score = new Score();
        playerOneStats = new Statistics();
        playerTwoStats = new Statistics();
        server = PlayerEnum.PLAYER1;
    }
	
	public PredictionCalculator(Score score, Statistics playerOneStats, Statistics playerTwoStats, PlayerEnum server)
	{
		this.score = score;
        this.playerOneStats = playerOneStats;
        this.playerTwoStats = playerTwoStats;
        this.server = server;
        
        playerOnePWG = calculatePWG(calculatePWOS(playerOneStats));
		playerTwoPWG = calculatePWG(calculatePWOS(playerTwoStats));       
	}
	
	public double[] calculate()
	{
		double pwg = 0;
		pwg = (server == PlayerEnum.PLAYER1)? playerOnePWG : playerTwoPWG;
		double[] probs = new double[3];
		probs[1] = calculateGamePercent(score.getPlayerOnePoints(), score.getPlayerTwoPoints(), pwg);
		return probs;
	}
	
	// Probability of winning a point on service
	// P (win) = P (noFault)P (win|noFault) + (1 − P (noFault))P (win|Fault)
	private double calculatePWOS( Statistics serverStats)
	{
		return (serverStats.getFirstServePercent() * serverStats.getFirstServeWins() + 
				(1- serverStats.getFirstServePercent()) * serverStats.getSecondServeWins());
	}
	
	// Calculating the chance of one player to win a game he/she's serving for based on ability (ind. of scoreline)	
	private double calculatePWG(double p)
	{
		double result = 0;
		result = 15 - 4*p - 10*Math.pow(p, 2) + (20 * p * Math.pow(1-p, 3)/(1 - 2*p*(1-p) ) );
		return (result * Math.pow(p, 4)) ;
	}
	
	private double calculateGamePercent(int a, int b, double pwg)
	{
		
		//pwg = (server == PlayerEnum.PLAYER1)? playerOnePWG : playerTwoPWG;
		if (a == 4 && b<=2) return 1;
		if (b == 4 && a<=2) return 0;
		if (a == 3 && b == 3) return(Math.pow(pwg,2)/(Math.pow(pwg, 2) + Math.pow(1-pwg , 2)));		
		return 
		(
				pwg * calculateGamePercent(a+1, b, pwg) + 
				(1 - pwg) * calculateGamePercent(a, b+1, pwg) 				
		);
	}
	
	private int calculateSetPercent(int a, int b)
	{
		
		return 0;
	}
	
	private int calculateMatchPercent()
	{
		return 0;
	}
}
