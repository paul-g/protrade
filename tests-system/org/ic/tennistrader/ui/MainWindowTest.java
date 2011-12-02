package org.ic.tennistrader.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainWindowTest{
    
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
    public void tearDown(){
        while (display.readAndDispatch()){
            // handle remaining work
        }
        display.dispose();
    }

    @Test
    public void test(){
        SWTBotShell shell = bot.shell("Tennis Trader");
        assertNotNull(shell);
    }
}