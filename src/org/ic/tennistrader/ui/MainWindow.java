package org.ic.tennistrader.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.ic.tennistrader.Main;

public class MainWindow {

    private Shell shell;

    // the sash forms
    private SashForm sashForm;
    private SashForm sashFormLeft;
    private SashForm sashFormRight;

    private DisplayPanel dp;

    private NavigationPanel np;

    private static Logger log = Logger.getLogger(MainWindow.class);
    
    private final int BAR_INCREMENT = 5;

    public MainWindow(Display display, LoginShell loginShell) {
        loginShell.updateProgressBar(BAR_INCREMENT);
        loginShell.setText("Login successful! Starting application...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
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
        loginShell.setText("Fetching betfair data");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.dp = new DisplayPanel(sashFormRight);
        loginShell.updateProgressBar(BAR_INCREMENT);
        loginShell.setText("Preparing display");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        np.addListener(dp);
        
        @SuppressWarnings("unused")
        ToolBarPanel bottomPanel = new ToolBarPanel(this, false);
        
        loginShell.updateProgressBar(BAR_INCREMENT);
        loginShell.setText("Configuring tooblars");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        loginShell.finishProgressBar();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loginShell.dispose();
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        
        display.dispose();
    }

    public void addActiveBetsDisplay(){
        addActiveBetsDisplay(sashFormLeft);
    }
    
    private void addActiveBetsDisplay(Composite composite) {
        CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
        CTabItem cti = new CTabItem(tabFolder, SWT.CLOSE);
        cti.setText("Active Bets");
        tabFolder.setSimple(false);
        tabFolder.setMinimizeVisible(true);
        tabFolder.setMaximizeVisible(true);
        Button button = new Button(tabFolder, SWT.NONE);
        button.setText("Active Bets Data Not Available...");
        cti.setControl(button);
        composite.layout();
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
}
