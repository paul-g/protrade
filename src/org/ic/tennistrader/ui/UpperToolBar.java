package org.ic.tennistrader.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ic.tennistrader.Main;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.profile.AccountFunds;
import org.ic.tennistrader.domain.profile.ProfileData;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;

public class UpperToolBar {
	private ToolBar toolbar;
	private ToolBar login;
	private Shell profileWindow = null;
	private Shell preferencesWindow = null;
	private ProfileData profileData;
	private static Logger log = Logger.getLogger(UpperToolBar.class);
	private MainWindow mainWindow;

	public UpperToolBar(final MainWindow mainWindow) {
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

		// Retrieving user data from the site
		try {
			profileData = BetfairConnectionHandler.getProfileData();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		// Log out/profile menu
		makeProfileMenu(mainWindow, shell);
		
	}
	
	/** Profile button menu constructor */
	private void makeProfileMenu(final MainWindow mainWindow, final Shell shell) {
		final ToolItem balanceItem = new ToolItem(login, SWT.DROP_DOWN);
		balanceItem.setToolTipText("Balance");


		final ToolItem profileItem = new ToolItem(login, SWT.DROP_DOWN);
		// TODO: this is a slight hack
		profileItem.setText(Main.USERNAME);
		profileItem.setToolTipText("Profile");
		
		try {		
			balanceItem.setText("£"
					+ profileData.getUkAccountFunds().getBalance());

			final Menu balanceDropDown = new Menu(shell, SWT.POP_UP);
			MenuItem ukBalance = new MenuItem(balanceDropDown, SWT.PUSH);
			ukBalance.setText("GBP");
			ukBalance.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					balanceItem.setText("£"
							+ profileData.getUkAccountFunds().getBalance());
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
						balanceItem.setText("$"
							 + profileData.getAusAccountFunds().getBalance());
					} catch (Exception e1) {
						log.error(e1.getMessage());
					}
				}
			});

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
					final Display nd = toolbar.getDisplay();
					mainWindow.dispose();
					final LoginShell ls = new LoginShell(nd);
					final MainWindow mw = new MainWindow(nd); 
					mw.addLoadListener(new Listener() {
					        @Override
					        public void handleEvent(Event event) {
					            if (event.text.equals("Done!")) {
					                ls.finishProgressBar();
					                ls.dispose();
					            }
					            else {
					                ls.updateProgressBar(10);
					                ls.setText(event.text);
					            }
					        }
					});
					ls.addLoginSuccessListener(new Listener() {
				        @Override
				        public void handleEvent(Event arg0) {
				            mw.show();
				            mw.run(nd);
				        }
				    });
					ls.run(nd);
				}
			});
			MenuItem profileButton = new MenuItem(profileDropDown, SWT.PUSH);
			profileButton.setText("Profile");
			profileButton.addSelectionListener(new SelectionListener() {
	            @Override
	            public void widgetDefaultSelected(SelectionEvent e) {
	            }

	            @Override
	            public void widgetSelected(SelectionEvent e) {
	            	openProfileWindow();
	            }
	        });
			MenuItem preferencesButton = new MenuItem(profileDropDown, SWT.PUSH);
			preferencesButton.setText("Preferences");
			preferencesButton.addSelectionListener(new SelectionListener() {
	            @Override
	            public void widgetDefaultSelected(SelectionEvent e) {
	            }

	            @Override
	            public void widgetSelected(SelectionEvent e) {
	            	openPreferencesWindow();
	            }
	        });
			profileItem.addListener(SWT.Selection, new RightDropDownListener(
					profileItem, profileDropDown));
			balanceItem.addListener(SWT.Selection, new RightDropDownListener(
					balanceItem, balanceDropDown));
		} catch (Exception e) {
		}
	}

	/** New widget button constructor */
	private void makeNewWidgetMenu(final MainWindow mainWindow,
			final Shell shell) {
		final ToolItem widgetItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		widgetItem.setToolTipText("Widget Menu");
		final Image img = new Image(shell.getDisplay(), "images/plus.png");
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
		
        MenuItem matchViewer = new MenuItem(widgetDropDown, SWT.PUSH);
        matchViewer.setText("Match Viewer");
        matchViewer.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                mainWindow.addMatchViewer();
            }
        });

        widgetItem.addListener(SWT.Selection, new LeftDropDownListener(
                widgetItem, widgetDropDown));
		widgetItem.addListener(SWT.Selection, new LeftDropDownListener(
				widgetItem, widgetDropDown));
		toolbar.pack();
	}

	/** Play menu constructor */
	private void makePlayMenu(final Shell shell) {
		final ToolItem playButtonItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		playButtonItem.setToolTipText("Play from a file");
		Image play = new Image(shell.getDisplay(), "images/play.png");
		playButtonItem.setImage(play);

		final Menu playDropDown = new Menu(shell, SWT.POP_UP);

		MenuItem usOpenFinal = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinal.setText("US Open Final 2011");
		usOpenFinal.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				openMatchView("data/fracsoft/fracsoft1.csv");
			}
		});
		
		MenuItem usOpenFinalFull = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinalFull.setText("US Open Final 2011(full)");
		usOpenFinalFull.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                openMatchView("data/full/fulldata1.csv");
            }
        });
		
		MenuItem usOpenFinalFullShort = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinalFullShort.setText("US Open Final 2011(full, but short)");
		usOpenFinalFullShort.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                openMatchView("data/full/fulldataShort.csv");
            }
        });
		
		MenuItem barclays = new MenuItem(playDropDown, SWT.PUSH);
		barclays.setText("Barclays ATP World Tour Finals 2011 Tsonga v Federer - set 3");
		barclays.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                openMatchView("data/recorded/tso-fed-set-3.csv");
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

	/** Text representation of the profile */
	public String textProfile() {
		String res ="";
		try {
			AccountFunds af = profileData.getUkAccountFunds();
			res =
				"Username : " + Main.USERNAME +
				"\nBetfair points : " + af.getBetfairPoints() +
				"\nCurrent balance : " + af.getBalance() +
				"\nAvailable balance : " + af.getAvailable() +
				"\nCredit limit : " + af.getCreditLimit() +
				"\nExposure : " + af.getExposure() +
				"\nExposure limit : " + af.getExposureLimit();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return res;
	}
	
	/** Open a new match view */
	private void openMatchView(String filename) {
		if (filename != null) {
			Match match = new HistoricalMatch(filename);
			mainWindow.getDisplayPanel().addMatchView(match);
		}
	}
	
	/** Method invoking the Profile Window appearance */
	public boolean openProfileWindow() {
    	if (profileWindow == null || profileWindow.isDisposed()) {
            profileWindow = new Shell(toolbar.getDisplay(), SWT.SHELL_TRIM);
            profileWindow.setLayout(new FillLayout());
            profileWindow.setText("Profile");
            profileWindow.setSize(200,200);
            profileWindow.setLocation(1100,80);
            Label profData = new Label(profileWindow,SWT.BORDER);
            profData.setText(textProfile());
            profileWindow.open();
            return true;
    	} else {
    		profileWindow.forceActive();
    		return false;
    	}
	}
	
	/** Method for Preferences Window invocation */
	public boolean openPreferencesWindow() {
		if (preferencesWindow == null || preferencesWindow.isDisposed()) {
            preferencesWindow = new Shell(toolbar.getDisplay(), SWT.SHELL_TRIM);
            preferencesWindow.setLayout(new FillLayout());
            preferencesWindow.setText("Preferences");
            preferencesWindow.setSize(200,200);
            preferencesWindow.setLocation(1120,100);
            ToolBar prefData = new ToolBar(preferencesWindow, SWT.VERTICAL);
            new ToolItem(prefData, SWT.CHECK).setText("Remember Me");
            new ToolItem(prefData, SWT.CHECK).setText("Display my name");
            preferencesWindow.open();
            return true;
    	} else {
    		preferencesWindow.forceActive();
    		return false;
    	}
	}
	
	/** Listener for the left hand side buttons */
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

	/** Listener for right hand side buttons */
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
