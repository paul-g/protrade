package org.ic.tennistrader.domain.match;

public class Statistics {
	private double FirstServePercent;
	private double FirstServeWins;
	private double SecondServeWins;
	
	public Statistics() {
        init();
    }
	
	private void init() {
		FirstServePercent = 0;
		FirstServeWins = 0;
		SecondServeWins = 0;
    }
	
	public double getFirstServePercent() {
        return FirstServePercent;
    }
	
	public double getFirstServeWins() {
       return FirstServeWins;
    }
	
	public double getSecondServeWins() {
       return SecondServeWins;
    }
	
	
	public void setFirstServePercent( double FirstServePercent ) {
        this.FirstServePercent = FirstServePercent;
    }
	
	public void setFirstServeWins( double FirstServeWins ) {
        this.FirstServeWins = FirstServeWins;
    }
	
	public void setSecondServeWins( double SecondServeWins ) {
        this.SecondServeWins = SecondServeWins;
    }	
	
}
