package org.ic.protrade.domain.markets;

import static org.junit.Assert.assertEquals;

import org.ic.protrade.domain.markets.MarketPrices;
import org.ic.protrade.domain.markets.MatchScore;
import org.ic.protrade.domain.markets.SetBettingMarketData;
import org.junit.Before;
import org.junit.Test;

public class SetBettingMarketDataTest {
	SetBettingMarketData setBettingMarketData;
	
	@Before
	public void setUp() {
		setBettingMarketData = new SetBettingMarketData();
	}
	
	@Test
	public void testZeroScores() {
		assertEquals(0, setBettingMarketData.getMatchScoreMarketData().size());
	}	
	
	@Test
	public void testAddSetScoreMarketPrices() {
		MatchScore matchScore = new MatchScore(2, 1);
		MarketPrices marketPrices = new MarketPrices();
		setBettingMarketData.addMatchScoreMarketPrices(matchScore, marketPrices);
		assertEquals(1, setBettingMarketData.getMatchScoreMarketData().size());
		assertEquals(marketPrices, setBettingMarketData.getMatchScoreMarketPrices(matchScore));
		assertEquals(marketPrices, setBettingMarketData.getMatchScoreMarketPrices(2, 1));
	}
}
