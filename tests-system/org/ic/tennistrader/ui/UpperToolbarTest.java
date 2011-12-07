package org.ic.tennistrader.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.junit.Before;
import org.junit.Test;

public class UpperToolbarTest extends DisplayTest{
	private MainWindow mw;
	private SWTBot bot;
	private UpperToolBar utb;

	@Before
	public void setUp(){
		super.setUp();
		mw = new MainWindow(display);
		bot = new SWTBot(mw.show());
		utb = new UpperToolBar(mw);
	}

	@Test
	public void existenceCheck() {
		// First check
		assertNotNull(utb);
	}
	
	@Test
	public void toolbarWidgetMenuCheck() {
		// New widget menu checks
		SWTBotToolbarDropDownButton widget_menu = bot.toolbarDropDownButtonWithTooltip("Widget Menu");
		assertNotNull(widget_menu);
	}
	
	@Test
	public void toolbarPlayMenuCheck() {
		// Play menu checks
		SWTBotToolbarDropDownButton play_menu = bot.toolbarDropDownButtonWithTooltip("Play from a file");
		assertNotNull(play_menu);
	}
	
	@Test
	public void toolbarProfileCheck() {
		// Profile button checks
		SWTBotToolbarDropDownButton profile = bot.toolbarDropDownButtonWithTooltip("Profile");
		assertEquals(utb.openProfileWindow(),true);
		assertEquals(utb.openPreferencesWindow(),true);
		assertEquals(utb.openProfileWindow(),false);
		assertEquals(utb.openPreferencesWindow(),false);
		assertNotNull(profile);
	}
	
	@Test
	public void toolbarBalanceCheck() {
		// Balance button checks
		SWTBotToolbarDropDownButton balance = bot.toolbarDropDownButtonWithTooltip("Balance");
		assertNotNull(balance);
	}
}
