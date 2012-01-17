package org.ic.protrade.domain;

import static org.ic.protrade.data.utils.Pair.pair;
import static org.junit.Assert.assertEquals;

import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Player;
import org.ic.protrade.data.match.PlayerEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.junit.Before;
import org.junit.Test;

public class BetTest {
	private HistoricalMatch match;

	@Before
	public void setUp() {
		match = new HistoricalMatch(new Player(), new Player());
	}

	@Test
	public void testPossibleLiability() {
		Bet bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5,
				20.7));
		assertEquals(20.7, bet.getPossibleLiability(), Math.pow(10, -5));
		assertEquals(20.7, bet.getCurrentLiability(), Math.pow(10, -5));

		bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, pair(3.7, 10.0));
		assertEquals(27, bet.getPossibleLiability(), Math.pow(10, -5));
		assertEquals(27, bet.getCurrentLiability(), Math.pow(10, -5));
	}

	@Test
	public void testCurrentLiability() {
		Bet bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.B, pair(2.5,
				20.7));
		bet.setUnmatchedValue(9.6);
		assertEquals(20.7, bet.getPossibleLiability(), Math.pow(10, -5));
		assertEquals(11.1, bet.getCurrentLiability(), Math.pow(10, -5));

		bet = new Bet(match, PlayerEnum.PLAYER1, BetTypeEnum.L, pair(3.7, 10.0));
		bet.setUnmatchedValue(3.0);
		assertEquals(27, bet.getPossibleLiability(), Math.pow(10, -5));
		assertEquals(18.9, bet.getCurrentLiability(), Math.pow(10, -5));
	}
}
