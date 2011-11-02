package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.TreeItem;

import src.domain.match.Match;
import src.domain.match.RealMatch;
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
        
        String name = ti.getText();
        
        // if it's not a match, don't display
        if ( !MatchUtils.isMatch(name) )
            return;
        
        Match match = NavigationPanel.getMatch(name);
        
        addMatchView(match);
    }

    public void addMatchView(Match match) {
        CTabItem[] items = folder.getItems();
        int pos = -1;

        String matchName = match.toString();
        
        for (int i = 0; pos == -1 && i < items.length; i++)
            if (items[i].getText().equals(matchName)) {
                pos = i;
            }

        // check new tab has been open
        if (pos == -1) {
            CTabItem item = new CTabItem(folder, SWT.CLOSE);
            item.setText(matchName);
            folder.setSelection(item);

            SashForm comp = new SashForm(folder, SWT.VERTICAL);

            addMatchData(comp, matchName);
            
            SashForm horizontal = new SashForm(comp, SWT.HORIZONTAL);
            horizontal.setLayout(new FillLayout());
            addMarketDataGrid(horizontal, match);

            if (match.isInPlay())
                addPredictionGui(horizontal, matchName);
            else {
                Label score = new Label(horizontal, SWT.BORDER);
                score.setText("Match is not in progress - No score available");
            }
            
            addChart(comp, match);

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
    private void addMatchData(Composite comp, String matchName){
        Composite composite = new Composite(comp, SWT.BORDER);
        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.HORIZONTAL;
        composite.setLayout(rowLayout);
        Label name = new Label(composite, SWT.BORDER);
        name.setText("Match : " + matchName);
        Label status = new Label(composite, SWT.BORDER);
        status.setText("Status: " + " IN PROGRESS");
    }

    /**
     * Adds the back and lay chart
     * 
     * @param comp
     * @param ti
     */
    private void addChart(Composite comp, Match match) {
    	Button changeAxisButton = new Button(comp, SWT.LEFT);
    	changeAxisButton.setText("Switch odds representation");
    	   	
        final UpdatableChart chart = new UpdatableChart(comp, SWT.BORDER);
        chart.getTitle().setText(match.getName());
        GridData chartData = new GridData();
        chartData.horizontalSpan = 2;
        
        changeAxisButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				chart.invertAxis();
			}    		
    	});
        
      match.registerForUpdate(chart, comp);
        comp.update();
    }

    /**
     * Adds the market data grid with back and lay values and amounts
     * 
     * @param comp
     * @param ti
     */
    private void addMarketDataGrid(Composite comp, Match match) {
    	UpdatableMarketDataGrid table = new UpdatableMarketDataGrid(comp);
    	match.registerForUpdate(table, comp);
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
