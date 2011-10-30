package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import src.Main;

public class MainWindow {

  private Shell shell;

  private SashForm sashForm;
  
  
  private SashForm sashFormLeft;

  private DisplayPanel dp;

  private NavigationPanel np;

  private static Logger log = Logger.getLogger(MainWindow.class);

  public MainWindow(Display display){
    this.shell = new Shell(display);
    shell.setMaximized(true);

    GridLayout layout = new GridLayout();
    layout.horizontalSpacing = 10;
    layout.verticalSpacing   = 10;
    layout.numColumns = 3;
    layout.makeColumnsEqualWidth = true;
    //layout.justify = true;

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
    
    sashForm.setFocus();

    this.np = new NavigationPanel(sashFormLeft);
    this.dp = new DisplayPanel(sashForm);

    np.addListener(dp);

    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }

    display.dispose();
  }

  public void addActiveBetsDisplay(){
    if (!np.isTabPresent("Active Bets Display")) np.addTab("Active Bets Display");
  }

  public void addMatchNavigator(){
    if (!np.isTabPresent("Match Navigator")) np.addTab("Match Navigator");
  }

  public void addPlayerStatistics(){
   
    CTabFolder tabFolder = new CTabFolder(sashFormLeft, SWT.BORDER);
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
    sashFormLeft.layout();
  }

  public void addNewTab(String text) {
    dp.addTab(text);
  }

  public Shell getShell(){
    return this.shell;
  }

  public void dispose() {
	  shell.dispose();
  }
}
