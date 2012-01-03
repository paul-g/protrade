package org.ic.tennistrader.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.Main;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.DisplayPanel;
import org.ic.tennistrader.ui.LowerToolBar;
import org.ic.tennistrader.ui.NavigationPanel;
import org.ic.tennistrader.ui.UpperToolBar;
import org.ic.tennistrader.ui.betting.BetsDisplay;
import org.ic.tennistrader.ui.menus.MenuPanel;
import org.ic.tennistrader.ui.widgets.StandardWidgetResizeListener;

public class StandardWindow implements MainWindow {

	private Shell shell;

	private final List<Listener> loadListeners = new ArrayList<Listener>();

	// the sash forms
	private static SashForm sashForm;
	private SashForm sashFormLeft;
	private SashForm sashFormRight;
	private final Display display;

	private static DisplayPanel dp;

	private NavigationPanel np;

	/* ToolBars */
	private UpperToolBar utb;
	private LowerToolBar ltb;

	private static Logger log = Logger.getLogger(StandardWindow.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ic.tennistrader.ui.ApplicationWindow#show()
	 */
	@Override
	public Shell show() {
		show(display);
		shell.open();
		return shell;
	}

	public StandardWindow(Display display) {
		this.display = display;
	}

	public static void main(String[] args) {
		Display display = new Display();
		MainWindow mw = new StandardWindow(display);
		Shell s = mw.show();
		while (!s.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void show(Display display) {

		notifyLoadEvent("Login successful! Starting application...");

		this.shell = new Shell(display);
		shell.setMaximized(true);

		shell.addListener(SWT.Resize, new StandardWidgetResizeListener(shell));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		log.info("Starting upper toolbar");
		utb = new UpperToolBar(this);
		log.info("Upper toolbar started");

		makeLayout();

		@SuppressWarnings("unused")
		MenuPanel mp = new MenuPanel(this);

		log.info("Starting navigation panel");
		this.np = new NavigationPanel(sashFormLeft);
		notifyLoadEvent("Fetching betfair data");
		log.info("Started navigation panel");

		dp = new DisplayPanel(sashFormRight, SWT.BORDER);

		notifyLoadEvent("Preparing display");
		np.addListener(dp);

		addActiveBetsDisplay(sashFormLeft);

		notifyLoadEvent("Configuring toolbars");

		ltb = new LowerToolBar(this);

		notifyLoadEvent("Done!");
		shell.layout();
	}

	private void makeLayout() {
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 10;
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;
		// layout.justify = true;

		shell.setLayout(layout);
		shell.setText(Main.TITLE);

		// Sashform set-up
		sashForm = new SashForm(shell, SWT.HORIZONTAL);
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.horizontalSpan = 3;
		layoutData.verticalAlignment = GridData.FILL;
		sashForm.setLayoutData(layoutData);
		sashForm.setLayout(layout);

		this.sashFormLeft = new SashForm(sashForm, SWT.VERTICAL);
		this.sashFormRight = new SashForm(sashForm, SWT.VERTICAL);

		sashForm.setFocus();
		sashForm.setWeights(new int[] { 20, 80 });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ic.tennistrader.ui.ApplicationWindow#run(org.eclipse.swt.widgets.
	 * Display)
	 */
	@Override
	public void run(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		DataManager.stopAllThreads();
		LowerToolBar.setStop();

		display.dispose();
	}

	public void addActiveBetsDisplay() {
		addActiveBetsDisplay(sashFormLeft);
	}

	private void addActiveBetsDisplay(Composite composite) {
		new BetsDisplay(composite, SWT.BORDER);
	}

	public void addMatchNavigator() {
		if (!np.isTabPresent("Match Navigator"))
			np.addTab("Match Navigator");
	}

	public void addPlayerStatistics() {
		addPlayerStatistics(sashFormRight);
	}

	private void addPlayerStatistics(Composite composite) {
		CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
		CTabItem cti = new CTabItem(tabFolder, SWT.CLOSE);
		tabFolder.setSimple(false);
		tabFolder.setMinimizeVisible(true);
		tabFolder.setMaximizeVisible(true);

		cti.setText("Player Statistics");
		final Browser browser;
		try {
			browser = new Browser(tabFolder, SWT.NONE);
		} catch (SWTError e) {
			log.error("Could not instantiate Browser: " + e.getMessage());
			return;
		}
		cti.setControl(browser);
		tabFolder.setSelection(cti);
		browser.setUrl("http://www.atpworldtour.com/Rankings/Singles.aspx");
		composite.layout();
	}

	public void addNewTab(String text) {
		dp.addTab(text);
	}

	@Override
	public Shell getShell() {
		return this.shell;
	}

	public void dispose() {
		shell.dispose();
	}

	public DisplayPanel getDisplayPanel() {
		return dp;
	}

	@Override
	public void addLoadListener(Listener listener) {
		loadListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ic.tennistrader.ui.ApplicationWindow#notifyLoadEvent(java.lang.String
	 * )
	 */
	@Override
	public void notifyLoadEvent(String name) {
		for (Listener l : loadListeners) {
			Event e = new Event();
			e.text = name;
			l.handleEvent(e);
		}
	}

	public static void toggleMaximizeMatchDisplay() {
		if (sashForm.getMaximizedControl() == dp.getControl())
			sashForm.setMaximizedControl(null);
		else
			sashForm.setMaximizedControl(dp.getControl());
	}

	public void addMatchViewer() {
		dp.addMatchViewer();
	}

	public UpperToolBar getUpperToolBar() {
		return utb;
	}

	public LowerToolBar getLowerToolBar() {
		return ltb;
	}
}
