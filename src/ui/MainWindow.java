package src.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import src.Main;

public class MainWindow {
  
  private Shell shell;
  
  private SashForm sashForm;
  
  private DisplayPanel dp;
  private NavigationPanel np;

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
  
  public void addNewTab() {
	dp.addTab("New Tab");
  }
  
  public Shell getShell(){
    return this.shell;
  }
  
}
