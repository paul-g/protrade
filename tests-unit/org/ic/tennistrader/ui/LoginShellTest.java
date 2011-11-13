package org.ic.tennistrader.ui;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.ic.tennistrader.Main;
import org.junit.Test;

public class LoginShellTest extends TestCase {
    
    @Test
    public void testEmptyLoginFail() throws Exception {
        final Display display = new Display();
        LoginShell ls = new LoginShell(display);
        final Shell shell = ls.showGui();
        final SWTBot bot = new SWTBot(shell);
        
        SWTBotButton loginButton = bot.button("Login");
        loginButton.click();
        SWTBotLabel fail = bot.label(LoginShell.FAIL);
        assertNotNull(fail);

        ls.dispose();
        display.dispose();
    }
    
    @Test
    public void testCorrectLoginSuccess() throws Exception {
        final Display display = new Display();
        final Shell shell = new LoginShell(display).showGui();
        final SWTBot bot = new SWTBot(shell);
        

        SWTBotButton loginButton = bot.button("Login");
        
        
        SWTBotText username = bot.text("username");
        username.setText("corina409");
        SWTBotText password = bot.text("password");
        password.setText("testpass1");
        
        loginButton.click();
        SWTBotLabel success = bot.label(LoginShell.SUCCESS);
        assertNotNull(success);
        
        display.dispose();
    }
    
    @Test
    public void testTestAccount() throws Exception{
        final Display display = new Display();
        final LoginShell ls = new LoginShell(display);
        final Shell shell = ls.showGui();
        final SWTBot bot = new SWTBot(shell);
        
        // load the username
        Main.readConfigFile();
        
        SWTBotButton testButton = bot.button("Test");
        
        // startup the app
        TestListener tl = new TestListener();
        
        ls.addLoginSuccessListener(tl);
        
        testButton.click();
        
        SWTBotLabel success = bot.label(LoginShell.SUCCESS);
        assertNotNull(success);
        
        assertTrue(tl.isReached());

    }
    
    class TestListener implements Listener{
        boolean reached = false;
        
        @Override
        public void handleEvent(Event arg0) {
           reached = true;
        }
            
         boolean isReached(){
             return reached;
         }
    }
    
}
