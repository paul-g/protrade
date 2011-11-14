package org.ic.tennistrader.ui;

import static org.junit.Assert.assertNotNull;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LowerToolbarTest {

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
    public void test() throws Exception {
        bot.shell("Tennis Trader");
        SWTBotToolbarButton connection = bot.toolbarButtonWithTooltip("Internet Connection");
        assertNotNull(connection);
        assertNotNull(connection.widget.getImage());
    }
}