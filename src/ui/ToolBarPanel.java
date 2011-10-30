package src.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import src.Main;
import src.service.BetfairConnectionHandler;

public class ToolBarPanel{

	private CoolBar toolbar;
	private CoolBar login;

	private static Logger log = Logger.getLogger(ToolBarPanel.class);

	public ToolBarPanel (final MainWindow mainWindow) {
		Shell shell = mainWindow.getShell();
		// Setting span throughout the columns
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;

		// Setting up the toolbar with the placeholders - due to the number of columns
		this.toolbar = new CoolBar (shell, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);		

		// New widget menu
		final CoolItem widgetItem = new CoolItem(toolbar,SWT.DROP_DOWN);
		Image img = new Image(shell.getDisplay(),"images/plus_item.png");
		widgetItem.setImage(img);
		final Menu widgetDropDown = new Menu(shell,SWT.POP_UP);
		MenuItem matchNavigator = new MenuItem(widgetDropDown,SWT.PUSH);
		matchNavigator.setText("Match Navigator");
		matchNavigator.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				log.info("Opened match navigator");
				mainWindow.addMatchNavigator();
			}
		});
		MenuItem activeBets = new MenuItem(widgetDropDown,SWT.PUSH);
		activeBets.setText("Active Bets Display");
		activeBets.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event arg0) {
				log.info("Opened an active bets display tab");
				mainWindow.addActiveBetsDisplay();
			}

		});

		new MenuItem(widgetDropDown,SWT.PUSH).setText("Match Statistics");
		MenuItem playerStats = new MenuItem(widgetDropDown, SWT.PUSH);
		playerStats.setText("Player Statistics");
		playerStats.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				mainWindow.addPlayerStatistics();
			}
		});
		widgetItem.addListener(SWT.Selection, new LeftDropDownListener(widgetItem, widgetDropDown));
		toolbar.pack();

		// Play button
		final CoolItem playButtonItem = new CoolItem(toolbar,SWT.DROP_DOWN);
		Image play = new Image(shell.getDisplay(),"images/play.png");
		playButtonItem.setImage(play);
		final Menu playDropDown = new Menu(shell,SWT.POP_UP);
		MenuItem playItem = new MenuItem(playDropDown,SWT.PUSH);
		playItem.setText("From File");
		playItem.addListener(SWT.Selection, new Listener(){
			
			@Override
			public void handleEvent(Event arg0) {
				
				/* PAUL -> YOUR CODE GOES HERE */
				
			}
			
		});
		playButtonItem.addListener(SWT.Selection, new LeftDropDownListener(playButtonItem, playDropDown));
		
		// Grid for right alignment
		GridData loginData = new GridData();
		loginData.horizontalSpan = 1;
		loginData.horizontalAlignment = SWT.RIGHT;
		this.login = new CoolBar (shell, SWT.FLAT | SWT.RIGHT);
		login.setLayoutData(loginData);
		// Log out/profile menu
		final CoolItem profileItem = new CoolItem(login,SWT.DROP_DOWN);
		// TODO: this is a slight hack
		profileItem.setText(Main.USERNAME);
		final Menu profileDropDown = new Menu(shell,SWT.POP_UP);
		MenuItem logout = new MenuItem(profileDropDown,SWT.PUSH);
		logout.setText("Log out");
		logout.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					BetfairConnectionHandler.logout();
				} catch (Exception e1) {
					log.error(e1.getMessage());
				}
				Display nd = toolbar.getDisplay();
				mainWindow.dispose();
				new LoginShell(nd);
			}
		});
		new MenuItem(profileDropDown,SWT.PUSH).setText("Profile");
		new MenuItem(profileDropDown,SWT.PUSH).setText("Preferences"); 
		profileItem.addListener(SWT.Selection, new RightDropDownListener(profileItem, profileDropDown));

	}

	private class LeftDropDownListener implements Listener {

		private CoolItem item; 
		private Menu menu;

		public LeftDropDownListener(CoolItem item, Menu menu){
			this.item = item;
			this.menu = menu;
		}
		
		public void handleEvent(Event event) {
			if(event.detail == SWT.ARROW) {
				Rectangle bounds = item.getBounds();
				Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
				menu.setLocation(point);
				menu.setVisible(true);
			}
		}
		
	}

	private class RightDropDownListener implements Listener {

		private CoolItem item; 
		private Menu menu;

		public RightDropDownListener(CoolItem item, Menu menu){
			this.item = item;
			this.menu = menu;
		}

		public void handleEvent(Event event) {
			if(event.detail == SWT.ARROW) {
				Rectangle bounds = item.getBounds();
				Point point = login.toDisplay(bounds.x + bounds.width, bounds.y + bounds.height);
				menu.setLocation(point);
				menu.setVisible(true);
			}
		}

	}

}
