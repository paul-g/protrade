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
import org.ic.tennistrader.ui.widgets.browser.BrowserWidget;
import org.ic.tennistrader.ui.widgets.video.MatchPlayer;

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
		makeBrowserEl(r);
		makeVideoEl(r);
	}

	private void makeVideoEl(RichListView r) {
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = getCurrentMatch();
				MatchPlayer player = new MatchPlayer(
						widgetPlaceholder.getParent(), SWT.NONE);
				if (match != null)
					player.setMatch(match);
				setSelection(player);
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Watch the match", "Match Player", control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Match Player INFO");
			}
		});
	}

	private void makeBrowserEl(RichListView r) {
		Control control = makeElementControl(new Listener() {
			@Override
			public void handleEvent(Event e) {
				Match match = getCurrentMatch();
				BrowserWidget browser = new BrowserWidget(
						widgetPlaceholder.getParent(), SWT.NONE);
				if (match != null)
					browser.setMatch(match);
				setSelection(browser);
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Browse online for statistics", "Browser", control);
		element.addInfoListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Browser INFO");
			}
		});
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
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Displays match summary", "Match Viewer", control);
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
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Displays match statistics", "Statistics Panel", control);
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
					grid = new UpdatableMarketDataGrid(
							widgetPlaceholder.getParent(), SWT.NONE, match);
					DataManager.registerForMatchUpdate(grid, match);
				} else {
					grid = new UpdatableMarketDataGrid(
							widgetPlaceholder.getParent(), SWT.NONE);
				}
				// NOTE for market data grid, controller is also required
				setSelection(grid);
			}
		});
		RichListElement element = new RichListElement(r, SWT.BORDER,
				"Displays market info and handles bet placing", "Market Grid",
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
				Match match = getCurrentMatch();
				Composite widget;
				if (match != null)
					widget = new DualChartWidget(widgetPlaceholder, match);
				else
					widget = new DualChartWidget(widgetPlaceholder);
				setSelection(widget);
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

	private Match getCurrentMatch() {
		return widgetPlaceholder.getDashboard().getMatch();
	}
}
