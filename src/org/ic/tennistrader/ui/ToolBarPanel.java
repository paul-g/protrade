package org.ic.tennistrader.ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.ic.tennistrader.Main;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;

public class ToolBarPanel {

	private ToolBar toolbar;
	private ToolBar login;

	private static Logger log = Logger.getLogger(ToolBarPanel.class);

	private MainWindow mainWindow;

	public ToolBarPanel(final MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		final Shell shell = mainWindow.getShell();

		// Setting span throughout the columns
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;

		// Setting up the toolbar with the placeholders - due to the number of
		// columns
		this.toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		// New widget menu
		makeNewWidgetMenu(mainWindow, shell);

		// Play button
		makePlayMenu(shell);

		// Grid for right alignment
		GridData loginData = new GridData();
		loginData.horizontalSpan = 1;
		loginData.horizontalAlignment = SWT.RIGHT;
		this.login = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		login.setLayoutData(loginData);

		// Log out/profile menu
		makeProfileMenu(mainWindow, shell);

	}

	private void makeProfileMenu(final MainWindow mainWindow, final Shell shell) {
		final ToolItem balanceItem = new ToolItem(login,SWT.DROP_DOWN);
		balanceItem.setToolTipText("Balance");
		try {
			balanceItem.setText("£"+BetfairConnectionHandler.getProfileData().getUkAccountFunds().getBalance());
		} catch (Exception e2) {
			log.error(e2.getMessage());
		}
		final Menu balanceDropDown = new Menu(shell, SWT.POP_UP);
		MenuItem ukBalance = new MenuItem(balanceDropDown, SWT.PUSH);
		ukBalance.setText("GBP");
		ukBalance.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					balanceItem.setText("£"+BetfairConnectionHandler.getProfileData().getUkAccountFunds().getBalance());
				} catch (Exception e1) {
					log.error(e1.getMessage());
				}
			}
		});
		MenuItem ausBalance = new MenuItem(balanceDropDown, SWT.PUSH);
		ausBalance.setText("AUD");
		ausBalance.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					balanceItem.setText("$"+BetfairConnectionHandler.getProfileData().getAusAccountFunds().getBalance());
				} catch (Exception e1) {
					log.error(e1.getMessage());
				}
			}
		});

		final ToolItem profileItem = new ToolItem(login, SWT.DROP_DOWN);
		// TODO: this is a slight hack
		profileItem.setText(Main.USERNAME);
		profileItem.setToolTipText("Profile");
		final Menu profileDropDown = new Menu(shell, SWT.POP_UP);
		MenuItem logout = new MenuItem(profileDropDown, SWT.PUSH);
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
		new MenuItem(profileDropDown, SWT.PUSH).setText("Profile");
		new MenuItem(profileDropDown, SWT.PUSH).setText("Preferences");
		profileItem.addListener(SWT.Selection, new RightDropDownListener(
				profileItem, profileDropDown));
		balanceItem.addListener(SWT.Selection, new RightDropDownListener(
				balanceItem, balanceDropDown));
	}

	private void makeNewWidgetMenu(final MainWindow mainWindow,
			final Shell shell) {
		final ToolItem widgetItem = new ToolItem(toolbar, SWT.DROP_DOWN);       
		final Image img = new Image(shell.getDisplay(), "images/plus_item.png");
		widgetItem.setImage(img);
		final Menu widgetDropDown = new Menu(shell, SWT.POP_UP);
		MenuItem matchNavigator = new MenuItem(widgetDropDown, SWT.PUSH);
		matchNavigator.setText("Match Navigator");
		matchNavigator.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				log.info("Opened match navigator");
				mainWindow.addMatchNavigator();
			}
		});

		MenuItem activeBets = new MenuItem(widgetDropDown, SWT.PUSH);
		activeBets.setText("Active Bets Display");
		activeBets.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				log.info("Opened an active bets display tab");
				mainWindow.addActiveBetsDisplay();
			}

		});

		new MenuItem(widgetDropDown, SWT.PUSH).setText("Match Statistics");
		MenuItem playerStats = new MenuItem(widgetDropDown, SWT.PUSH);
		playerStats.setText("Player Statistics");
		playerStats.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				mainWindow.addPlayerStatistics();
			}
		});
		widgetItem.addListener(SWT.Selection, new LeftDropDownListener(
				widgetItem, widgetDropDown));
		toolbar.pack();
	}

	private void makePlayMenu(final Shell shell) {
		final ToolItem playButtonItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		Image play = new Image(shell.getDisplay(), "images/play.png");
		playButtonItem.setImage(play);

		final Menu playDropDown = new Menu(shell, SWT.POP_UP);

		MenuItem usOpenFinal = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinal.setText("US Open Final 2011");
		usOpenFinal.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				openMatchView("fracsoft-data/fracsoft1.csv");
			}
		});

		MenuItem playItem = new MenuItem(playDropDown, SWT.PUSH);
		playItem.setText("From File");
		playItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {

				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				String[] filterNames = new String[] { "CSV Files",
				"All Files (*)" };
				String[] filterExtensions = new String[] { "*.csv", "*" };
				String filterPath = "/";
				String platform = SWT.getPlatform();
				if (platform.equals("win32") || platform.equals("wpf")) {
					filterNames = new String[] { "CSV Files", "All Files (*.*)" };
					filterExtensions = new String[] { "*.csv", "*.*" };
					filterPath = "c:\\";
				}
				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);
				dialog.setFilterPath(filterPath);
				dialog.setFileName("myfile");

				openMatchView(dialog.open());
			}

		});
		playButtonItem.addListener(SWT.Selection, new LeftDropDownListener(
				playButtonItem, playDropDown));
	}

	public ToolBarPanel(final MainWindow mainWindow, boolean isTop) {

		final Shell shell = mainWindow.getShell();
		// Setting span throughout the columns
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		Display display = shell.getDisplay();

		this.toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		final ToolItem widgetItem = new ToolItem(toolbar, SWT.POP_UP);
		widgetItem.setToolTipText("Internet Connection");
		final Image off = new Image(display, "images/connection_lost.png");
		final Image on = new Image(display, "images/connection_on.png");
		widgetItem.setImage(on);
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try { Thread.sleep(3000); } catch (Exception e) { }
					if (!shell.isDisposed()) {
						toolbar.getDisplay().asyncExec(new Runnable() {
							public void run() {
								if (isInternetReachable()) {
									log.info("Connection - ON");
									widgetItem.setImage(on);
								} else {
									log.info("Connection - OFF");
									widgetItem.setImage(off);
								}
							}
						});
					}
				}
			}
		}).start();
	}

	private void openMatchView(String filename) {
		if (filename != null) {

			// TODO: change the match to a Historical match
			Match match = new HistoricalMatch(filename);

			mainWindow.getDisplayPanel().addMatchView(match);
		}
	}

	private boolean isInternetReachable() {
		try {
			// URL to a source
			URL url = new URL("http://www.google.com");
			// Open a connection
			HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
			// Retrieving data from the source - if there is no connection, throws and exception
			@SuppressWarnings("unused")
			Object objData = urlConnect.getContent();
		} catch (UnknownHostException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	private class LeftDropDownListener implements Listener {

		private ToolItem item;
		private Menu menu;

		public LeftDropDownListener(ToolItem item, Menu menu) {
			this.item = item;
			this.menu = menu;
		}

		public void handleEvent(Event event) {
			if (event.detail == SWT.ARROW) {
				Rectangle bounds = item.getBounds();
				Point point = toolbar.toDisplay(bounds.x, bounds.y
						+ bounds.height);
				menu.setLocation(point);
				menu.setVisible(true);
			}
		}

	}

	private class RightDropDownListener implements Listener {

		private ToolItem item;
		private Menu menu;

		public RightDropDownListener(ToolItem item, Menu menu) {
			this.item = item;
			this.menu = menu;
		}

		public void handleEvent(Event event) {
			if (event.detail == SWT.ARROW) {
				Rectangle bounds = item.getBounds();
				Point point = login.toDisplay(bounds.x + bounds.width, bounds.y
						+ bounds.height);
				menu.setLocation(point);
				menu.setVisible(true);
			}
		}
	}
}
