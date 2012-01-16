package org.ic.protrade.ui.betting;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;

import static org.ic.protrade.utils.Pair.pair;
import static org.junit.Assert.*;

import org.ic.protrade.domain.Bet;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.match.HistoricalMatch;
import org.ic.protrade.domain.match.Player;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.betting.BetManager;
import org.ic.protrade.ui.DisplayTest;
import org.ic.protrade.ui.betting.BetsDisplay;
import org.ic.protrade.utils.Pair;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class BetsDisplayTest extends DisplayTest{
	BetsDisplay betsDisplay;
	
	@Before
	public void setUp() {
		super.setUp();
		betsDisplay = new BetsDisplay(shell, SWT.None);
		BetsDisplay.setActiveBets(new ArrayList<Label>());
		BetsDisplay.setUnmatchedBetsLabels(new HashMap<Bet, Label>());
	}
	
	@Test
	public void testAddBet() {
		Player firstPlayer = new Player("Rafael", "Nadal");
        Player secondPlayer = new Player("Roger", "Federer");
        HistoricalMatch match = new HistoricalMatch(firstPlayer, secondPlayer);
		//Bet bet = new Bet(match,PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5, 10.0));
		BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
		//BetsDisplay.addBet(bet);
		assertEquals(1, BetsDisplay.getActiveBets().size());
		assertEquals(1, BetsDisplay.getUnmatchedBetsLabels().keySet().size());
	}
	
	@Test
	public void testUpdateUnmatchedBet() {		
		Player firstPlayer = new Player("Rafael", "Nadal");
        Player secondPlayer = new Player("Roger", "Federer");
        HistoricalMatch match = new HistoricalMatch(firstPlayer, secondPlayer);
        
        BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
        
		MOddsMarketData data = new MOddsMarketData();
    	ArrayList<Pair<Double, Double>> pl1Back = new ArrayList<Pair<Double, Double>>();
    	pl1Back.add(pair(2.5, 10.0));
    	data.setPl1Back(pl1Back);
    	BetManager.updateMarketAvailableMatches(match, data);
    	
    	assertEquals(0, BetsDisplay.getUnmatchedBetsLabels().size());
	}

}
