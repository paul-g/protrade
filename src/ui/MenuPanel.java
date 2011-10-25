package src.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuPanel {


	/** The constructor and setter of the menu of the application */
	public MenuPanel (Shell shell) {

		// Main menu of the application
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		// File button
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");
		shell.setMenuBar(menuBar);
		
		// File drop down menu
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);

	    MenuItem fileCreateItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileCreateItem.setText("New &Widget");

	    MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("E&xit");

		// Edit button
		MenuItem editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		editMenuHeader.setText("&Edit");
		shell.setMenuBar(menuBar);
		
		// Edit drop down menu
		Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
	    editMenuHeader.setMenu(editMenu);

	    MenuItem fileEditItem = new MenuItem(editMenu, SWT.PUSH);
	    fileEditItem.setText("E&dit");

		
		// About button
		MenuItem aboutMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuHeader.setText("&About");
		shell.setMenuBar(menuBar);
		
		// About drop down menu
		Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
	    aboutMenuHeader.setMenu(aboutMenu);

	    MenuItem aboutEditItem = new MenuItem(aboutMenu, SWT.PUSH);
	    aboutEditItem.setText("&Creators");
	}
}