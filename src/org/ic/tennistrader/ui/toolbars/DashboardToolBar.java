package org.ic.tennistrader.ui.toolbars;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.listener.ToolItemListener;

public class DashboardToolBar {
	private final ToolBar toolbar;

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
		final ToolItem fillItem = new ToolItem(toolbar, SWT.PUSH);
		fillItem.setToolTipText("Expand selected widget");
		fillItem.setImage(new Image(shell.getDisplay(), "images/toolbar/expand.png"));
	}

	private void makeLockMenu(Shell shell) {
		final ToolItem redoItem = new ToolItem(toolbar, SWT.CHECK);
		redoItem.setToolTipText("Lock selected widget");
		redoItem.setImage(new Image(shell.getDisplay(), "images/toolbar/locked.png"));
	}

	private void makeUndoRedoMenu(Shell shell) {
		final ToolItem undoItem = new ToolItem(toolbar, SWT.PUSH);
		undoItem.setToolTipText("Undo the last action");
		undoItem.setImage(new Image(shell.getDisplay(), "images/toolbar/undo.png"));

		final ToolItem redoItem = new ToolItem(toolbar, SWT.PUSH);
		redoItem.setToolTipText("Redo the last undone action");
		redoItem.setImage(new Image(shell.getDisplay(), "images/toolbar/redo.png"));
	}

	private void makeBetsTable(Shell shell) {
		final ToolItem betsItem = new ToolItem(toolbar, SWT.PUSH);
		betsItem.setToolTipText("Table of bets");
		betsItem.setImage(new Image(shell.getDisplay(), "images/toolbar/bets.png"));
		betsItem.addListener(SWT.Selection, new BetsListener(betsItem));
	}

	/** New widget button constructor */
	private void makeNewWidgetMenu(Composite parent, final Shell shell) {
		final ToolItem widgetItem = new ToolItem(toolbar, SWT.PUSH);
		widgetItem.setToolTipText("New Widget Menu");
		widgetItem.setImage(new Image(shell.getDisplay(), "images/toolbar/plus.png"));
		final Menu widgetDropDown = new Menu(shell, SWT.POP_UP);
		widgetDropDown.setData("WIDGETMENU");
	}

	/** Play menu constructor */
	private void makePlayMenu(final Shell shell) {
		final ToolItem playButtonItem = new ToolItem(toolbar, SWT.DROP_DOWN);

		playButtonItem.setToolTipText("Play from a file");
		playButtonItem.setImage(new Image(shell.getDisplay(), "images/toolbar/play.png"));

		final Menu playDropDown = new Menu(shell, SWT.POP_UP);

		MenuItem usOpenFinal = new MenuItem(playDropDown, SWT.PUSH);
		usOpenFinal.setText("US Open Final 2011");
		usOpenFinal.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				openMatchView("data/fracsoft/fracsoft1.csv",
						"data/fracsoft/fracsoft1_set.csv");
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
		playButtonItem.addListener(SWT.Selection, new ToolItemListener(toolbar,
				playButtonItem, playDropDown, true));
	}

	/** Open a new match view */
	private void openMatchView(String filename) {
		if (filename != null) {
			Match match = new HistoricalMatch(filename);
			// mainWindow.getDisplayPanel().handleMatchSelection(match);
		}
	}

	/** Open a new match view */
	private void openMatchView(String filename, String setBettingFilename) {
		if (filename != null) {
			Match match = new HistoricalMatch(filename, setBettingFilename);
			// mainWindow.getDisplayPanel().handleMatchSelection(match);
		}
	}

	/** Getter for Tool bar */
	public ToolBar getToolBar() {
		return toolbar;
	}

}