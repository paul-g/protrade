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
import org.eclipse.swt.layout.RowLayout;
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
import src.score.PredictionGui;
import src.ui.updatable.UpdatableChart;
import src.ui.updatable.UpdatableMarketDataGrid;
import src.utils.MatchUtils;

public class DisplayPanel implements Listener {

    private final CTabFolder folder;
    private Display display;
    private CTabItem selected;

    private static Logger log = Logger.getLogger(DisplayPanel.class);

    public DisplayPanel(Composite parent) {
        folder = new CTabFolder(parent, SWT.RESIZE | SWT.BORDER);
        folder.setSimple(false);
        folder.setMinimizeVisible(true);
        folder.setMaximizeVisible(true);
        
        /**************/
        // folder.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));

        folder.setLayoutData(makeLayoutData());

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
        if (!MatchUtils.isMatch(name))
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

            Composite control = new Composite(folder, SWT.NONE);
            control.setLayout(new FillLayout());
            
            SashForm comp = new SashForm(control, SWT.VERTICAL);
            
            addMatchData(comp, match);

            SashForm horizontal = new SashForm(comp, SWT.HORIZONTAL);
            
            addMarketDataGrid(horizontal, match);

            if (match.isInPlay())
                addPredictionGui(horizontal, matchName);
            else {
                Label score = new Label(horizontal, SWT.BORDER);
                score.setText("Match is not in progress - No score available");
            }

            addChart(comp, match);

            item.setControl(control);
            
            folder.setSelection(item);

            horizontal.setWeights(new int[]{60, 40});
            comp.setWeights(new int[]{10,30, 60});
        } else
            // just bring the required tab under focus
            folder.setSelection(pos);
    }

    /**
     * Adds an area for displaying match data
     * 
     * @param comp
     * @param ti
     */
    private void addMatchData(Composite comp, Match match) {
        Composite composite = new Composite(comp, SWT.BORDER);
        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        composite.setLayout(rowLayout);
        
        Label name = new Label(composite, SWT.BORDER);
        name.setText("Match : " + match.getName());
        
        Label status = new Label(composite, SWT.BORDER);
        String st = (match.isInPlay()? "In Progress" : "Not In Progress");
        status.setText("Status: " + st);
        
        composite.pack();
    }

    /**
     * Adds the back and lay chart
     * 
     * @param comp
     * @param ti
     */
    private void addChart(Composite comp, Match match) {
        // Select values on chart
        Composite c = new Composite(comp, SWT.NONE);
        c.setLayout(new FillLayout());
        final UpdatableChart chart = new UpdatableChart(c, SWT.BORDER, match);
         
        match.registerForUpdate(chart, c);
        
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
                    // create the new shell
                    Shell shell = new Shell(display, SWT.RESIZE);
                    shell.setLayout(new FillLayout());
                    
                    // get selected tab
                    CTabItem ni = selected;
                    
                    shell.setText(ni.getText());
                    Composite comp = (Composite) ni.getControl();
                    SashForm  sashForm = (SashForm)comp.getChildren()[0];
                    sashForm.setFocus();
                    sashForm.setParent(shell);
                    sashForm.pack();

                    //ni.setControl(new Composite(folder, SWT.NONE));
                    ni.dispose();
                    shell.pack();
                    shell.open();
                }
            }
        });
        
        // close option
        MenuItem closeItem = new MenuItem(popup, SWT.NONE);
        closeItem.setText("Close");
        closeItem.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                if (selected != null)
                   selected.dispose();
            }
        });
        
        // close all option
        MenuItem closeAll = new MenuItem(popup, SWT.NONE);
        closeAll.setText("Close All");
        closeAll.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                CTabItem[] items = folder.getItems();
                for (int i = 0; i< items.length; i++)
                    items[i].dispose();
            }
        });
        
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

    private GridData makeLayoutData() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        return gridData;
    }
}
