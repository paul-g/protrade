package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.browser.WindowEvent;
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


    sashForm.setFocus();

    this.np = new NavigationPanel(sashForm);
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
    if (!np.isTabPresent("Player Statistics")) np.addTab("Player Statistics");
    CTabItem item = np.getTab("Player Statistics");
    final Browser browser;
    try {
      browser = new Browser(np.getFolder(), SWT.NONE);
    } catch (SWTError e) {
      log.error("Could not instantiate Browser: " + e.getMessage());
      return;
    }
    browser.setUrl("http://www.atpworldtour.com/Rankings/Singles.aspx");

    item.setControl(browser);
  }

  public void addNewTab() {
    dp.addTab("New Tab");
  }

  public Shell getShell(){
    return this.shell;
  }

  static void initialize(final Display display, Browser browser) {
    browser.addOpenWindowListener(new OpenWindowListener() {
      public void open(WindowEvent event) {
        Shell shell = new Shell(display);
        shell.setText("New Window");
        shell.setLayout(new FillLayout());
        Browser browser = new Browser(shell, SWT.NONE);
        initialize(display, browser);
        event.browser = browser;
      }
    });
    browser.addVisibilityWindowListener(new VisibilityWindowListener() {
      public void hide(WindowEvent event) {
        Browser browser = (Browser)event.widget;
        Shell shell = browser.getShell();
        shell.setVisible(false);
      }
      public void show(WindowEvent event) {
        Browser browser = (Browser)event.widget;
        final Shell shell = browser.getShell();
        /* popup blocker - ignore windows with no style */
        boolean isOSX = SWT.getPlatform().equals ("cocoa") || SWT.getPlatform().equals ("carbon");
        if (!event.addressBar && !event.statusBar && !event.toolBar && (!event.menuBar || isOSX)) {
          System.out.println("Popup blocked.");
          event.display.asyncExec(new Runnable() {
            public void run() {
              shell.close();
            }
          });
          return;
        }
        if (event.location != null) shell.setLocation(event.location);
        if (event.size != null) {
          Point size = event.size;
          shell.setSize(shell.computeSize(size.x, size.y));
        }
        shell.open();
      }
    });
    browser.addCloseWindowListener(new CloseWindowListener() {
      public void close(WindowEvent event) {
        Browser browser = (Browser)event.widget;
        Shell shell = browser.getShell();
        shell.close();
      }
    });
  }

}