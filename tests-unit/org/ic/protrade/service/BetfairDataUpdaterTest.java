package org.ic.protrade.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ic.protrade.data.market.EventBetfair;
import org.ic.protrade.data.match.LiveMatch;
import org.ic.protrade.service.threads.BetfairDataUpdaterThread;
import org.junit.Before;
import org.junit.Test;

public class BetfairDataUpdaterTest {
	BetfairDataUpdaterThread betfairDataUpdaterThread;

	/*
	 * Mockery context = new Mockery() {{
	 * setImposteriser(ClassImposteriser.INSTANCE); }}; final RealMatch match =
	 * context.mock(RealMatch.class); RealMatch match = mock(RealMatch.class);
	 * EventBetfair eb = context.mock(EventBetfair.class); context.checking(new
	 * Expectations() {{ one (betfairDataUpdaterThread).getEvents().size();
	 * will(returnValue(1)); }});
	 */

	@Before
	public void setUp() {
		betfairDataUpdaterThread = new BetfairDataUpdaterThread();
	}

	@Test
	public void testOneEvent() {
		EventBetfair eb = new EventBetfair("name", 1);
		LiveMatch match = new LiveMatch("pl1", "pl2", eb);
		betfairDataUpdaterThread.setMatch(match);
		assertEquals(1, betfairDataUpdaterThread.getEvents().size());
	}

	@Test
	public void testNEvents() {
		int n = 5;
		for (int i = 0; i < n; i++) {
			EventBetfair eb = new EventBetfair("name", i);
			LiveMatch match = new LiveMatch("pl1", "pl2", eb);
			betfairDataUpdaterThread.setMatch(match);
		}
		assertEquals(n, betfairDataUpdaterThread.getEvents().size());
	}

	@Test
	public void testStopCondition() {
		betfairDataUpdaterThread.start();
		assertTrue(betfairDataUpdaterThread.isAlive());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
		}
		betfairDataUpdaterThread.setStop();
		betfairDataUpdaterThread.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		assertFalse(betfairDataUpdaterThread.isAlive());
	}

}
