package org.ic.protrade.domain.markets;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.ic.protrade.data.market.connection.MarketPrices;
import org.ic.protrade.data.utils.Pair;
import org.junit.Before;
import org.junit.Test;

public class MarketPricesTest {
	MarketPrices marketPrices;

	@Before
	public void setUp() {
		marketPrices = new MarketPrices(new ArrayList<Pair<Double, Double>>(),
				new ArrayList<Pair<Double, Double>>());
	}

	@Test
	public void testBackPrices() {
		assertEquals(0, marketPrices.getBackPrices().size());
	}

	@Test
	public void testLayPrices() {
		assertEquals(0, marketPrices.getLayPrices().size());
	}
}
