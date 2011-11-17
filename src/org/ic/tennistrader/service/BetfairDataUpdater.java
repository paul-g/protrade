package org.ic.tennistrader.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.model.connection.BetfairExchangeHandler;

import org.apache.log4j.Logger;

public class BetfairDataUpdater extends DataUpdater {
    //private List<EventBetfair> events;
	private BetfairUpdaterEvents synchronizedEvents;
    private HashMap<EventBetfair, RealMatch> matches;
    private int i = 0;
    
    private static Logger log = Logger.getLogger(BetfairDataUpdater.class);

    public BetfairDataUpdater() {
        matches = new HashMap<EventBetfair, RealMatch>();
        //events = new ArrayList<EventBetfair>();
        synchronizedEvents = new BetfairUpdaterEvents();
    }

    public void addEvent(RealMatch match) {
        EventBetfair eventBetfair = match.getEventBetfair();
		matches.put(eventBetfair, match);
        //events.add(eventBetfair);
		synchronizedEvents.addEvent(eventBetfair);
    }

    @Override
    public void run() {
		while (!this.stop) {			
			HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
			List<EventBetfair> events = new ArrayList<EventBetfair>(synchronizedEvents.getEvents());
			for (EventBetfair eb : events) {
				//System.out.println("Size of events - " + events.size());
				RealMatch match = matches.get(eb);
				if (match.isInPlay() || match.getRecentMarketData() == null || i == 0) {
					MOddsMarketData marketData = BetfairExchangeHandler
							.getMarketOdds(eb);
					if (marketData.getPl1Back() != null) {
						matches.get(eb).addMarketData(marketData);
					}
					newMap.put(eb, marketData);
				}
				i = (i + 1) % 10;
			}
			LiveDataFetcher.handleEvent(newMap);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.info("Betfair thread interrupted");
			}
		}
		log.info("Stopped Betfair thread");
    }

	@Override
	public void setStop() {
		this.stop = true;
	}
	
	public List<EventBetfair> getEvents() {
		return synchronizedEvents.getEvents();
	}

	public void removeEvent(EventBetfair eventBetfair) {
		synchronizedEvents.removeEvent(eventBetfair);
	}
}