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
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");
		shell.setMenuBar(menuBar);

		// Edit menu

		// About menu

	}
}
