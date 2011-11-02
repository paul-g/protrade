package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import src.Main;
import src.domain.MOddsMarketData;
import src.model.connection.BetfairExchangeHandler;
import src.score.PredictionGui;
import src.service.LiveDataFetcher;
import src.ui.updatable.UpdatableChart;
import src.ui.updatable.UpdatableMarketDataGrid;
import src.utils.MatchUtils;

public class DisplayPanel implements Listener {

	private final CTabFolder folder;
	private Display display;
	private CTabItem selected;

	private static Logger log = Logger.getLogger(DisplayPanel.class);

	public DisplayPanel(Composite parent) {
		display = parent.getDisplay();
		folder = new CTabFolder(parent, SWT.RESIZE | SWT.BORDER);
		folder.setSimple(false);
		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		folder.setLayoutData(gridData);
		setOnClickMenu();
	}

	public void addTab(String text) {
		CTabItem cti = new CTabItem(folder, SWT.CLOSE);
		cti.setText(text);
		folder.setSelection(cti);
	}

	private void addPredictionGui(Composite composite, String title) {
		new PredictionGui(composite, title);
	}

	public void handleEvent(Event event) {
		TreeItem ti = (TreeItem) event.item;

		// if it's not a match, don't display
		if ( !MatchUtils.isMatch(ti.getText()) )
			return;

		CTabItem[] items = folder.getItems();
		int pos = -1;

		for (int i = 0; pos == -1 && i < items.length; i++)
			if (items[i].getText().equals(ti.getText())) {
				pos = i;
			}

		// check new tab has been open
		if (pos == -1) {
			CTabItem item = new CTabItem(folder, SWT.CLOSE);
			item.setText(ti.getText());
			folder.setSelection(item);

			// Composite comp = new Composite(folder, SWT.NONE);
			SashForm comp = new SashForm(folder, SWT.VERTICAL);

			addMatchData(comp, ti);

			SashForm horizontal = new SashForm(comp, SWT.HORIZONTAL);
			horizontal.setLayout(new FillLayout());
			addMarketDataGrid(horizontal, ti);

			if (MatchUtils.inPlay(ti))
				addPredictionGui(horizontal, ti.getText());
			else {
				Label score = new Label(horizontal, SWT.BORDER);
				score.setText("Match is not in progress - No score available");
			}


			addChart(comp, ti);

			item.setControl(comp);

		} else
			// just bring the required tab under focus
			folder.setSelection(pos);
	}

	/**
	 * Adds an area for displaying match data
	 * @param comp
	 * @param ti
	 */
	private void addMatchData(Composite comp, TreeItem ti){
		Composite composite = new Composite(comp, SWT.BORDER);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.HORIZONTAL;
		composite.setLayout(rowLayout);
		Label name = new Label(composite, SWT.BORDER);
		name.setText("Match : " + ti.getText());
		Label status = new Label(composite, SWT.BORDER);
		status.setText("Status: " + " IN PROGRESS");
	}

	/**
	 * Adds the back and lay chart
	 * 
	 * @param comp
	 * @param ti
	 */
	private void addChart(Composite comp, TreeItem ti) {
		Button changeAxisButton = new Button(comp, SWT.LEFT);
		changeAxisButton.setText("Switch odds representation");

		final UpdatableChart chart = new UpdatableChart(comp, SWT.BORDER);
		chart.getTitle().setText(ti.getText());
		GridData chartData = new GridData();
		chartData.horizontalSpan = 2;

		changeAxisButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				chart.invertAxis();
			}    		
    	});
        
        //Logger log = Logger.getLogger(DisplayPanel.class);
        //log.info("created chart, now got to register");
        LiveDataFetcher.registerLive(chart, NavigationPanel.getMatch(ti), comp);
        //log.info("Chart registered successfully!");
        comp.update();
    }

	/**
	 * Adds the market data grid with back and lay values and amounts
	 * 
	 * @param comp
	 * @param ti
	 */
	private void addMarketDataGrid(Composite comp, TreeItem ti) {
		//Table table = new Table(comp, SWT.BORDER);
		UpdatableMarketDataGrid table = new UpdatableMarketDataGrid(comp,ti);
		LiveDataFetcher.registerLive(table, NavigationPanel.getMatch(ti), comp);
	}

	private void setOnClickMenu() {
		Menu popup = new Menu(folder);
		MenuItem openItem = new MenuItem(popup, SWT.NONE);
		openItem.setText("Open in a new window");
		openItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selected != null) {
					Shell shell = new Shell(display);
					CTabItem ni = selected;
					shell.setText(ni.getText());
//					shell.setMaximized(true);
					SashForm sashForm = (SashForm) ni.getControl();
					sashForm.setFocus();
					sashForm.setParent(shell);
					sashForm.pack();
					shell.open();
				}
			}
		});
		MenuItem closeItem = new MenuItem(popup, SWT.NONE);
		closeItem.setText("Close");
		MenuItem closeAll = new MenuItem(popup, SWT.NONE);
		closeAll.setText("Close All");
		folder.addListener(SWT.MenuDetect, new RightClickListener(popup));
	}

	private class RightClickListener implements Listener {
		private Menu menu;

		public RightClickListener(Menu menu) {
			this.menu = menu;
		}

		@Override
		public void handleEvent(Event event) {
			Point click = new Point(event.x, event.y);
			Point point = folder.getDisplay().map(null, folder, click);
			CTabItem item = folder.getItem(point);
			if (item != null) {
				selected = item;
				menu.setLocation(click);
				menu.setVisible(true);
			}
		}
	}
}
