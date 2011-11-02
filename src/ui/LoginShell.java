package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import src.Main;
import src.exceptions.LoginFailedException;
import src.model.connection.BetfairConnectionHandler;

public class LoginShell{
  
  private Shell loginShell;
  
  private Label result;
  
  private static Logger log = Logger.getLogger(LoginShell.class);
  
  private static final String TITLE = "Login to tennis trader"; 

  public LoginShell(final Display display){
    this.loginShell = new Shell(display, SWT.MAX/*SWT.NO_TRIM|SWT.ON_TOP */ );//SWT.TRANSPARENCY_ALPHA);
    loginShell.setSize(614,531);
    
    final Image logoUp = new Image (loginShell.getDisplay(), "images/logo_modif.jpg");
    loginShell.setBackgroundImage(logoUp);
    
    Rectangle rect = loginShell.getClientArea();
    
    loginShell.setText(TITLE);
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 5;
    gridLayout.marginRight = 200;
    gridLayout.marginLeft = 200;
    gridLayout.marginTop = 300;
    gridLayout.marginBottom = 5;
    //gridLayout.
    
    loginShell.setLayout(gridLayout);

    GridData gridData = new GridData(150,30);
    gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
    gridData.horizontalSpan = 10;
    
    GridData gridd = new GridData(GridData.FILL_BOTH);
    //gridd.horizontalAlignment = GridData.;
    //gridd.horizontalSpan = 10;
    gridd.minimumHeight = 500;
    gridd.minimumWidth = 600;
    
    
    
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
    
    Button rememberMe = new Button(loginShell, SWT.CHECK);
    GridData rmData = new GridData();
    rmData.horizontalSpan = 4; 
    rememberMe.setText("Remember Me");
    rememberMe.setLayoutData(rmData);
    rememberMe.addListener(SWT.Selection, new Listener() {
      
      @Override
      public void handleEvent(Event arg0) {
       log.info("Remember Me selected"); 
        
      }
    });
    
    @SuppressWarnings("unused")
    Label blankLabel2 = new Label(loginShell, SWT.NONE); 

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

    Button testAccount = new Button(loginShell, SWT.NONE);
    testAccount.setText("Use Test Account");
    testAccount.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event arg0) {
        String username = Main.USERNAME;
        String password = Main.PASSWORD;
        log.info("username " + username);
       if (checkLogin(username, password )) {
          updateResult(SUCCESS);
          loginShell.dispose();
          launchApp(display);
        } else
          updateResult(FAIL);
      }
    });
    
    GridData resultData = new GridData();
    resultData.horizontalSpan = 5; 
    this.result = new Label(loginShell, SWT.NONE);
    result.setLayoutData(resultData);
    result.setText(FAIL.length() > SUCCESS.length() ? FAIL : SUCCESS);
    result.setVisible(false);
    


    login.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        String user = username.getText();
        if (checkLogin(user, password.getText())) {
          Main.USERNAME = user; 
          updateResult(SUCCESS);
          loginShell.dispose();
          launchApp(display);
        } else
          updateResult(FAIL);
      }
    });    
    
    //  Center the login screen
    Monitor primary = display.getPrimaryMonitor ();
    Rectangle bounds = primary.getBounds();
    
    int x = bounds.x + (bounds.width - rect.width)/2;
    int y = bounds.y + (bounds.height - rect.height)/2;
    loginShell.setLocation (x, y);
    
    
    
    
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
