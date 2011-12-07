package org.ic.tennistrader.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.*;


import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

public class LowerToolbarTest extends DisplayTest {
	
	private MainWindow mw;
	private SWTBot bot;
	private LowerToolBar ltb;

	@Before
	public void setUp(){
		super.setUp();
		mw = new MainWindow(display);
		bot = new SWTBot(mw.show());
		ltb = mw.getLowerToolBar();
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void widgetMemoryCheck() {
		// Memory usage widget check - SWTBot does not yet support ProgressBar
		Matcher<Widget> matcher = allOf(withTooltip("MemoryBar"));
        SWTBotLabel memory = new SWTBotLabel((Label) bot.widget(matcher));
        assertNotNull(memory);
	}

}
