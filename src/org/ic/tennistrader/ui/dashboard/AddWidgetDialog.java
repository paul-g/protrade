package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.score.StatisticsPanel;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.chart.DualChartWidget;
import org.ic.tennistrader.ui.richlist.RichListView;
import org.ic.tennistrader.ui.richlist.RichListElement;
import org.ic.tennistrader.ui.score.WimbledonScorePanel;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;

public class AddWidgetDialog extends RichListDialog {

	private WidgetPlaceholder widgetPlaceholder;

	private Composite selection;

	public AddWidgetDialog() {
		setText("Add Widget");
	}
	
	public void setWidgetPlaceholder(WidgetPlaceholder widgetPlaceholder) {
		this.widgetPlaceholder = widgetPlaceholder;
	}

	public Composite getSelection() {
		return selection;
	}

	@Override
	protected void addElements(RichListView r) {
		makeDualChartElement(r);
		makeScorePanelEl(r);
		makeMarketGridEl(r);
		makeStatisticsPanelEl(r);
	}

	private void makeStatisticsPanelEl(RichListView r) {
		Image image = new Image(Display.getCurrent(), "images/chart.png");
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
				setSelection(new StatisticsPanel(widgetPlaceholder.getParent(),
						match));
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Displays match statistics", "Statistics Panel", image, control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("StatisticsPanel INFO");
			}
		});
	}

	private void makeMarketGridEl(RichListView r) {
		Image image = new Image(Display.getCurrent(), "images/chart.png");
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
				// NOTE for market data grid, controller is also required
				UpdatableMarketDataGrid grid = new UpdatableMarketDataGrid(
						widgetPlaceholder.getParent(), SWT.NONE, match);
				DataManager.registerForMatchUpdate(grid, match);
				setSelection(grid);
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Displays market info and handles bet placing",
				"Market Grid", image, control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("MarketGrid INFO");
			}
		});
	}

	private void makeScorePanelEl(RichListView r) {
		Image image = new Image(Display.getCurrent(), "images/score-panel-small.png");
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
				setSelection(new WimbledonScorePanel(widgetPlaceholder, match));
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"A panel with match scores and inferred probabilities",
				"Score Panel", image, control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("ScorePanel INFO");
			}
		});
	}

	private void makeDualChartElement(RichListView r) {
		Image image = new Image(Display.getCurrent(), "images/chart-small.png");
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
				setSelection(new DualChartWidget(widgetPlaceholder, match));
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Dual Chart Widget description", "Dual Chart Widget", image,
				control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Chart INFO");
			}
		});
	}

	private void setSelection(Composite selection) {
		this.selection = selection;
		dialog.dispose();
	}
}
