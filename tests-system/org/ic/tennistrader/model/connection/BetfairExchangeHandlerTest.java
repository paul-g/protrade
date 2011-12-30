package org.ic.tennistrader.model.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.List;
import org.ic.tennistrader.domain.Tournament;
import org.ic.tennistrader.domain.markets.CompleteMarketData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.markets.MatchScore;
import org.ic.tennistrader.domain.markets.SetBettingMarketData;
import org.ic.tennistrader.domain.match.RealMatch;
import org.junit.Test;

public class BetfairExchangeHandlerTest extends BetfairConnectionTest{
	@Test
	public void testMarketOdds() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(tours);
			for (Tournament t : tours) {
				MOddsMarketData data = BetfairExchangeHandler.getMatchOddsMarketData(t.getEventBetfair());
				assertNotNull(data);
				for (RealMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getMatchOddsMarketData(m.getEventBetfair());
					assertNotNull(data);
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
				RealMatch match = new RealMatch("Federer", "Djokovic", t.getEventBetfair());
				SetBettingMarketData data = BetfairExchangeHandler.getSetBettingMarketData(match);
				assertNotNull(data);
				for (RealMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getSetBettingMarketData(m);
					assertNotNull(data);
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
				RealMatch match = new RealMatch("Federer", "Djokovic", t.getEventBetfair());
				CompleteMarketData data = BetfairExchangeHandler.getCompleteMarketData(match);
				assertNotNull(data);
				for (RealMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getCompleteMarketData(m);
					assertNotNull(data);
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
