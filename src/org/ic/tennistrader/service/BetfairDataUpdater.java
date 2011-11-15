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
    private List<EventBetfair> events;
    private HashMap<EventBetfair, RealMatch> matches;
    
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BetfairDataUpdater.class);

    public BetfairDataUpdater() {
        matches = new HashMap<EventBetfair, RealMatch>();
        events = new ArrayList<EventBetfair>();
    }

    public void addEvent(RealMatch match) {
        matches.put(match.getEventBetfair(), match);
        events.add(match.getEventBetfair());
    }

    @Override
    public void run() {
		while (!this.stop) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.info("Betfair thread interrupted");
			}
			HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
			for (EventBetfair eb : events) {
				MOddsMarketData marketData = BetfairExchangeHandler
						.getMarketOdds(eb);
				if (marketData.getPl1Back() != null) {
					matches.get(eb).addMarketData(marketData);

				}
				newMap.put(eb, marketData);
			}
			LiveDataFetcher.handleEvent(newMap);
		}
    }

	@Override
	public void setStop() {
		this.stop = true;
	}
}