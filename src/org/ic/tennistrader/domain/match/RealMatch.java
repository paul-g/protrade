package org.ic.tennistrader.domain.match;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.model.connection.BetfairExchangeHandler;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

/**
 * An upcoming or in play match
 * @author pg1709
 *
 */
public class RealMatch implements Match{
	private String player1, player2;
	private EventBetfair eventBetfair;
	private List<MOddsMarketData> marketDatas;
	
	public RealMatch(String player1, String player2, EventBetfair eb) {
	    String name = eb.getName();
	    String[] names = name.split(" v ");
	    if ( names.length == 2){
	        this.player1 = names[0];
		    this.player2 = names[1];
	    }
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
	
	public boolean isInPlay(){
        if (this.getRecentMarketData() == null)
            this.addMarketData(BetfairExchangeHandler.getMarketOdds(this.getEventBetfair()));
        return this.getRecentMarketData().getDelay() > 0;
	}
	
	public String getName(){
	    return toString();
	}
	
	public void registerForUpdate(UpdatableWidget widget, Composite composite){
	    LiveDataFetcher.registerLive(widget, this, composite);
	}

    @Override
    public String getPlayer1() {
        return player1;
    }

    @Override
    public String getPlayer2() {
        return player2;
    }
}
