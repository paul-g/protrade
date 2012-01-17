package org.ic.protrade.model.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.ic.protrade.data.market.connection.BetfairConnectionHandler;
import org.ic.protrade.data.market.connection.ProfileData;
import org.ic.protrade.data.market.connection.Tournament;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class BetfairConnectionHandlerTest extends BetfairConnectionTest{	
	@Test
	public void testGetTournamentsData() {
		if (setUp) {
			List<Tournament> tours = BetfairConnectionHandler
					.getTournamentsData();
			assertNotNull(tours);
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}
	
	@Test
	public void testProfileData() {
		if (setUp) {
			try {
				ProfileData profileData = BetfairConnectionHandler
						.getProfileData();
				assertEquals(0, profileData.getAusAccountFunds().getBalance(),
						Math.pow(10, -5));
				assertEquals(10, profileData.getUkAccountFunds().getBalance(),
						Math.pow(10, -5));
				assertEquals(username, profileData.getUsername());
			} catch (Exception e) {
				fail("Profile data could not be retrieved form Betfair");
			}
		} else {
			fail(SETUP_FAILED_MESSAGE);
		}
	}	
}
