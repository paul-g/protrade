package org.ic.tennistrader.ui;

import static org.junit.Assert.assertNotNull;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class UpperToolbarTest {

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
    public void test() {
        bot.shell("Tennis Trader");
        SWTBotToolbarDropDownButton profile = bot.toolbarDropDownButtonWithTooltip("Profile");
        SWTBotToolbarDropDownButton balance = bot.toolbarDropDownButtonWithTooltip("Balance");
        assertNotNull(profile);
        assertNotNull(balance);
    }
}
