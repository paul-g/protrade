package src.ui;

import org.apache.log4j.Logger;
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

import src.Main;

public class ToolBarPanel{
  
    private ToolBar toolbar;
    
    private static Logger log = Logger.getLogger(ToolBarPanel.class);

	public ToolBarPanel (final MainWindow mainWindow) {
	    Shell shell = mainWindow.getShell();
	    // Setting span throughout the columns
		GridData gridData = new GridData();
		gridData.horizontalSpan = ((GridLayout) shell.getLayout()).numColumns;

		// Setting up the toolbar with the placeholders - due to the number of columns
		this.toolbar = new ToolBar (shell, SWT.FLAT | SWT.RIGHT);
        
		//final ToolBar toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
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
		itemMainMenu.addListener(SWT.Selection, new DropDownListener(itemMainMenu, mainDropDownMenu));

		// Edit menu
		final ToolItem itemEditMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
		itemEditMenu.setText("Edit");
		itemEditMenu.setToolTipText("Edit options");
		final Menu editDropDownMenu = new Menu(shell,SWT.POP_UP);
		new MenuItem(editDropDownMenu,SWT.PUSH).setText("Undo");
		new MenuItem(editDropDownMenu,SWT.PUSH).setText("Redo");
		new MenuItem(editDropDownMenu,SWT.PUSH).setText("Find");	  
		itemEditMenu.addListener(SWT.Selection, new DropDownListener(itemEditMenu, editDropDownMenu));

		// About menu
		final ToolItem itemAboutMenu = new ToolItem(toolbar,SWT.DROP_DOWN);
		itemAboutMenu.setText("About");
		itemAboutMenu.setToolTipText("About the creators");
		final Menu aboutDropDownMenu = new Menu(shell,SWT.POP_UP);
		new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Credits");
		new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Help");
		new MenuItem(aboutDropDownMenu,SWT.PUSH).setText("Update");	  
		itemAboutMenu.addListener(SWT.Selection, new DropDownListener(itemAboutMenu, aboutDropDownMenu));
		
		// Log out/profile menu
		final ToolItem profileItem = new ToolItem(toolbar,SWT.DROP_DOWN);
        // TODO: this is a slight hack
		profileItem.setText(Main.USER);
        profileItem.setToolTipText("Click to view your profile");
	    final Menu profileDropDown = new Menu(shell,SWT.POP_UP);
	    new MenuItem(profileDropDown,SWT.PUSH).setText("Log out");
	    new MenuItem(profileDropDown,SWT.PUSH).setText("Profile");
	    new MenuItem(profileDropDown,SWT.PUSH).setText("Preferences"); 
	    profileItem.addListener(SWT.Selection, new DropDownListener(profileItem, profileDropDown));
	    
	    // New widget menu
	    final ToolItem widgetItem = new ToolItem(toolbar,SWT.DROP_DOWN);
        widgetItem.setText("New Widget");
        widgetItem.setToolTipText("Click to view your profile");
        final Menu widgetDropDown = new Menu(shell,SWT.POP_UP);
        MenuItem matchNavigator = new MenuItem(widgetDropDown,SWT.PUSH);
        matchNavigator.setText("Match Navigator");
        matchNavigator.addListener(SWT.Selection, new Listener(){
            @Override
            public void handleEvent(Event arg0) {
              log.info("Opened match navigator");
              mainWindow.addMatchNavigator();
            }
            
          });
        
        MenuItem activeBets = new MenuItem(widgetDropDown,SWT.PUSH);
        activeBets.setText("Active Bets Display");
        activeBets.addListener(SWT.Selection, new Listener(){
          @Override
          public void handleEvent(Event arg0) {
            log.info("Opened an active bets display tab");
            mainWindow.addActiveBetsDisplay();
          }
          
        });
        
        new MenuItem(widgetDropDown,SWT.PUSH).setText("Match Statistics"); 
        widgetItem.addListener(SWT.Selection, new DropDownListener(widgetItem, widgetDropDown));
        
        
	    
		toolbar.pack();
	}
	
	private class DropDownListener implements Listener{

	  private ToolItem item; 
	  private Menu menu;
	  
	  public DropDownListener(ToolItem item, Menu menu){
	    this.item = item;
	    this.menu = menu;
	  }
	  
	  public void handleEvent(Event event) {
        if(event.detail == SWT.ARROW) {
            Rectangle bounds = item.getBounds();
            Point point = toolbar.toDisplay(bounds.x, bounds.y + bounds.height);
            menu.setLocation(point);
            menu.setVisible(true);
         }
     }
	  
	}

}
