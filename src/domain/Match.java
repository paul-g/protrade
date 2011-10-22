package src.domain;

public class Match extends EventMarketBetfair {
	private String player1, player2;
	
	public Match(String player1, String player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public String toString() {
		return this.player1;// + " vs " + this.player2;
	}
}
