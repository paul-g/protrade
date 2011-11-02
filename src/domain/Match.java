package src.domain;

import java.util.ArrayList;
import java.util.List;

public class Match {
	private String player1, player2;
	private EventBetfair eventBetfair;
	private List<MOddsMarketData> marketDatas;
	
	public Match(String player1, String player2, EventBetfair eb) {
		this.player1 = player1;
		this.player2 = player2;
		this.setEventBetfair(eb);
		setMarketDatas(new ArrayList<MOddsMarketData>());
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
	
	public void addMarketData(MOddsMarketData marketData) {
		this.marketDatas.add(marketData);
	}
	
	public MOddsMarketData getRecentMarketData() {
		if (this.marketDatas.size() == 0)
			return null;
		return this.marketDatas.get(this.marketDatas.size() - 1);
	}
	
	public void setMarketDatas(List<MOddsMarketData> marketDatas) {
		this.marketDatas = marketDatas;
	}

	public List<MOddsMarketData> getMarketDatas() {
		return marketDatas;
	}
}
