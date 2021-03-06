package org.ic.protrade.ui.toolbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ic.protrade.Main;
import org.ic.protrade.data.market.connection.BetfairConnectionHandler;
import org.ic.protrade.data.market.connection.ProfileData;
import org.ic.protrade.listener.ToolItemListener;
import org.ic.protrade.model.betting.BetManager;
import org.ic.protrade.ui.ProfileWindow;
import org.ic.protrade.ui.login.LoginShell;
import org.ic.protrade.ui.main.StandardWindow;

public class ProfileToolBar {

	private final ToolBar toolbar;

	private static Logger log = Logger.getLogger(ProfileToolBar.class);

	private ProfileData profileData;

	private Shell preferencesWindow = null;

	private ProfileWindow profileWindow = null;

	private final Shell shell;

	private ToolItem liabilityToolItem;

	private ToolItem profitToolItem;

	// temporary, since access to profile toolbar from BetManager is required
	private static List<ProfileToolBar> profileToolBars = new ArrayList<ProfileToolBar>();

	public ProfileToolBar(Composite parent) {

		shell = parent.getShell();

		toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);

		// Retrieving user data from the site
		try {
			log.info("Retrieving proifle data");
			profileData = BetfairConnectionHandler.getProfileData();
			log.info("Profile data fetched");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		makeLiabilityItem();
		makeProfitItem();
		makeBalanceItem();
		makeProfileItem();
		profileToolBars.add(this);
	}

	/**
	 * Method for coloured text label creation
	 * 
	 * @return
	 */
	private Image createColouredText(String text, int color) {
		Display display = Display.getCurrent();
		GC gc = new GC(toolbar);
		Point p = gc.textExtent(text);
		Image image = new Image(display, p.x, p.y);
		gc.dispose();
		gc = new GC(image);
		gc.setForeground(display.getSystemColor(color));
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		gc.drawText(text, 0, 0, false);
		gc.dispose();
		return image;
	}

	/** Profit Item constructor */
	private void makeProfitItem() {
		final ToolItem item = new ToolItem(toolbar, SWT.NONE);
		item.setText("Profit:");
		item.setToolTipText("Total Profit");
		profitToolItem = new ToolItem(toolbar, SWT.NONE);
		updateProfitToolItem();
	}

	public void updateProfitToolItem() {
		profitToolItem
				.setImage(createColouredText(
						"£" + BetManager.getTotalMinimalProfit(),
						SWT.COLOR_DARK_GREEN));
	}

	/** Liability Item constructor */
	private void makeLiabilityItem() {
		final ToolItem item = new ToolItem(toolbar, SWT.NONE);
		item.setText("Liability:");
		item.setToolTipText("Total Liability");
		liabilityToolItem = new ToolItem(toolbar, SWT.NONE);
		updateLiabilityToolItem();
	}

	public void updateLiabilityToolItem() {
		liabilityToolItem.setImage(createColouredText(
				"£" + BetManager.getTotalLiabiltiy(), SWT.COLOR_RED));
	}

	/** Profile button menu constructor */
	private void makeProfileItem() {

		try {
			final ToolItem profileItem = new ToolItem(toolbar, SWT.DROP_DOWN);
			// TODO: this is a slight hack
			profileItem.setText(Main.username);
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
					final Display nd = toolbar.getDisplay();
					// mainWindow.dispose();
					final LoginShell ls = new LoginShell(nd);
					final StandardWindow mw = new StandardWindow(nd);
					mw.addLoadListener(new Listener() {
						@Override
						public void handleEvent(Event event) {
							if (event.text.equals("Done!")) {
								ls.finishProgressBar();
								ls.dispose();
							} else {
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
			profileItem.addListener(SWT.Selection, new ToolItemListener(
					toolbar, profileItem, profileDropDown, false));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private Menu makeBalanceItem() {
		final ToolItem balanceItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		balanceItem.setToolTipText("Balance");

		try {
			balanceItem.setText("£"
					+ profileData.getUkAccountFunds().getBalance());
		} catch (Exception e) {
			log.error(e.getMessage());
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
		balanceItem.addListener(SWT.Selection, new ToolItemListener(toolbar,
				balanceItem, balanceDropDown, false));

		return balanceDropDown;
	}

	/** Method invoking the Profile Window appearance */
	public boolean openProfileWindow() {
		if (profileWindow == null || profileWindow.isDisposed()) {
			profileWindow = new ProfileWindow(toolbar.getDisplay(), profileData);
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
			preferencesWindow.setSize(200, 200);
			preferencesWindow.setLocation(1120, 100);
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

	public ToolBar getToolBar() {
		return toolbar;
	}

	public static void updateLiabilityAndProfit() {
		for (ProfileToolBar ptb : profileToolBars) {
			ptb.updateLiabilityToolItem();
			ptb.updateProfitToolItem();
		}
	}

}
