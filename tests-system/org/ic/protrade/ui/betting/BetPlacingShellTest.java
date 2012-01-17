package org.ic.protrade.ui.betting;

import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Player;
import org.ic.protrade.ui.DisplayTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BetPlacingShellTest extends DisplayTest{
	private Player firstPlayer, secondPlayer;
	private HistoricalMatch match;
	private BetPlacingShell betPlacingShell;
	
	@Before
    public void setUp() {        
//   	super.setUp();
//    	firstPlayer = new Player("Rafael", "Nadal");
//    	secondPlayer = new Player("Roger", "Federer");
//    	match = new HistoricalMatch(firstPlayer, secondPlayer);
//    	betPlacingShell = new BetPlacingShell(display, match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, "Bet details");
    }
	
	@Ignore
	@Test
	public void testInitialSetting() {
		//betPlacingShell = new BetPlacingShell(display, match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, "Bet details");
    	//assertFalse(betPlacingShell.getErrorLabel().isVisible());
	}

}
