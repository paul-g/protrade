package org.ic.tennistrader.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.score.StatisticsPanel;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.chart.DualChartWidget;
import org.ic.tennistrader.ui.dashboard.WidgetPlaceholder;
import org.ic.tennistrader.ui.richlist.RichListElement;
import org.ic.tennistrader.ui.richlist.RichListView;
import org.ic.tennistrader.ui.score.WimbledonScorePanel;
import org.ic.tennistrader.ui.updatable.MatchDataView;
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

		makeMarketGridEl(r);
		makeStatisticsPanelEl(r);
		makeMatchDataViewEl(r);
		makeScorePanelEl(r);
	}

	private void makeMatchDataViewEl(RichListView r) {
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = getCurrentMatch();
				Composite composite;
				if (match != null)
					composite = new MatchDataView(
							widgetPlaceholder.getParent(), SWT.NONE, match);
				else
					composite = new MatchDataView(
							widgetPlaceholder.getParent(), SWT.NONE);
				setSelection(composite);
			}
		});
		RichListElement element = new RichListElement(
				r,
				SWT.BORDER,
				"Displays match summary which includes the name of the tournament and the status of the match.",
				"Match Viewer", control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("StatisticsPanel INFO");
			}
		});
	}

	private void makeStatisticsPanelEl(RichListView r) {
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = getCurrentMatch();
				Composite widget;
				if (match != null)
					widget = new StatisticsPanel(widgetPlaceholder.getParent(),
							match);
				else
					widget = new StatisticsPanel(widgetPlaceholder.getParent());
				setSelection(widget);
			}
		});
		RichListElement element = new RichListElement(
				r,
				SWT.BORDER,
				"Displays detailed player statistics which include basic player information and historical match/set statistics "
						+ "obtained from www.tennisinsight.com. "
						+ "The match/set statistics represents the percentage of the matches/sets/games/points won in the last period  ",
				"Statistics Panel", control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("StatisticsPanel INFO");
			}
		});
	}

	private void makeMarketGridEl(RichListView r) {
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = getCurrentMatch();

				UpdatableMarketDataGrid grid;

				if (match != null) {
					grid = new UpdatableMarketDataGrid(widgetPlaceholder
							.getParent(), SWT.NONE, match);
					DataManager.registerForMatchUpdate(grid, match);
				} else {
					grid = new UpdatableMarketDataGrid(widgetPlaceholder
							.getParent(), SWT.NONE);
				}
				// NOTE for market data grid, controller is also required
				setSelection(grid);
			}
		});
		RichListElement element = new RichListElement(
				r,
				SWT.BORDER,
				"Displays the current best 3 availalbe odds with the amount available at those odds. "
						+ "The widget also displays the latest market information such "
						+ "as the Last Price Matched, the Total Amount Matched and "
						+ "the Market Overround which gives a measure of the competitiveness of the prices on offer. "
						+ "The Market Grid handels bet placing. "
						+ "To bet, you just need to click your preffered odds. The data is fetched from Betfair server at "
						+ "a speed of 5 requests/second", "Market Grid.",
				control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("MarketGrid INFO");
			}
		});
	}

	private void makeScorePanelEl(RichListView r) {
		Image image = new Image(Display.getCurrent(),
				"images/score-panel-small.png");
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = getCurrentMatch();
				Composite widget;
				if (match != null) {
					widget = new WimbledonScorePanel(widgetPlaceholder, match);
				} else {
					widget = new WimbledonScorePanel(widgetPlaceholder);
				}
				setSelection(widget);
			}
		});
		RichListElement element = new RichListElement(
				r,
				SWT.BORDER,
				"A panel with the current match score. The widget also includes the inferred "
						+ "probabilities of both players to win the game/set/match."
						+ "The inferred probabilities are calculated using the Markov chain models that use as "
						+ "input current match score and player's serve statistics. "
						+ "The model used is described in a number of academic papers focusing on tennis modelling.",
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
				Match match = getCurrentMatch();
				Composite widget;
				if (match != null)
					widget = new DualChartWidget(widgetPlaceholder, match);
				else
					widget = new DualChartWidget(widgetPlaceholder);
				setSelection(widget);
			}
		});
		RichListElement element = new RichListElement(
				r,
				SWT.BORDER,
				"This widget is composed of two charts. The top chart displays the Back/Lay odds as well as a few technical "
						+ "indicators such as Moving Average and Predicted Odds. "
						+ "The Moving Average is calculated as the unweighted mean of the previous 10 odds. "
						+ "The Predicted Odds are calculated using the Markov Chain model that use as "
						+ "input current match score and player's serve statistics. "
						+ "The model used is described in a number of academic papers focusing on tennis modelling. "
						+ "The bottom chart displays the volume of the matched bets over time.",
				"Dual Chart Widget", image, control);
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

	private Match getCurrentMatch() {
		return widgetPlaceholder.getDashboard().getMatch();
	}
}
