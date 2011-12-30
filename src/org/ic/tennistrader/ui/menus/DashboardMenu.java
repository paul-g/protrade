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

		makeNewMenu(menu);
		
		MenuItem saveItem = new MenuItem(menu, SWT.PUSH);
		saveItem.setText("&Save");
		
		saveItem.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.getDashboard().save();
			}
		});
		
		MenuItem saveAsItem = new MenuItem(menu, SWT.PUSH);
		saveAsItem.setText("&Save As");
		
		saveAsItem.addSelectionListener(new SelectionAdapter(){
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

	private void makeNewMenu(Menu menu) {
		MenuItem newItem = new MenuItem(menu, SWT.CASCADE);
		newItem.setText("&New");
		Menu newSubMenu = new Menu(newItem);
		newItem.setMenu(newSubMenu);
		
		MenuItem newEmpty = new MenuItem(newSubMenu, SWT.PUSH);
		newEmpty.setText("Empty");
		
		newEmpty.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardWindow.newDashboard();
			}
		});
		
		MenuItem newPredefined = new MenuItem(newSubMenu, SWT.PUSH);
		newPredefined.setText("Predefined");
		
		newPredefined.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutDialog ld = new LayoutDialog();
				ld.show();
				String s = ld.getSelection();
				if (s != null) {
					dashboardWindow.loadDashboard(s);
				}
			}
		});
	}
}