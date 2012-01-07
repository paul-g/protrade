package org.ic.tennistrader.model.betting;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.model.betting.BetManager;
import org.ic.tennistrader.utils.Pair;
import static org.ic.tennistrader.utils.Pair.pair;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class BetManagerTest {
    private Player firstPlayer;
	private Player secondPlayer;
    private HistoricalMatch match;
    
    @Before
    public void setUp() {
    	firstPlayer = new Player("Rafael", "Nadal");
    	secondPlayer = new Player("Roger", "Federer");
    	match = new HistoricalMatch(firstPlayer, secondPlayer);
        //Bet bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5, 10.0));
    	BetManager.setMatchedBets(new ArrayList<Bet>());
        BetManager.setUnmatchedBets(new ArrayList<Bet>());
        BetManager.setMatchMarketData(new ConcurrentHashMap<Match, VirtualBetMarketInfo>());
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testBetPlacing() {
        BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
        assertEquals(0, BetManager.getMatchedBets().size());
        assertEquals(1, BetManager.getUnmatchedBets().size());
        for (int i = 0; i < 5; i++) {
        	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
        	assertEquals(0, BetManager.getMatchedBets().size());
            assertEquals(i+2, BetManager.getUnmatchedBets().size());
        }
    }
    
    @Test
    public void testBetProfit() {
    	Bet bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5, 10.0));
    	assertEquals(15.0, BetManager.getBetProfit(bet, true), Math.pow(10, -5));
    	assertEquals(-10.0, BetManager.getBetProfit(bet, false), Math.pow(10, -5));
    	bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, pair(2.5, 10.0));
    	assertEquals(10.0, BetManager.getBetProfit(bet, true), Math.pow(10, -5));
    	assertEquals(-15.0, BetManager.getBetProfit(bet, false), Math.pow(10, -5));
    }
    
    @Test
    public void testUnmatchedBet() {
    	MOddsMarketData data = new MOddsMarketData();
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testMatchedBet() {
    	MOddsMarketData data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
    	/*new MOddsMarketData();
    	ArrayList<Pair<Double, Double>> pl1Back = new ArrayList<Pair<Double, Double>>();
    	pl1Back.add(pair(2.5, 10.0));
    	data.setPl1Back(pl1Back);
    	*/
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testUpdatingMarketBackPlayerOne() {
		MOddsMarketData data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 20.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 20.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testUpdatingMarketBackPlayerTwo() {
		MOddsMarketData data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.B, 2.5, 10.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER2, BetTypeEnum.B, 2.5, 20.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.B, 2.5, 20.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testUpdatingMarketLayPlayerOne() {
		MOddsMarketData data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.L, 2.5, 10.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, 2.5, 20.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.L, 2.5, 20.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testUpdatingMarketLayPlayerTwo() {
		MOddsMarketData data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 10.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 20.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 20.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testPartlyMatchBet() {
    	MOddsMarketData data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 10.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 30.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.B, 2.5, 30.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 20.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 30.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testUnmatchedBetMoreMatches() {
    	MOddsMarketData data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 10.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 30.0);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	
    	HistoricalMatch secondMatch = new HistoricalMatch(new Player("Rafael", "Nadal"), new Player("Roger", "Federer"));
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 30.0);
    	BetManager.updateMarketAvailableMatches(secondMatch, data);
    	assertEquals(0, BetManager.getMatchedBets().size());
    	assertEquals(1, BetManager.getUnmatchedBets().size());
    	
    	data = createMarket(PlayerEnum.PLAYER2, BetTypeEnum.L, 2.5, 30.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	assertEquals(1, BetManager.getMatchedBets().size());
    	assertEquals(0, BetManager.getUnmatchedBets().size());
    }
    
    @Test
    public void testMatchLiability() {
    	MOddsMarketData data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 50.0);
    	BetManager.updateMarketAvailableMatches(match, data);    	
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
    	assertEquals(10.0, BetManager.getMatchLiability(match), Math.pow(10, -5));    	
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);
    	assertEquals(20.0, BetManager.getMatchLiability(match), Math.pow(10, -5));
    	
    	data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.L, 4.0, 50.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, 4.0, 10.0);
    	assertEquals(30.0, BetManager.getMatchLiability(match), Math.pow(10, -5));
    }
    
    @Test
    public void testTotalLiability() {
    	MOddsMarketData data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 50.0);
    	BetManager.updateMarketAvailableMatches(match, data);    	
    	data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.L, 4.0, 50.0);
    	BetManager.updateMarketAvailableMatches(match, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);  	
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, 2.5, 10.0);    	
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, 4.0, 10.0);
    	
    	HistoricalMatch newMatch = new HistoricalMatch(new Player(), new Player());
    	data = createMarket(PlayerEnum.PLAYER1, BetTypeEnum.L, 4.0, 50.0);
    	BetManager.updateMarketAvailableMatches(newMatch, data);
    	BetManager.placeBet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, 4.0, 10.0);
    	assertEquals(60.0, BetManager.getTotalLiabiltiy(), Math.pow(10, -5));
    }

	private MOddsMarketData createMarket(PlayerEnum player, BetTypeEnum betType, double odds, double value) {
		MOddsMarketData data = new MOddsMarketData();
    	ArrayList<Pair<Double, Double>> marketValues = new ArrayList<Pair<Double, Double>>();
    	marketValues.add(pair(odds, value));
    	if (betType.equals(BetTypeEnum.B)) {
            if (player.equals(PlayerEnum.PLAYER1))
                data.setPl1Back(marketValues);
            else
            	data.setPl2Back(marketValues);
        } else {
            if (player.equals(PlayerEnum.PLAYER1))
            	data.setPl1Lay(marketValues);
            else
            	data.setPl2Lay(marketValues);
        }        
		return data;
	}
	
	@Test
	public void testValidPrices() {
		assertFalse(BetManager.isValidPrice(0.2));
		assertFalse(BetManager.isValidPrice(-3.0));
		assertFalse(BetManager.isValidPrice(1.0));
		/*
		for (double i = 1.01; i <= 2.0; i += 0.01) {
			assertTrue("Wrong value: " + i, BetManager.isValidPrice(i));
		}
		*/
		assertTrue(BetManager.isValidPrice(1.01));
		assertTrue(BetManager.isValidPrice(1.06));
		assertFalse(BetManager.isValidPrice(2.01));
		assertTrue(BetManager.isValidPrice(2.02));	
		assertTrue(BetManager.isValidPrice(3.0));	
		assertFalse(BetManager.isValidPrice(3.01));
		assertTrue(BetManager.isValidPrice(3.65));	
	}

}
