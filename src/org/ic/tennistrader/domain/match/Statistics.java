package org.ic.tennistrader.domain.match;

public class Statistics {
	private int FirstServePercent;
	private int FirstServeWins;
	private int SecondServeWins;
	
	public Statistics() {
        init();
    }
	
	private void init() {
		FirstServePercent = 0;
		FirstServeWins = 0;
		SecondServeWins = 0;
    }
	
	public int getFirstServePercent() {
        return FirstServePercent;
    }
	
	public int getFirstServeWins() {
       return FirstServeWins;
    }
	
	public int getSecondServeWins() {
       return SecondServeWins;
    }
	
	
	public void setFirstServePercent( int FirstServePercent ) {
        this.FirstServePercent = FirstServePercent;
    }
	
	public void setFirstServeWins( int FirstServeWins ) {
        this.FirstServeWins = FirstServeWins;
    }
	
	public void setSecondServeWins( int SecondServeWins ) {
        this.SecondServeWins = SecondServeWins;
    }	
	
}
