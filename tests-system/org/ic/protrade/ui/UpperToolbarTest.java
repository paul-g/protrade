package org.ic.protrade.ui;

import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.ic.protrade.ui.main.StandardWindow;
import org.ic.protrade.ui.toolbars.DashboardToolBar;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class UpperToolbarTest extends DisplayTest {

	private StandardWindow mw;
	private SWTBot bot;
	private SWTBot logbot;
	private SWTBot toolbot;
	private DashboardToolBar utb;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		mw = new StandardWindow(display);
		bot = new SWTBot(mw.show());
		utb = mw.getUpperToolBar();
		// logbot = new SWTBot(utb.getLoginToolBar());
		toolbot = new SWTBot(utb.getToolBar());

	}

	@Test
	public void existenceCheck() {
		// First check
		assertNotNull(utb);
	}

	@Test
	public void toolbarWidgetMenuCheck() {
		// New widget menu checks
		SWTBotToolbarDropDownButton wmbutton = toolbot
				.toolbarDropDownButtonWithTooltip("Widget Menu");
		assertNotNull(wmbutton);
		// wmbutton.click();
		// Matcher<MenuItem> match = withText("Match Navigator");
		// wmbutton.menuItem(match).click();
		// match = withText("Active Bets Display");
		// wmbutton.menuItem(match).click();
		// match = withText("Match Viewer");
		// wmbutton.menuItem(match).click();
		// match = withText("Player Statistics");
		// wmbutton.menuItem(match).click();
	}

	@Test
	public void toolbarPlayMenuCheck() {
		// Play menu checks
		SWTBotToolbarDropDownButton pmbutton = bot
				.toolbarDropDownButtonWithTooltip("Play from a file");
		assertNotNull(pmbutton);
	}

	@Ignore
	@Test
	public void toolbarProfileCheck() {
		// Profile button checks
		SWTBotToolbarDropDownButton profbutton = logbot
				.toolbarDropDownButtonWithTooltip("Profile");
		// assertEquals(utb.openProfileWindow(),true);
		// assertEquals(utb.openPreferencesWindow(),true);
		// assertEquals(utb.openProfileWindow(),false);
		// assertEquals(utb.openPreferencesWindow(),false);
		assertNotNull(profbutton);
	}

	@Test
	public void toolbarBalanceCheck() {
		// Balance button checks
		SWTBotToolbarDropDownButton balance = bot
				.toolbarDropDownButtonWithTooltip("Balance");
		assertNotNull(balance);
	}
}
