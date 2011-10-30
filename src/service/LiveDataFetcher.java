package src.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import src.domain.EventBetfair;
import src.domain.MOddsMarketData;
import src.domain.Match;
import src.domain.UpdatableWidget;

public class LiveDataFetcher {
	private static BetfairDataUpdater betfairDataUpdater = null;
	private static HashMap<Integer, UpdatableWidget> listeners = new HashMap<Integer, UpdatableWidget>();
	private static Logger log = Logger.getLogger(LiveDataFetcher.class);
	
	public static void register(UpdatableWidget widget, Match match, Composite comp) {
		boolean first = false;
		if (betfairDataUpdater == null) {
			betfairDataUpdater = new BetfairDataUpdater(comp);
			first = true;
		}
		betfairDataUpdater.addEvent(match.getEventBetfair());
		listeners.put(match.getEventBetfair().getBetfairId(), widget);
		log.info("go to run thread");
		if (first) {
			betfairDataUpdater.run();
		}
		log.info("after run in thread");
		
	}
	
	public static void handleEvent(HashMap<EventBetfair, MOddsMarketData> data) {		
		Iterator<EventBetfair> i = data.keySet().iterator();
		while (i.hasNext()) {
			EventBetfair eb = i.next();
			listeners.get(eb.getBetfairId()).handleUpdate(data.get(eb));
		}
	}

}
