package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import src.Main;
import src.exceptions.LoginFailedException;
import src.service.BetfairConnectionHandler;

public class LoginShell{
  
  private Shell loginShell;
  
  private Label result;
  
  private static Logger log = Logger.getLogger(LoginShell.class);
  
  private static final String TITLE = "Login to tennis trader"; 

  public LoginShell(final Display display){
    this.loginShell = new Shell(display, SWT.MAX);
    loginShell.setText(TITLE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 5;
    loginShell.setLayout(gridLayout);

    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.horizontalSpan = 4;

    Label loginLabel = new Label(loginShell, SWT.NONE);
    loginLabel.setText("Username: ");

    final Text username = new Text(loginShell, SWT.NONE);
    username.setLayoutData(gridData);

    Label passLabel = new Label(loginShell, SWT.NONE);
    passLabel.setText("Password: ");

    final Text password = new Text(loginShell, SWT.PASSWORD);
    password.setLayoutData(gridData);

    // just for alignment
    @SuppressWarnings("unused")
    Label blankLabel = new Label(loginShell, SWT.NONE);


    Button login = new Button(loginShell, SWT.PUSH);
    GridData buttonData = new GridData();
    login.setText("Login");
    login.setLayoutData(buttonData);

    Button reset = new Button(loginShell, SWT.NONE);
    reset.setText("Reset");
    reset.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event arg0) {
        username.setText("");
        password.setText("");
      }     
    });

    // TODO: for testing only
    Button bypass = new Button(loginShell, SWT.NONE);
    bypass.setText("Bypass");
    
    bypass.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
          loginShell.dispose();
          launchApp(display);         
      }
    });
    
    Button testAccount = new Button(loginShell, SWT.NONE);
    testAccount.setText("Use Test Account");
    
    GridData resultData = new GridData();
    resultData.horizontalSpan = 5; 
    this.result = new Label(loginShell, SWT.NONE);
    result.setLayoutData(resultData);
    result.setText(FAIL.length() > SUCCESS.length() ? FAIL : SUCCESS);
    result.setVisible(false);
    
    testAccount.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event arg0) {
          Main.USER = "corina409";
          updateResult(SUCCESS);
          loginShell.dispose();
          launchApp(display);
      }
      
    });

    login.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String user = username.getText();
        if (checkLogin(user, password.getText())) {
          Main.USER = user; 
          updateResult(SUCCESS);
          loginShell.dispose();
          launchApp(display);
        } else
          updateResult(FAIL);
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
  
  private static final String SUCCESS = "Login successful! Please wait...";
  
  private static final String FAIL    = "Login failed! Please try again...";
  
  private static boolean checkLogin(String username, String password) {
    // Perform the login
    try {
      BetfairConnectionHandler.login(username, password);
    } catch (LoginFailedException e) {
      log.info(e.getMessage());
      return false;
    }
    
    log.info("Login succeeded with token - "
        + BetfairConnectionHandler.getApiContext().getToken());

    // get list of tournaments (events and markets of tennis type event id)
    /*
    //List<Tournament> tournaments = BetfairConnectionHandler.getTournamentsData();
    List<EventMarketBetfair> tournaments = BetfairConnectionHandler.getTournamentsData();
    log.info("List of events under \' tennis event type id \': ");
    
    for (EventMarketBetfair emb : tournaments) {
      printEvents(0, emb);
    }
    */

    // Perform logout
    /*
     * try { 
     *  BetfairConnectionHandler.logout(); 
     * } catch (Exception e){
     *  log.info(e.getMessage()); System.exit(-1); 
     * }
     * log.info("Logout succesful");
     */

    return true;
  }
  
  private void updateResult(String message){
    result.setText(message);
    result.setVisible(true);
    result.update();
    this.loginShell.update();
    if (message.equals(SUCCESS))
        try {
          Thread.sleep(1000);
        } catch (Exception e){}

  }
  
  private static void launchApp(Display display) {
    new MainWindow(display);
  }
  
}
