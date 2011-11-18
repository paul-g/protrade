package org.ic.tennistrader.domain.match;

import java.util.ArrayList;
import java.util.List;
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
	private Player player1 = new Player();
	private Player player2 = new Player();
	private EventBetfair eventBetfair;
	private List<MOddsMarketData> marketDatas;
	private Score score;
	private boolean namesSet = false;
	
	public RealMatch(String player1, String player2, EventBetfair eb) {
	    this.score = new Score();
	    String name = eb.getName();
	    String[] names = name.split(" v ");
	    if ( names.length == 2){
	        this.player1.setLastname(names[0]);
		    this.player2.setLastname(names[1]);
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
        if (!namesSet && marketData.getPlayer1() != null) {
            setPlayerNames(marketData);
            namesSet = true;
        }
	}

    private void setPlayerNames(MOddsMarketData marketData) {
        int lastNameIndex = marketData.getPlayer1().lastIndexOf(' ');
        if (lastNameIndex > 0) {
            this.player1.setFirstname(marketData.getPlayer1().substring(0,
                    lastNameIndex));
            this.player1.setLastname(marketData.getPlayer1().substring(
                    lastNameIndex + 1));
            lastNameIndex = marketData.getPlayer2().lastIndexOf(' ');
            this.player2.setFirstname(marketData.getPlayer2().substring(0,
                    lastNameIndex));
            this.player2.setLastname(marketData.getPlayer2().substring(
                    lastNameIndex + 1));
        } else {
            this.player1.setLastname(marketData.getPlayer1());
            this.player2.setLastname(marketData.getPlayer2());
        }
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
	
	public void registerForUpdate(UpdatableWidget widget){
	    LiveDataFetcher.registerLive(widget, this);
	}

    @Override
    public Player getPlayerOne() {
        return player1;
    }

    @Override
    public Player getPlayerTwo() {
        return player2;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void setScore(Score score) {
       this.score = score;
    }
}
