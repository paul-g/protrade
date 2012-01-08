package org.ic.tennistrader.ui.toolbars;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ic.tennistrader.Main;
import org.ic.tennistrader.domain.profile.ProfileData;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;
import org.ic.tennistrader.ui.ProfileWindow;
import org.ic.tennistrader.ui.login.LoginShell;
import org.ic.tennistrader.ui.main.StandardWindow;

public class ProfileToolBar {

	private final ToolBar toolbar;

	private static Logger log = Logger.getLogger(ProfileToolBar.class);

	private ProfileData profileData;

	private Shell preferencesWindow = null;

	private ProfileWindow profileWindow = null;

	public ProfileToolBar(Composite parent) {

		final Shell shell = parent.getShell();

		toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);

		// Retrieving user data from the site
		try {
			log.info("Retrieving proifle data");
			profileData = BetfairConnectionHandler.getProfileData();
			log.info("Profile data fetched");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		makeProfileMenu(parent, shell);

		// Grid for right alignment
		GridData loginData = new GridData();
		loginData.horizontalSpan = 1;
		loginData.horizontalAlignment = SWT.RIGHT;
		toolbar.setLayoutData(loginData);

	}

	/** Profile button menu constructor */
	private void makeProfileMenu(Composite parent, final Shell shell) {
		final ToolItem balanceItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		balanceItem.setToolTipText("Balance");

		final ToolItem profileItem = new ToolItem(toolbar, SWT.DROP_DOWN);
		// TODO: this is a slight hack
		profileItem.setText(Main.username);
		profileItem.setToolTipText("Profile");

		try {
			balanceItem.setText("£"
					+ profileData.getUkAccountFunds().getBalance());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		try {
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
						balanceItem
								.setText("$"
										+ profileData.getAusAccountFunds()
												.getBalance());
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
			balanceItem.addListener(SWT.Selection, new ToolItemListener(
					toolbar, balanceItem, balanceDropDown, false));
			profileItem.addListener(SWT.Selection, new ToolItemListener(
					toolbar, profileItem, profileDropDown, false));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
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

}
