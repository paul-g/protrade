package src.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import src.domain.EventBetfair;
import src.domain.MOddsMarketData;
import src.domain.Match;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class BetfairDataUpdater implements Runnable {
	private Composite comp;
	private List<EventBetfair> events;
	private HashMap<EventBetfair, Match> matches;
	private static Logger log = Logger.getLogger(BetfairDataUpdater.class);

	public BetfairDataUpdater(Composite comp) {
		this.comp = comp;
		matches = new HashMap<EventBetfair, Match>();
		events = new ArrayList<EventBetfair>();
	}
	
	public void addEvent(Match match) {
		matches.put(match.getEventBetfair(), match);
		events.add(match.getEventBetfair());
	}
	
	public List<EventBetfair> getEvents() {
		return events;
	}
	
	@Override
	public void run() {
		/*
		comp.getDisplay().timerExec(0, new Runnable() {
			@Override
			public void run() {
				fillChartData(chart);
				chart.redraw();
				if (!comp.isDisposed()) comp.update();
				comp.getDisplay().timerExec(1000, this);
			}
		});
		*/
		
		comp.getDisplay().timerExec(0, new Runnable() {
			@Override
			public void run() {				
				HashMap<EventBetfair, MOddsMarketData> newMap = new HashMap<EventBetfair, MOddsMarketData>();
				for (EventBetfair eb : events) {
					MOddsMarketData marketData = BetfairExchangeHandler.getMarketOdds(eb);
					if (marketData.getPl1Back() != null)
						matches.get(eb).setMarketData(marketData);
					newMap.put(eb, marketData);
				}
				LiveDataFetcher.handleEvent(newMap);
				if (!comp.isDisposed())
					comp.getDisplay().timerExec(5000, this);
			}
		});		
	}
}
