package org.ic.tennistrader.domain.markets;

import static org.junit.Assert.assertEquals;

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
		setBettingMarketData.addSetScoreMarketPrices(matchScore, marketPrices);
		assertEquals(1, setBettingMarketData.getMatchScoreMarketData().size());
		assertEquals(marketPrices, setBettingMarketData.getSetScoreMarketPrices(matchScore));
		assertEquals(marketPrices, setBettingMarketData.getSetScoreMarketPrices(2, 1));
	}
}
