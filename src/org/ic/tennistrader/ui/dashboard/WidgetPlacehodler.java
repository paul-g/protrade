package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.score.StatisticsPanel;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.chart.DualChartWidget;
import org.ic.tennistrader.ui.score.WimbledonScorePanel;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;

class WidgetPlacehodler extends Composite {

	private final Button b;
	private final WidgetContainer widgetContainer;
	
	WidgetPlacehodler(Composite parent, int style, WidgetContainer widgetContainer) {
		super(parent, style);
		this.widgetContainer = widgetContainer;
		setLayout(new FillLayout());
		b = new Button(this, SWT.PUSH);
		final WidgetContainer wc = widgetContainer;
		b.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				
				Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
				dispose();
				
				//TODO select widget to add based on event type
				wc.setWidget(new DualChartWidget(wc, match));
				//wc.setWidget(new WimbledonScorePanel(wc, match));
				
				// NOTE for market data grid, controller is also required				
				//UpdatableMarketDataGrid grid = new UpdatableMarketDataGrid(wc, SWT.NONE, match);
				//LiveDataFetcher.registerForMatchUpdate(grid, match);
				//wc.setWidget(grid);
				
				//wc.setWidget(new StatisticsPanel(wc, match));
			}
			
		});
	}
	
	

}
