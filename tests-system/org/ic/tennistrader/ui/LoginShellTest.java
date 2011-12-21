package org.ic.tennistrader.ui;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.ic.tennistrader.Main;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginShellTest extends DisplayTest {    
    private LoginShell ls;
    private Shell shell;
    private SWTBot bot;
    private SWTBotButton loginButton;

    @Before
    public void setUp() {        
    	super.setUp();
        ls = new LoginShell(display);
        shell = ls.show();
        bot = new SWTBot(shell);
        loginButton = bot.button("Login");
    }

    @Test
    public void emptyLoginFail() throws Exception {
        loginButton.click();
        SWTBotLabel fail = bot.label(LoginShell.FAIL);
        assertNotNull(fail);

    }

    @Ignore
    @Test
    public void correctLoginSuccess() throws Exception {
        SWTBotText username = bot.text("username");
        username.setText(Main.getTestUsername());
        SWTBotText password = bot.text("password");
        password.setText(Main.getTestPassword());
        loginButton.click();
        SWTBotLabel success = bot.label(LoginShell.SUCCESS);
        assertNotNull(success);
    }

    @Test
    public void testAccount() throws Exception {

        // load the username
        Main.readAndDecryptConfigFile();

        SWTBotButton testButton = bot.button("Test");

        // startup the app
        TestListener tl = new TestListener();

        ls.addLoginSuccessListener(tl);

        testButton.click();

        SWTBotLabel success = bot.label(LoginShell.SUCCESS);
        assertNotNull(success);

        assertTrue(tl.isReached());
    }

    private class TestListener implements Listener {
        boolean reached = false;

        @Override
        public void handleEvent(Event arg0) {
            reached = true;
        }

        boolean isReached() {
            return reached;
        }
    }

}
