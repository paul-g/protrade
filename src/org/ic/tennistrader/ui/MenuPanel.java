package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuPanel {

	private MainWindow mw;
	private Shell shell;

	/** The constructor and setter of the menu of the application */
	public MenuPanel (MainWindow mw) {
		
		this.mw = mw;
		shell = mw.getShell();

		// Main menu of the application
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		// File button
		MenuItem appMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		appMenuHeader.setText("&Application");
		shell.setMenuBar(menuBar);
		
		// File drop down menu
		Menu appMenu = new Menu(shell, SWT.DROP_DOWN);
	    appMenuHeader.setMenu(appMenu);

	    MenuItem appSettingsItem = new MenuItem(appMenu, SWT.PUSH);
	    appSettingsItem.setText("&Settings");
	    appSettingsItem.addSelectionListener(new TabListener("Settings"));

	    MenuItem appExitItem = new MenuItem(appMenu, SWT.PUSH);
	    appExitItem.setText("E&xit");
	    appExitItem.addSelectionListener(new ExitListener());
		
		// About button
		MenuItem aboutMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuHeader.setText("A&bout");
		shell.setMenuBar(menuBar);
		
		// About drop down menu
		Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
	    aboutMenuHeader.setMenu(aboutMenu);

	    MenuItem aboutHelpItem = new MenuItem(aboutMenu, SWT.PUSH);
	    aboutHelpItem.setText("&Help");
	    aboutHelpItem.addSelectionListener(new TabListener("Help"));
	    
	    MenuItem aboutCreatorsItem = new MenuItem(aboutMenu, SWT.PUSH);
	    aboutCreatorsItem.setText("&Creators");
	    aboutCreatorsItem.addSelectionListener(new TabListener("Creators"));
	}

	private class ExitListener implements  SelectionListener {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {}
		@Override
		public void widgetSelected(SelectionEvent e) {
			shell.close();
		}
	}
	
	private class TabListener implements SelectionListener {
		private String name;
		
		public TabListener (String name) {
			this.name = name;
		}
		
		public void widgetDefaultSelected(SelectionEvent e) {
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			mw.addNewTab(name);
		}
	}
}