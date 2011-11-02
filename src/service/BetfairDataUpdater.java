package src.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import src.domain.EventBetfair;
import src.domain.MOddsMarketData;
import src.domain.Match;
import src.model.connection.BetfairExchangeHandler;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class BetfairDataUpdater implements DataUpdater {
    private List<EventBetfair> events;
    private HashMap<EventBetfair, Match> matches;
    private static Logger log = Logger.getLogger(BetfairDataUpdater.class);

    public BetfairDataUpdater() {
        matches = new HashMap<EventBetfair, Match>();
        events = new ArrayList<EventBetfair>();
    }

    public void addEvent(Match match) {
        matches.put(match.getEventBetfair(), match);
        events.add(match.getEventBetfair());
    }

    @Override
    public void run() {
        HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
        for (EventBetfair eb : events) {
            MOddsMarketData marketData = BetfairExchangeHandler
                    .getMarketOdds(eb);
            if (marketData.getPl1Back() != null)
                matches.get(eb).addMarketData(marketData);
            newMap.put(eb, marketData);
        }
        LiveDataFetcher.handleEvent(newMap);
    }
}
