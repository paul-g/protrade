package src;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import src.ui.DisplayPanel;
import src.ui.NavigationPanel;

public class Main {

  private static final String TITLE = "Tennis Trader";

  private static final Display display = new Display();

  public static void main(String[] args) {

    createLoginShell();

  }

  private static void createLoginShell() {
    

    Shell loginShell = new Shell(display, SWT.MAX);
    
    /*Monitor primary = display.getPrimaryMonitor();
    Rectangle bounds = primary.getBounds();
    Rectangle rect = loginShell.getBounds();
    
    int x = bounds.x + (bounds.width - rect.width) / 2;
    int y = bounds.y + (bounds.height - rect.height) / 2;
    
    loginShell.setLocation(x,y);
    */

    loginShell.setText("Login to tennis trader");

    GridLayout gridLayout = new GridLayout();
    gridLayout.makeColumnsEqualWidth = true;
    gridLayout.numColumns = 3;

    loginShell.setLayout(gridLayout);

    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.horizontalSpan = 2;

    Label loginLabel = new Label(loginShell, SWT.NONE);
    loginLabel.setText("Username: ");

    Text username = new Text(loginShell, SWT.NONE);
    username.setLayoutData(gridData);

    Label passLabel = new Label(loginShell, SWT.NONE);
    passLabel.setText("Password: ");

    Text password = new Text(loginShell, SWT.PASSWORD);
    password.setLayoutData(gridData);

    // just for alignment
    @SuppressWarnings("unused")
    Label blankLabel = new Label(loginShell, SWT.NONE);

    Button button = new Button(loginShell, SWT.PUSH);
    GridData buttonData = new GridData();
    button.setText("Login");
    button.setLayoutData(buttonData);

    Button reset = new Button(loginShell, SWT.None);
    reset.setText("Reset");

    button.addListener(SWT.Selection, new Listener() {
      @Override
      public void handleEvent(Event event) {
        // TODO Auto-generated method stub
        if (checkLogin())
          launchApp(display);
      }
    });

    loginShell.pack();

    loginShell.open();

    while (!loginShell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }

    display.dispose();

  }
  
 
  private static void launchApp(Display display) {
    Shell shell = new Shell(display);
    shell.setMaximized(true);
    shell.setLayout(new FillLayout());
    shell.setText(TITLE);

    NavigationPanel np = new NavigationPanel(shell);
    DisplayPanel dp = new DisplayPanel(shell);

    np.addListener(dp);

    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }

    display.dispose();
  }
  
  private static boolean checkLogin(){
    // TODO: impement logic for checking login here
    return true;
  }
  
}