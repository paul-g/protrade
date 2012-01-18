package org.ic.protrade.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ic.protrade.Main;
import org.ic.protrade.service.DataManager;
import org.ic.protrade.ui.LowerToolBar;
import org.ic.protrade.ui.dashboard.Dashboard;
import org.ic.protrade.ui.menus.DashboardMenu;
import org.ic.protrade.ui.toolbars.DashboardCoolBar;
import org.ic.protrade.ui.toolbars.DashboardToolBar;

public class DashboardWindow implements MainWindow {

	private Shell shell;

	private final List<Listener> loadListeners = new ArrayList<Listener>();

	private final Display display;

	private static final DashboardWindow INSTANCE = new DashboardWindow(
			Display.getCurrent());

	private Dashboard dashboard;

	/* ToolBars */
	private DashboardToolBar utb;
	private LowerToolBar ltb;

	private static Composite dashboardComp;

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

	private DashboardWindow(Display display) {
		this.display = display;
	}

	public static void main(String[] args) {
		Display display = new Display();
		MainWindow mw = new DashboardWindow(display);
		mw.show();
		mw.run(display);
	}

	private void show(Display display) {

		notifyLoadEvent("Login successful! Starting application...");
		log.info(display.getClientArea());

		shell = new Shell(display);
		shell.setSize(display.getClientArea().width,
				display.getClientArea().height);
		shell.setMaximized(true);
		shell.setLayout(makeLayout());
		// shell.addListener(SWT.Resize, new
		// StandardWidgetResizeListener(shell));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setText(Main.APPLICATION_TITLE);

		DashboardCoolBar dcb = new DashboardCoolBar(shell);
		dcb.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1,
				1));

		log.info("Adding menu");
		new DashboardMenu(this);
		log.info("Added menu");

		log.info(display.getClientArea());
		log.info("Adding dashboard");
		dashboardComp = new Composite(shell, SWT.BORDER);
		dashboardComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		dashboardComp.setLayout(new FillLayout());
		newDashboard("templates/chart-master/dashboard.dat");
		log.info(display.getClientArea());
		/*
		 * dashboard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
		 * 1, 1));
		 */
		log.info("Added dashboard");
		LowerToolBar ltb = new LowerToolBar(shell);
		ltb.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1,
				1));
	}

	private Layout makeLayout() {
		return new GridLayout(1, true);
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

	@Override
	public void dispose() {
		shell.dispose();
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

	public DashboardToolBar getUpperToolBar() {
		return utb;
	}

	public LowerToolBar getLowerToolBar() {
		return ltb;
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	public Dashboard getCurrentDashboard() {
		return dashboard;
	}

	public void newDashboard(String filename) {
		closeDashboard();
		openDashboard(filename);
		shell.pack();
		dashboardComp.pack();
	}

	private void openDashboard(String filename) {

		if (filename != null) {
			dashboard = new Dashboard(dashboardComp, filename);
			log.info("Opening dashboard from file " + filename);
		} else {
			log.info("Opening default dashboard");
			dashboard = new Dashboard(dashboardComp);
		}
	}

	private void closeDashboard() {
		if (dashboard != null) {
			dashboard.dispose();
		}
	}

	public static DashboardWindow getInstance() {
		return INSTANCE;
	}

	public static Composite getDashboardComp() {
		return dashboardComp;
	}

	public static void addEmptyWidgetToCurrentDashboard() {
		getInstance().getCurrentDashboard().addWidget(0, 0);
		dashboardComp.pack();
	}
}
