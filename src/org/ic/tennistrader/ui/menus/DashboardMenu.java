package org.ic.tennistrader.ui.menus;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.ui.dialogs.LiveMatchDialog;
import org.ic.tennistrader.ui.dialogs.ProgressDialog;
import org.ic.tennistrader.ui.main.DashboardWindow;
import org.ic.tennistrader.ui.main.LayoutDialog;

public class DashboardMenu {

	private final class MatchRunnable implements Runnable {
		private final String filename;
		private final ProgressDialog pd;
		private final String setBettingFilename;
		private Match match;

		private MatchRunnable(String filename, ProgressDialog pd,
				String setBettingFilename) {
			this.filename = filename;
			this.pd = pd;
			this.setBettingFilename = setBettingFilename;
		}

		@Override
		public void run() {
			match = new HistoricalMatch(filename, setBettingFilename, pd);
		}

		public Match getMatch() {
			return match;
		}
	}

	private static final Logger log = Logger.getLogger(DashboardMenu.class);

	private final DashboardWindow dashboardWindow;

	public DashboardMenu(DashboardWindow dashboardWindow) {
		this.dashboardWindow = dashboardWindow;
		Shell shell = dashboardWindow.getShell();
		Menu menuBar = new Menu(shell, SWT.BAR);
		makeDashboardMenu(shell, menuBar);
		makeAboutMenu(shell, menuBar);
		makeOpenMenu(shell, menuBar);
		shell.setMenuBar(menuBar);
	}

	private void makeOpenMenu(final Shell shell, Menu menuBar) {
		MenuItem openHeader = new MenuItem(menuBar, SWT.CASCADE);
		openHeader.setText("&Open");

		Menu openMenu = new Menu(shell, SWT.DROP_DOWN);
		openHeader.setMenu(openMenu);

		MenuItem liveMatchMenu = new MenuItem(openMenu, SWT.DROP_DOWN);
		liveMatchMenu.setText("&Live Match");
		liveMatchMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				LiveMatchDialog lmd = new LiveMatchDialog(shell);
				lmd.open();
				Match match = lmd.getSelectedMatch();
				log.info("Dialog selection " + match);
				if (match != null) {
					dashboardWindow.getCurrentDashboard().setMatch(match);
				}

			}
		});

		MenuItem simulationMenu = new MenuItem(openMenu, SWT.CASCADE);
		simulationMenu.setText("&Recorded Match");
		simulationMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Opening simulation");
			}
		});

		final Menu playDropDown = new Menu(shell, SWT.DROP_DOWN);

		simulationMenu.setMenu(playDropDown);

		makeRecordedItem(playDropDown, "US Open Final 2011",
				"data/fracsoft/fracsoft1.csv",
				"data/fracsoft/fracsoft1_set.csv");

		makeRecordedItem(playDropDown, "US Open Final 2011(full)",
				"data/full/fulldata1.csv");

		makeRecordedItem(playDropDown, "US Open Final 2011(full, but short)",
				"data/full/fulldataShort.csv");

		makeRecordedItem(playDropDown,
				"Barclays ATP World Tour Finals 2011 Tsonga v Federer - set 3",
				"data/recorded/tso-fed-set-3.csv");

		makeRecordedItem(
				playDropDown,
				"Barclays ATP World Tour Finals 2011 Tsonga v Federer with Fracsoft - set 3",
				"data/recorded/fracsoft-score.csv");

		MenuItem playItem = new MenuItem(playDropDown, SWT.PUSH);
		playItem.setText("From File");
		playItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				FileDialog dialog = new FileDialog(dashboardWindow.getShell(),
						SWT.SAVE);
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

				openMatchView(dialog.open(), null);
			}
		});
	}

	private void makeRecordedItem(final Menu playDropDown, final String text,
			final String matchPath, final String setPath) {

		MenuItem usOpenFinalFullShort = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinalFullShort.setText(text);
		usOpenFinalFullShort.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				openMatchView(matchPath, setPath);
			}
		});
	}

	private void makeRecordedItem(final Menu playDropDown, final String text,
			final String filePath) {
		MenuItem usOpenFinalFullShort = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinalFullShort.setText(text);
		usOpenFinalFullShort.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				openMatchView(filePath, null);
			}
		});
	}

	private void makeAboutMenu(Shell shell, Menu menuBar) {
		MenuItem aboutMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuHeader.setText("A&bout");

		Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
		aboutMenuHeader.setMenu(aboutMenu);
	}

	private void makeDashboardMenu(Shell shell, Menu menuBar) {
		MenuItem dasboard = new MenuItem(menuBar, SWT.CASCADE);
		dasboard.setText("&Dashboard");

		Menu menu = new Menu(shell, SWT.DROP_DOWN);
		dasboard.setMenu(menu);

		makeNewMenu(menu);

		MenuItem saveItem = new MenuItem(menu, SWT.PUSH);
		saveItem.setText("&Save");

		saveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.getCurrentDashboard().save();
			}
		});

		MenuItem saveAsItem = new MenuItem(menu, SWT.PUSH);
		saveAsItem.setText("&Save As");

		saveAsItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.getCurrentDashboard().save();
			}
		});

		MenuItem loadItem = new MenuItem(menu, SWT.PUSH);
		loadItem.setText("&Load");

		loadItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.newDashboard("templates/dashboard.dat");
			}
		});
	}

	private void makeNewMenu(Menu menu) {
		MenuItem newItem = new MenuItem(menu, SWT.CASCADE);
		newItem.setText("&New");
		Menu newSubMenu = new Menu(newItem);
		newItem.setMenu(newSubMenu);

		MenuItem newEmpty = new MenuItem(newSubMenu, SWT.PUSH);
		newEmpty.setText("Empty");

		newEmpty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.newDashboard(null);
			}
		});

		MenuItem newPredefined = new MenuItem(newSubMenu, SWT.PUSH);
		newPredefined.setText("Predefined");

		newPredefined.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutDialog ld = new LayoutDialog();
				ld.show();
				String s = ld.getSelection();
				if (s != null) {
					dashboardWindow.newDashboard(s);
				}
			}
		});
	}

	/** Open a new match view */
	private void openMatchView(final String filename,
			final String setBettingFilename) {
		log.info("Opening match view");
		Match match;
		if (filename != null) {
			final ProgressDialog pd = new ProgressDialog(
					dashboardWindow.getShell());
			MatchRunnable r = new MatchRunnable(filename, pd,
					setBettingFilename);

			// new Thread(r).start();
			// pd.open();

			// dashboardWindow.getCurrentDashboard().setMatch(r.getMatch());

			Match m = new HistoricalMatch(filename, setBettingFilename, pd);
			m.getScore().setServer(PlayerEnum.PLAYER1);
			dashboardWindow.getCurrentDashboard().setMatch(
					new HistoricalMatch(filename, setBettingFilename, pd));

		}
	}
}