package org.ic.protrade.domain.markets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ic.protrade.data.MatchScore;
import org.junit.Before;
import org.junit.Test;

public class MatchScoreTest {
	MatchScore matchScore;

	@Before
	public void setUp() {
		matchScore = new MatchScore(2, 1);
	}

	@Test
	public void testFirstPlayerScore() {
		assertEquals(2, matchScore.getFirstPlayerScore());
	}

	@Test
	public void testSecondPlayerScore() {
		assertEquals(1, matchScore.getSecondPlayerScore());
	}

	@Test
	public void testFirstPlayerLastName() {
		matchScore.setFirstPlayerLastName("Federer");
		assertEquals("Federer", matchScore.getFirstPlayerLastName());
	}

	@Test
	public void testEquals() {
		MatchScore secondMatchScore = new MatchScore(2, 1);
		assertTrue(matchScore.equals(secondMatchScore));
		secondMatchScore = new MatchScore(1, 2);
		assertFalse(matchScore.equals(secondMatchScore));
	}

	@Test
	public void testHashCode() {
		assertEquals(21, matchScore.hashCode());
	}
}