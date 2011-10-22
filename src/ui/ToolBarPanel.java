package src.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolBarPanel {

	public ToolBarPanel (Shell shell) {

		// Setting span throughout the columns
		GridData gridData = new GridData();
		gridData.horizontalSpan = ((GridLayout) shell.getLayout()).numColumns;

		// Setting up the toolbar with the placeholders - due to the number of columns
		final ToolBar toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		// Main menu
		final ToolItem itemMainMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
		itemMainMenu.setText("Main");
		itemMainMenu.setToolTipText("Main program operations");
		final Menu mainDropDownMenu = new Menu(shell,SWT.POP_UP);
		new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Open");
		new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Save");
		new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Save As...");
		new MenuItem(mainDropDownMenu,SWT.PUSH).setText("Exit");	  
		itemMainMenu.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.ARROW) {
					Rectangle bounds = itemMainMenu.getBounds();
					Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
					mainDropDownMenu.setLocation(point);
					mainDropDownMenu.setVisible(true);
				}
			}
		});

		// Edit menu
		final ToolItem itemEditMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
		itemEditMenu.setText("Edit");
		itemEditMenu.setToolTipText("Edit options");
		final Menu editDropDownMenu = new Menu(shell,SWT.POP_UP);
		new MenuItem(editDropDownMenu,SWT.PUSH).setText("Undo");
		new MenuItem(editDropDownMenu,SWT.PUSH).setText("Redo");
		new MenuItem(editDropDownMenu,SWT.PUSH).setText("Find");	  
		itemEditMenu.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.ARROW) {
					Rectangle bounds = itemEditMenu.getBounds();
					Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
					editDropDownMenu.setLocation(point);
					editDropDownMenu.setVisible(true);
				}
			}
		});

		// About menu
		final ToolItem itemAboutMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
		itemAboutMenu.setText("About");
		itemAboutMenu.setToolTipText("About the creators");
		final Menu aboutDropDownMenu = new Menu(shell,SWT.POP_UP);
		new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Credits");
		new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Help");
		new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Update");	  
		itemAboutMenu.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.ARROW) {
					Rectangle bounds = itemAboutMenu.getBounds();
					Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
					aboutDropDownMenu.setLocation(point);
					aboutDropDownMenu.setVisible(true);
				}
			}
		});

		toolbar.pack();
	}

}
