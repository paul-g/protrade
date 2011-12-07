package org.ic.tennistrader.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.junit.Before;
import org.junit.Test;

public class LowerToolbarTest extends DisplayTest {
	
	private MainWindow mw;
	private SWTBot bot;
	private SWTBot toolbot;
	private LowerToolBar ltb;

	@Before
	public void setUp(){
		super.setUp();
		mw = new MainWindow(display);
		bot = new SWTBot(mw.show());
		ltb = mw.getLowerToolBar();
		toolbot = new SWTBot(ltb.getToolbar());
	}
	
	@Test
	public void existenceCheck() {
		// First check
		assertNotNull(ltb);
	}
	
	@Test
	public void widgetInternetConnectionCheck() {
		// Internet connection widget checks
        SWTBotToolbarButton connection = bot.toolbarButtonWithTooltip("Internet Connection");
		boolean test = ltb.isInternetReachable();
        assertNotNull(connection);
        assertNotNull(connection.widget.getImage());
        assertEquals(test,true);
	}
	
	@Test
	public void widgetMemoryCheck() {
		// Memory usage widget check - SWTBot does not yet support ProgressBar
        SWTBotToolbarButton memory = toolbot.toolbarButtonWithTooltip("Internet Connection");
//        SWTBotLabel memory = toolbot.label("Memory Usage");
        assertNotNull(memory);
	}

}
