package org.ic.tennistrader.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;

public class DisplayPanelTest {
	
    private Display display;
    private LoginShell ls;
    private Shell shell;
    private SWTBot bot;
    private DisplayPanel dp;
    private Match match;
    
    @Before
    public void setUp() {
    	match = new HistoricalMatch("data/test/fracsoft-reader/tso-fed.csv");
    	display = new Display();
    	shell = new Shell();
    	shell.setLayout(new FillLayout());
    	shell.setSize(0,0);
    	dp = new DisplayPanel(shell, SWT.NONE);
    	shell.open();
        bot = new SWTBot(shell);
    }

    @After  
    public void tearDown() {
        while (display.readAndDispatch()){
            // handle remaining work
        }
        display.dispose();
    }

    @Test
    public void addMatchView() throws Exception {
    	dp.handleMatchSelection(match);
    	SWTBotCTabItem item = bot.cTabItem(match.toString());
    }
}
