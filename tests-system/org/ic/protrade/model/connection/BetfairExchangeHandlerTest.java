package org.ic.protrade.model.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.ic.protrade.data.MatchScore;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.market.connection.BetfairConnectionHandler;
import org.ic.protrade.data.market.connection.BetfairExchangeHandler;
import org.ic.protrade.data.market.connection.CompleteMarketData;
import org.ic.protrade.data.market.connection.SetBettingMarketData;
import org.ic.protrade.data.market.connection.Tournament;
import org.ic.protrade.data.match.LiveMatch;
import org.junit.Test;

public class BetfairExchangeHandlerTest extends BetfairConnectionTest {
	@Test
	public void testMarketOdds() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(tours);
			for (Tournament t : tours) {
				MOddsMarketData data;
				for (LiveMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getMatchOddsMarketData(m);
					assertNotNull(data);
					break;
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}

	@Test
	public void testSetBetting() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(tours);
			for (Tournament t : tours) {
				LiveMatch match = new LiveMatch("Federer", "Djokovic",
						t.getEventBetfair());
				SetBettingMarketData data = BetfairExchangeHandler
						.getSetBettingMarketData(match);
				assertNotNull(data);
				for (LiveMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getSetBettingMarketData(m);
					assertNotNull(data);
					break;
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}

	@Test
	public void testCompleteMarket() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(tours);
			for (Tournament t : tours) {
				LiveMatch match = new LiveMatch("Federer", "Djokovic",
						t.getEventBetfair());
				CompleteMarketData data = BetfairExchangeHandler
						.getCompleteMarketData(match);
				assertNotNull(data);
				for (LiveMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getCompleteMarketData(m);
					assertNotNull(data);
					break;
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}

	@Test
	public void testGetMatchScore() {
		String runnerName = "Federer 2 - 0";
		MatchScore matchScore = MatchScore.getMatchScore(runnerName);
		assertEquals(2, matchScore.getFirstPlayerScore());
		assertEquals(0, matchScore.getSecondPlayerScore());
		assertEquals("Federer", matchScore.getFirstPlayerLastName());
	}
}
