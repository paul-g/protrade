package org.ic.protrade.model.prediction;

import static org.junit.Assert.*;
import static org.ic.protrade.utils.Pair.pair;

import org.ic.protrade.domain.markets.MarketPrices;
import org.ic.protrade.domain.markets.MatchScore;
import org.ic.protrade.domain.markets.SetBettingMarketData;
import org.ic.protrade.domain.match.HistoricalMatch;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.Player;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.prediction.SetMarketManager;
import org.junit.Before;
import org.junit.Test;

public class SetMarketManagerTest {
	private Match match;
	private SetBettingMarketData marketData;
	
	@Before
	public void setUp() {
		match = new HistoricalMatch(new Player("Roger", "Federer"), new Player("Rafael", "Nadal"));
		marketData = new SetBettingMarketData();		
		setMarketData();
	}
	
	@Test
	public void testNewMatch() {
		assertEquals(0, match.getImpossibleScores().size());
		assertEquals(-1, match.getCurrentSet());
		assertFalse(SetMarketManager.isSetEnd(match, marketData));
		assertEquals(1, match.getCurrentSet());
		assertNull(SetMarketManager.getLastImpossibleScore(match, marketData));
	}
	
	@Test
	public void testNewSetEnd() {
		assertEquals(0, match.getImpossibleScores().size());
		assertEquals(-1, match.getCurrentSet());
		assertFalse(SetMarketManager.isSetEnd(match, marketData));
		assertEquals(1, match.getCurrentSet());
		assertEquals(1, match.getImpossibleScores().size());
		assertNull(SetMarketManager.getLastImpossibleScore(match, marketData));
		
		MatchScore matchScore;
		MarketPrices marketPrices;
		matchScore = new MatchScore(2, 0);
		marketPrices = new MarketPrices();
		marketPrices.addBackPrice(pair(1000.0, 100.0));
		marketData.addMatchScoreMarketPrices(matchScore, marketPrices);
		
		assertTrue(SetMarketManager.isSetEnd(match, marketData));
		assertEquals(2, match.getCurrentSet());
		assertEquals(1, match.getImpossibleScores().size());
		
		assertEquals(matchScore, SetMarketManager.getLastImpossibleScore(match, marketData));
		assertEquals(2, match.getImpossibleScores().size());
		assertEquals(PlayerEnum.PLAYER2, SetMarketManager.getSetWinner(matchScore));
	}
	
	
	private void setMarketData() {
		MatchScore matchScore;
		MarketPrices marketPrices;
		matchScore = new MatchScore(2, 0);
		marketPrices = new MarketPrices();
		marketPrices.addBackPrice(pair(1.2, 100.0));
		marketPrices.addBackPrice(pair(1.1, 123.3));		
		marketData.addMatchScoreMarketPrices(matchScore, marketPrices);
		
		matchScore = new MatchScore(2, 1);
		marketPrices = new MarketPrices();
		marketPrices.addBackPrice(pair(1.5, 100.0));
		marketPrices.addBackPrice(pair(1.2, 123.3));		
		marketData.addMatchScoreMarketPrices(matchScore, marketPrices);
		
		matchScore = new MatchScore(0, 2);
		marketPrices = new MarketPrices();
		marketPrices.addBackPrice(pair(998.0, 100.0));		
		marketData.addMatchScoreMarketPrices(matchScore, marketPrices);
		
		matchScore = new MatchScore(1, 2);
		marketPrices = new MarketPrices();
		marketPrices.addBackPrice(pair(38.0, 100.0));		
		marketData.addMatchScoreMarketPrices(matchScore, marketPrices);
	}	
}
