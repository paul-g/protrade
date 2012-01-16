package org.ic.protrade.domain.match;

public class Statistics {
	private double firstServePercent;
	private double firstServeWins;
	private double secondServeWins;
	
	public Statistics() {
        init();
    }
	
	public Statistics(double firstServePercent, double firstServeWins,
			double secondServeWins) {
		super();
		this.firstServePercent = firstServePercent;
		this.firstServeWins = firstServeWins;
		this.secondServeWins = secondServeWins;
	}

	private void init() {
		firstServePercent = 0.0;
		firstServeWins = 0.0;
		secondServeWins = 0.0;
    }
	
	public double getFirstServePercent() {
        return firstServePercent;
    }
	
	public double getFirstServeWins() {
       return firstServeWins;
    }
	
	public double getSecondServeWins() {
       return secondServeWins;
    }
	
	public void setFirstServePercent( double firstServePercent ) {
        this.firstServePercent = firstServePercent;
    }
	
	public void setFirstServeWins( double FirstServeWins ) {
        this.firstServeWins = FirstServeWins;
    }
	
	public void setSecondServeWins( double SecondServeWins ) {
        this.secondServeWins = SecondServeWins;
    }	
}
