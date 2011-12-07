package org.ic.tennistrader.ui;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainWindowTest extends DisplayTest{
    private MainWindow mw;
    private SWTBot bot;

    @Before
    public void setUp(){
    	super.setUp();
        mw = new MainWindow(display);
        bot = new SWTBot(mw.show());
    }
    @Test
    public void test(){
        SWTBotShell shell = bot.shell("Tennis Trader");
        assertNotNull(shell);
    }
}