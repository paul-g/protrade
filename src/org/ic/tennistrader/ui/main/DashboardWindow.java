package org.ic.tennistrader.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.LowerToolBar;
import org.ic.tennistrader.ui.UpperToolBar;
import org.ic.tennistrader.ui.dashboard.Dashboard;
import org.ic.tennistrader.ui.menus.DashboardMenu;
import org.ic.tennistrader.ui.widgets.StandardWidgetResizeListener;

public class DashboardWindow implements MainWindow {

	private Shell shell;

	private final List<Listener> loadListeners = new ArrayList<Listener>();

	private final Display display;

	private Dashboard dashboard;

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

	public DashboardWindow(Display display) {
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

		this.shell = new Shell(display);
		shell.setMaximized(true);
		shell.setLayout(makeLayout());
		shell.addListener(SWT.Resize, new StandardWidgetResizeListener(shell));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		// new UpperToolBar(mainWindow);

		log.info("Adding menu");
		new DashboardMenu(this);
		log.info("Added menu");

		System.out.println(shell.getClientArea());
		dashboard = new Dashboard(shell);

		// new LowerToolBar(this);
		// ltb = new LowerToolBar(this);
	}

	private Layout makeLayout() {
		return new FillLayout();
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

	public UpperToolBar getUpperToolBar() {
		return utb;
	}

	public LowerToolBar getLowerToolBar() {
		return ltb;
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void newDashboard() {
		if (dashboard != null) {
			dashboard.dispose();
		}
		dashboard = new Dashboard(shell);
		shell.layout();
	}

	public void loadDashboard(String filename) {

		if (dashboard != null) {
			dashboard.dispose();
		}
		dashboard = new Dashboard(shell, filename);
		shell.layout();
	}
}
