package org.ic.tennistrader.model.connection;

import static org.junit.Assert.*;
import java.util.List;
import org.ic.tennistrader.domain.Tournament;
import org.ic.tennistrader.domain.profile.ProfileData;
import org.junit.Test;

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
