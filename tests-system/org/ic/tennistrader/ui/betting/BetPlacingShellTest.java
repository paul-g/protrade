package org.ic.tennistrader.ui.betting;

import static org.junit.Assert.*;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.ui.DisplayTest;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
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
