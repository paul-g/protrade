package org.ic.tennistrader.model.connection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.List;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.Tournament;
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
				MOddsMarketData data = BetfairExchangeHandler.getMarketOdds(t.getEventBetfair());
				assertNotNull(data);
				for (RealMatch m : t.getMatches()) {
					data = BetfairExchangeHandler.getMarketOdds(m.getEventBetfair());
					assertNotNull(data);
				}
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}
}
