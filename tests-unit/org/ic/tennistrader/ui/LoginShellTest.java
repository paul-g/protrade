package org.ic.tennistrader.ui;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;

public class LoginShellTest extends TestCase {
    
    private final Display display = new Display();;
    private final Shell shell = new LoginShell(display).showGui();
    private final SWTBot bot = new SWTBot(shell);
    
    @Test
    public void testEmptyLoginFail() throws Exception {
        SWTBotPreferences.PLAYBACK_DELAY = 100;

        SWTBotButton loginButton = bot.button("Login");
        loginButton.click();
        SWTBotLabel fail = bot.label(LoginShell.FAIL);
        assertNotNull(fail);

        /*
         * SWTBotText userText = bot.textWithLabel("Username: "); SWTBotText
         * passwordText = bot.textWithLabel("Password: ");
         */
        /*
         * userText.setFocus(); userText.setText("Superman");
         * 
         * Assert.assertEquals(userText.getText(),"Superman");
         * 
         * passwordText.setFocus(); passwordText.setText("test123");
         * 
         * Assert.assertEquals(passwordText.getText(),"test123");
         * 
         * loginButton.setFocus(); loginButton.click();
         * 
         * 
         * userText.setFocus(); userText.setText("Favonius");
         * 
         * Assert.assertEquals(userText.getText(), "Favonius");
         * 
         * passwordText.setFocus(); passwordText.setText("abcd123");
         * 
         * Assert.assertEquals(passwordText.getText(), "abcd123");
         * 
         * loginButton.setFocus(); loginButton.click();
         */
    }

    @Test
    public void testCorrectLoginSuccess() {

        SWTBotButton loginButton = bot.button("Login");
        loginButton.click();
        
        SWTBotText userText = bot.textWithLabel("Username: ");
        SWTBotText passwordText = bot.textWithLabel("Password: ");
   
        SWTBotLabel success = bot.label(LoginShell.SUCCESS);
        assertNotNull(success);
    }

}
