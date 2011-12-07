package org.ic.tennistrader.ui.betting;

import static org.junit.Assert.assertEquals;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.model.BetManager;
import org.ic.tennistrader.ui.DisplayTest;
import org.ic.tennistrader.ui.betting.BetDisplayInfo;
import static org.ic.tennistrader.utils.Pair.pair;
import org.junit.Before;
import org.junit.Test;

public class BetDisplayInfoTest extends DisplayTest{
    private BetDisplayInfo betDisplayInfo;    
    private Match match;
    
    @Before
    public void setUp() {      
    	super.setUp();        
        Player firstPlayer = new Player("Rafael", "Nadal");
        Player secondPlayer = new Player("Roger", "Federer");
        match = new HistoricalMatch(firstPlayer, secondPlayer);        
        betDisplayInfo = new BetDisplayInfo(comp, firstPlayer, secondPlayer);
    }
    
    @Test
    public void noBet() {
        assertEquals(0, betDisplayInfo.getBets().keySet().size());
    }
    
    @Test
    public void oneBet() {
        Bet bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5,
                10.0));
        Label betLabel = new Label(comp, SWT.None);
        betDisplayInfo.addBet(bet, betLabel);
        assertEquals(1, betDisplayInfo.getBets().keySet().size());
        double firstWinnerProfit = BetManager.getBetProfit(bet, BetManager
                .isBetSuccessful(bet, PlayerEnum.PLAYER1));
        double secondWinnerProfit = BetManager.getBetProfit(bet, BetManager
                .isBetSuccessful(bet, PlayerEnum.PLAYER2));
        betDisplayInfo.setPlayerWinnerProfits(firstWinnerProfit,
                secondWinnerProfit);
        assertEquals(betDisplayInfo.getFirstPlayerWinnerProfit(), firstWinnerProfit, Math.pow(10, -5));
        assertEquals(betDisplayInfo.getSecondPlayerWinnerProfit(), secondWinnerProfit, Math.pow(10, -5));
    }
    
}
