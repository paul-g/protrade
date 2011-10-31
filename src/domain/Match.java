package src.domain;

public class Match {
	private String player1, player2;
	private EventBetfair eventBetfair;
	MOddsMarketData marketData;
	
	public Match(String player1, String player2, EventBetfair eb) {
		this.player1 = player1;
		this.player2 = player2;
		this.setEventBetfair(eb);
		marketData = null;
	}
	
	public String toString() {
		//return this.player1 + " vs " + this.player2;
		return this.eventBetfair.getName();
	}

	public void setEventBetfair(EventBetfair eventBetfair) {
		this.eventBetfair = eventBetfair;
	}

	public EventBetfair getEventBetfair() {
		return eventBetfair;
	}
	
	public void setMarketData(MOddsMarketData marketData) {
		this.marketData = marketData;
	}
	
	public MOddsMarketData getMarketData() {
		return this.marketData;
	}
}
