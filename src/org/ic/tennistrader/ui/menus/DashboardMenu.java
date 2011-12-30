package org.ic.tennistrader.ui.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.ui.main.DashboardWindow;

public class DashboardMenu {

	private DashboardWindow dashboardWindow;

	public DashboardMenu(DashboardWindow dashboardWindow) {
		this.dashboardWindow = dashboardWindow;
		Shell shell = dashboardWindow.getShell();
		Menu menuBar = new Menu(shell, SWT.BAR);
		makeDashboardMenu(shell, menuBar);
		makeAboutMenu(shell, menuBar);
	}

	private void makeAboutMenu(Shell shell, Menu menuBar) {
		// About button
		MenuItem aboutMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuHeader.setText("A&bout");
		shell.setMenuBar(menuBar);

		// About drop down menu
		Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
		aboutMenuHeader.setMenu(aboutMenu);
	}

	private void makeDashboardMenu(Shell shell, Menu menuBar) {
		MenuItem dasboard = new MenuItem(menuBar, SWT.CASCADE);
		dasboard.setText("&Dashboard");
		shell.setMenuBar(menuBar);

		Menu menu = new Menu(shell, SWT.DROP_DOWN);
		dasboard.setMenu(menu);

		MenuItem newItem = new MenuItem(menu, SWT.PUSH);
		newItem.setText("&New");
		newItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.newDashboard();
			}
		});
		
		MenuItem saveItem = new MenuItem(menu, SWT.PUSH);
		saveItem.setText("&Save");
		
		saveItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.getDashboard().save();
			}
		});
		
		MenuItem loadItem = new MenuItem(menu, SWT.PUSH);
		loadItem.setText("&Load");
		
		loadItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.loadDashboard("dashboard.dat");
			}
		});
	}
}