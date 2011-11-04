package src.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import src.domain.EventBetfair;
import src.domain.MOddsMarketData;
import src.domain.match.RealMatch;
import src.model.connection.BetfairExchangeHandler;

import org.apache.log4j.Logger;

public class BetfairDataUpdater implements DataUpdater {
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
