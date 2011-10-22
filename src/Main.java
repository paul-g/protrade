package src;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import src.domain.*;
import src.exceptions.LoginFailedException;
import src.service.BetfairConnectionHandler;
import src.ui.DisplayPanel;
import src.ui.NavigationPanel;

public class Main {

	private static final String TITLE = "Tennis Trader";

	private static final Display display = new Display();
	
	private static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {

		createLoginShell();

	}

	private static void createLoginShell() {

		final Shell loginShell = new Shell(display, SWT.MAX);

		/*
		 * Monitor primary = display.getPrimaryMonitor(); Rectangle bounds =
		 * primary.getBounds(); Rectangle rect = loginShell.getBounds();
		 * 
		 * int x = bounds.x + (bounds.width - rect.width) / 2; int y = bounds.y
		 * + (bounds.height - rect.height) / 2;
		 * 
		 * loginShell.setLocation(x,y);
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

		final Text username = new Text(loginShell, SWT.NONE);
		username.setLayoutData(gridData);

		Label passLabel = new Label(loginShell, SWT.NONE);
		passLabel.setText("Password: ");

		final Text password = new Text(loginShell, SWT.PASSWORD);
		password.setLayoutData(gridData);

		// just for alignment
		@SuppressWarnings("unused")
		Label blankLabel = new Label(loginShell, SWT.NONE);

		Button button = new Button(loginShell, SWT.PUSH);
		GridData buttonData = new GridData();
		button.setText("Login");
		button.setLayoutData(buttonData);

		Button reset = new Button(loginShell, SWT.NONE);
		reset.setText("Reset");
		reset.addListener(SWT.Selection, new Listener() {
		  public void handleEvent(Event arg0) {
				username.setText("");
				password.setText("");
			}			
		});

		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (checkLogin(username.getText(), password.getText())) {
					loginShell.dispose();
					launchApp(display);					
				}
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
		
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 10;
		layout.verticalSpacing   = 10;
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;
		//layout.justify = true;
		
		shell.setLayout(layout);
		shell.setText(TITLE);
		
//		addToolBar(shell);
		addMenuBar(shell);
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
		 * 	BetfairConnectionHandler.logout(); 
		 * } catch (Exception e){
		 * 	log.info(e.getMessage()); System.exit(-1); 
		 * }
		 * log.info("Logout succesful");
		 */

		return true;
	}
	
	private static void addMenuBar(Shell shell) {
		
		// Menu bar of the application
		Menu menuBar = new Menu(shell, SWT.BAR);
	    MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");
	    shell.setMenuBar(menuBar);
	    
	}
	
	private static void printEvents(int level, EventMarketBetfair event) {
			String msg = "";
			for(int i = 0 ; i < level; i++)
				msg += "\t";
			msg += event.toString();
			log.info(msg);
			for (EventMarketBetfair e : event.getChildren()) {
				printEvents(level + 1, e);
			}
		
		/*
		for (Tournament t : tournaments) {
			log.info(t.toString());
			for (EventMarketBetfair m : t.getChildren()) {
				log.info("\t " + m.toString());
			}
		}
		*/
	}
	
	private static void addToolBar(Shell shell){

	  // Setting span throughout the columns
	  GridData gridData = new GridData();
	  gridData.horizontalSpan = ((GridLayout) shell.getLayout()).numColumns;

	  // Setting up the toolbar with the placeholders - due to the number of columns
	  final ToolBar toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
	  toolbar.setLayoutData(gridData);
	  
	  // Main menu
	  final ToolItem itemMainMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
	  itemMainMenu.setText("Main");
	  itemMainMenu.setToolTipText("Main program operations");
	  final Menu mainDropDownMenu = new Menu(shell,SWT.POP_UP);
	  new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Open");
	  new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Save");
	  new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Save As...");
	  new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Exit");	  
	  itemMainMenu.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        if(event.detail == SWT.ARROW) {
	          Rectangle bounds = itemMainMenu.getBounds();
	          Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
	          mainDropDownMenu.setLocation(point);
	          mainDropDownMenu.setVisible(true);
	        }
	      }
	  });
	  
	  // Edit menu
	  final ToolItem itemEditMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
	  itemEditMenu.setText("Edit");
	  itemEditMenu.setToolTipText("Edit options");
	  final Menu editDropDownMenu = new Menu(shell,SWT.POP_UP);
	  new MenuItem(editDropDownMenu,SWT.PUSH).setText("Undo");
	  new MenuItem(editDropDownMenu,SWT.PUSH).setText("Redo");
	  new MenuItem(editDropDownMenu,SWT.PUSH).setText("Find");	  
	  itemEditMenu.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        if(event.detail == SWT.ARROW) {
	          Rectangle bounds = itemEditMenu.getBounds();
	          Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
	          editDropDownMenu.setLocation(point);
	          editDropDownMenu.setVisible(true);
	        }
	      }
	  });
	  
	  // About menu
	  final ToolItem itemAboutMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
	  itemAboutMenu.setText("About");
	  itemAboutMenu.setToolTipText("About the creators");
	  final Menu aboutDropDownMenu = new Menu(shell,SWT.POP_UP);
	  new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Credits");
	  new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Help");
	  new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Update");	  
	  itemAboutMenu.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        if(event.detail == SWT.ARROW) {
	          Rectangle bounds = itemAboutMenu.getBounds();
	          Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
	          aboutDropDownMenu.setLocation(point);
	          aboutDropDownMenu.setVisible(true);
	        }
	      }
	  });
	  
	  toolbar.pack();
	}

}
