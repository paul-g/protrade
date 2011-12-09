package org.ic.tennistrader.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MenuExampleTest {

    private Shell shell;
    private SWTBot bot;

    @Test
    public void emptyLoginFail() throws Exception {
        Display display = new Display();
        MenuExample me = new MenuExample(display);
        bot = new SWTBot(shell);
        SWTBotMenu s = bot.shell("Test").contextMenu("Test");
        //SWTBotMenu m = s.contextMenu("Test");
    }

    public static SWTBotMenu getSubMenuItem(final SWTBotMenu parentMenu,
            final String itemText) throws WidgetNotFoundException {

        MenuItem menuItem = UIThreadRunnable
                .syncExec(new WidgetResult<MenuItem>() {
                    public MenuItem run() {
                        Menu bar = parentMenu.widget.getMenu();
                        if (bar != null) {
                            for (MenuItem item : bar.getItems()) {
                                if (item.getText().equals(itemText)) {
                                    return item;
                                }
                            }
                        }
                        return null;
                    }
                });

        if (menuItem == null) {
            throw new WidgetNotFoundException("MenuItem \"" + itemText
                    + "\" not found.");
        } else {
            return new SWTBotMenu(menuItem);
        }
    }
}