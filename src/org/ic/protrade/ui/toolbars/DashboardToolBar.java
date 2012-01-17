package org.ic.protrade.ui.toolbars;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.listener.ToolItemListener;
import org.ic.protrade.ui.dashboard.Dashboard;
import org.ic.protrade.ui.main.DashboardWindow;

public class DashboardToolBar {
	private final class FileMatchOpenListener extends MatchOpenListener {
		private final Shell shell;

		private FileMatchOpenListener(Shell shell) {
			super();
			this.shell = shell;
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {

			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			String[] filterNames = new String[] { "CSV Files", "All Files (*)" };
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
	}

	private class MatchOpenListener extends SelectionAdapter {

		private final String matchOddsPath;
		private final String setOddsPath;

		@SuppressWarnings("unused")
		public MatchOpenListener(String matchOddsPath, String setOddsPath) {
			super();
			this.matchOddsPath = matchOddsPath;
			this.setOddsPath = setOddsPath;
		}

		public MatchOpenListener(String matchPath) {
			super();
			this.matchOddsPath = matchPath;
			setOddsPath = null;
		}

		public MatchOpenListener() {
			matchOddsPath = null;
			setOddsPath = null;
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			super.widgetSelected(arg0);
			if (setOddsPath != null)
				openMatchView(matchOddsPath, setOddsPath);
			else
				openMatchView(matchOddsPath);
		}

		protected void openMatchView(String filename) {
			if (filename != null) {
				Match match = new HistoricalMatch(filename, null);
				DashboardWindow.getInstance().getCurrentDashboard()
						.setMatch(match);
			}
		}

		protected void openMatchView(String filename, String setBettingFilename) {
			if (filename != null) {
				throw new UnsupportedOperationException();
			}
		}
	}

	private final ToolBar toolbar;

	private BetsTable bets = null;

	private static Logger log = Logger.getLogger(DashboardToolBar.class);

	public DashboardToolBar(Composite parent) {
		final Shell shell = parent.getShell();

		GridData gridData = new GridData(GridData.FILL, GridData.END, true,
				false, 1, 1);

		toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		makeNewWidgetMenu(parent, shell);

		makePlayMenu(shell);

		makeUndoRedoMenu(shell);

		makeLockMenu(shell);

		makeFillMenu(shell);

		makeBetsTable(shell);

	}

	private void makeFillMenu(Shell shell) {
		final ToolItem item = new ToolItem(toolbar, SWT.PUSH);
		item.setToolTipText("Expand selected widget");
		item.setImage(new Image(shell.getDisplay(), "images/toolbar/expand.png"));
	}

	private void makeLockMenu(Shell shell) {
		final ToolItem item = new ToolItem(toolbar, SWT.CHECK);
		item.setToolTipText("Lock dashboard configuration");
		item.setImage(new Image(shell.getDisplay(), "images/toolbar/locked.png"));
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				super.widgetSelected(se);
				boolean selection = item.getSelection();
				log.info("Locked: " + selection);
				DashboardWindow.getInstance().getCurrentDashboard()
						.lock(selection);
			}
		});
	}

	private void makeUndoRedoMenu(Shell shell) {
		final ToolItem undoItem = new ToolItem(toolbar, SWT.PUSH);
		undoItem.setToolTipText("Undo the last action");
		undoItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/undo.png"));

		final ToolItem redoItem = new ToolItem(toolbar, SWT.PUSH);
		redoItem.setToolTipText("Redo the last undone action");
		redoItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/redo.png"));
	}

	private void makeBetsTable(Shell shell) {
		final ToolItem betsItem = new ToolItem(toolbar, SWT.PUSH);
		betsItem.setToolTipText("Table of bets");
		betsItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/bets.png"));
		betsItem.addListener(SWT.Selection, new BetsListener(betsItem, this));
	}

	private void makeNewWidgetMenu(Composite parent, final Shell shell) {
		final ToolItem widgetItem = new ToolItem(toolbar, SWT.PUSH);
		widgetItem.setToolTipText("New Widget Menu");
		widgetItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/plus.png"));
		widgetItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				Dashboard d = DashboardWindow.getInstance()
						.getCurrentDashboard();
				d.addWidget(0, 0);
				// DashboardWindow.getInstance().getCurrentDashboard().redraw();
			}
		});
	}

	private void makePlayMenu(final Shell shell) {
		final ToolItem playButtonItem = new ToolItem(toolbar, SWT.DROP_DOWN);

		playButtonItem.setToolTipText("Play from a file");
		playButtonItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/play.png"));

		final Menu playDropDown = new Menu(shell, SWT.POP_UP);

		MenuItem usOpenFinal = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinal.setText("US Open Final 2011");

		/*
		 * usOpenFinal.addSelectionListener(new MatchOpenListener(
		 * "data/fracsoft/fracsoft1.csv", "data/fracsoft/fracsoft1_set.csv"));
		 */

		MenuItem usOpenFinalFull = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinalFull.setText("US Open Final 2011(full)");
		usOpenFinalFull.addSelectionListener(new MatchOpenListener(
				"data/full/fulldata1.csv"));

		MenuItem usOpenFinalFullShort = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinalFullShort.setText("US Open Final 2011(full, but short)");
		usOpenFinalFullShort.addSelectionListener(new MatchOpenListener(
				"data/full/fulldataShort.csv"));

		MenuItem barclays = new MenuItem(playDropDown, SWT.PUSH);
		barclays.setText("Barclays ATP World Tour Finals 2011 Tsonga v Federer - set 3");
		barclays.addSelectionListener(new MatchOpenListener(
				"data/recorded/tso-fed-set-3.csv"));

		MenuItem playItem = new MenuItem(playDropDown, SWT.PUSH);
		playItem.setText("From File");
		playItem.addSelectionListener(new FileMatchOpenListener(shell));
		playButtonItem.addListener(SWT.Selection, new ToolItemListener(toolbar,
				playButtonItem, playDropDown, true));
	}

	public ToolBar getToolBar() {
		return toolbar;
	}

	public BetsTable getBetsTable() {
		return bets;
	}

	public void setBetsTable(BetsTable bets) {
		this.bets = bets;
	}

}