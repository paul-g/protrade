package org.ic.protrade.ui.betting;

import static org.junit.Assert.assertEquals;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.ic.protrade.domain.Bet;
import org.ic.protrade.domain.match.HistoricalMatch;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.Player;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.betting.BetManager;
import org.ic.protrade.ui.DisplayTest;
import org.ic.protrade.ui.betting.BetDisplayInfo;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

import static org.ic.protrade.utils.Pair.pair;

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
        betDisplayInfo = new BetDisplayInfo(shell, firstPlayer, secondPlayer);
    }
    
    @Test
    public void noBet() {
        assertEquals(0, betDisplayInfo.getBets().keySet().size());
    }
    
    @Test
    public void oneBet() {
        Bet bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5,
                10.0));
        Label betLabel = new Label(shell, SWT.None);
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
