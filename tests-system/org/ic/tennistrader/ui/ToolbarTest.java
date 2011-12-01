package org.ic.tennistrader.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ToolbarTest {

	private Display display;
	private MainWindow mw;
	private SWTBot bot;

	@Before
	public void setUp(){
		display = new Display();
		mw = new MainWindow(display);
		bot = new SWTBot(mw.show());
	}

	@After
	public void putDown() {
		display.dispose();
	}

	@Test
	public void widgetUpperLeftCheck() {
		// Left corner widgets
		UpperToolBar utb = new UpperToolBar(mw);
		if (!utb.openPreferencesWindow()) assertNotNull(null);
		SWTBotToolbarDropDownButton widget_menu = bot.toolbarDropDownButtonWithTooltip("Widget Menu");
		SWTBotToolbarDropDownButton play_menu = bot.toolbarDropDownButtonWithTooltip("Play from a file");
		assertNotNull(widget_menu);
		assertNotNull(play_menu);
		assertNotNull(utb);
	}
	
	@Test
	public void widgetUpperRightCheck() {
		// Right corner widgets
		SWTBotToolbarDropDownButton profile = bot.toolbarDropDownButtonWithTooltip("Profile");
		SWTBotToolbarDropDownButton balance = bot.toolbarDropDownButtonWithTooltip("Balance");
		assertNotNull(profile);
		assertNotNull(balance);
	}
	
	@Test
	public void widgetLowerToolbarCheck() {
		// Lower tool bar
		LowerToolBar ltb = new LowerToolBar(mw);
		boolean test = ltb.isInternetReachable();
        SWTBotToolbarButton connection = bot.toolbarButtonWithTooltip("Internet Connection");
        assertNotNull(connection);
        assertNotNull(connection.widget.getImage());
        assertNotNull(ltb);
        assertEquals(test,true);
	}
}
