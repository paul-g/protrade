package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.ic.tennistrader.Main;
import org.ic.tennistrader.service.LiveDataFetcher;

public class MainWindow {

    private Shell shell;
    
    private List<Listener> loadListeners = new ArrayList<Listener>();

    // the sash forms
    private static SashForm sashForm;
    private SashForm sashFormLeft;
    private SashForm sashFormRight;
    private Display display;
    
    private static DisplayPanel dp;

    private NavigationPanel np;

    private static Logger log = Logger.getLogger(MainWindow.class);
    
    private final int BAR_INCREMENT = 5;
    
    public Shell show(){
        show(display);
        shell.open();
        return shell;
    }
    
    public MainWindow(Display display) {
        this.display = display;
    }

    private void show(Display display) {
        notifyLoadEvent("Login successful! Starting application...");
        
        this.shell = new Shell(display);
        shell.setMaximized(true);
        
        /***********************/
        shell.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        GridLayout layout = new GridLayout();
        layout.horizontalSpacing = 10;
        layout.verticalSpacing = 10;
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = true;
        // layout.justify = true;

        shell.setLayout(layout);
        shell.setText(Main.TITLE);

        // Menu and Tool bar set-up
        @SuppressWarnings("unused")
        ToolBarPanel tp = new ToolBarPanel(this);

        @SuppressWarnings("unused")
        MenuPanel mp = new MenuPanel(this);

        // Sashform set-up
        this.sashForm = new SashForm(shell, SWT.HORIZONTAL);
        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.horizontalSpan = 3;
        layoutData.verticalAlignment = GridData.FILL;
        sashForm.setLayoutData(layoutData);
        sashForm.setLayout(layout);

        this.sashFormLeft = new SashForm(sashForm, SWT.VERTICAL);
        this.sashFormRight = new SashForm(sashForm, SWT.VERTICAL);

        
        sashForm.setFocus();
        sashForm.setWeights(new int[]{20,80});

        this.np = new NavigationPanel(sashFormLeft);
        notifyLoadEvent("Fetching betfair data");

        this.dp = new DisplayPanel(sashFormRight);
        
        notifyLoadEvent("Preparing display");
        np.addListener(dp);

        addActiveBetsDisplay(sashFormLeft);
        
        notifyLoadEvent("Configuring toolbars");
        @SuppressWarnings("unused")
        ToolBarPanel bottomPanel = new ToolBarPanel(this, false);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        notifyLoadEvent("Done!");
    }
    
    public void run(Display display) {
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        LiveDataFetcher.stopAllThreads();
        ToolBarPanel.setStop();
        //System.out.println("Application is closing...");
        display.dispose();
    }

    public void addActiveBetsDisplay(){
        addActiveBetsDisplay(sashFormLeft);
    }
    
    private void addActiveBetsDisplay(Composite composite) {
        new BetsDisplay(composite);
    }

    public void addMatchNavigator() {
        if (!np.isTabPresent("Match Navigator"))
            np.addTab("Match Navigator");
    }
    
    public void addPlayerStatistics(){
        addPlayerStatistics(sashFormRight);
    }
    
    private void addPlayerStatistics(Composite composite) {
        CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
        CTabItem cti = new CTabItem(tabFolder, SWT.CLOSE);
        tabFolder.setSimple(false);
        tabFolder.setMinimizeVisible(true);
        tabFolder.setMaximizeVisible(true);

        cti.setText("Player Statistics");
        final Browser browser;
        try {
            browser = new Browser(tabFolder, SWT.NONE);
        } catch (SWTError e) {
            log.error("Could not instantiate Browser: " + e.getMessage());
            return;
        }
        cti.setControl(browser);
        tabFolder.setSelection(cti);
        browser.setUrl("http://www.atpworldtour.com/Rankings/Singles.aspx");
        composite.layout();
    }

    public void addNewTab(String text) {
        dp.addTab(text);
    }

    public Shell getShell() {
        return this.shell;
    }

    public void dispose() {
        shell.dispose();
    }
    
    public DisplayPanel getDisplayPanel(){
        return dp;
    }
    
    public void addLoadListener(Listener listener){
        loadListeners.add(listener);
    }
    
    public void notifyLoadEvent(String name){
        for (Listener l: loadListeners){
            Event e = new Event();
            e.text = name;
            l.handleEvent(e);
        }
    }
    
    public static void toggleMaximizeMatchDisplay(){
        if( sashForm.getMaximizedControl() == dp.getControl() )
            sashForm.setMaximizedControl(null);
          else
            sashForm.setMaximizedControl(dp.getControl());
    }

   public void addMatchViewer() {
        dp.addMatchViewer();
    }
}
