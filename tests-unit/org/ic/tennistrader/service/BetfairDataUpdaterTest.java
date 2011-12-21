package org.ic.tennistrader.service;

import static org.junit.Assert.*;

import org.ic.tennistrader.domain.markets.EventBetfair;
import org.ic.tennistrader.domain.match.RealMatch;
import org.junit.Before;
import org.junit.Test;

public class BetfairDataUpdaterTest {
	BetfairDataUpdaterThread betfairDataUpdaterThread;
	/*
	Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};    
    	final RealMatch match = context.mock(RealMatch.class);
		RealMatch match = mock(RealMatch.class);
		EventBetfair eb = context.mock(EventBetfair.class);		
		context.checking(new Expectations() {{
            one (betfairDataUpdaterThread).getEvents().size();
                will(returnValue(1));
        }});		 
	*/
	
	@Before
	public void setUp() {
		betfairDataUpdaterThread = new BetfairDataUpdaterThread();
	}
	
	@Test
	public void testOneEvent() {		
		EventBetfair eb = new EventBetfair("name", 1);
		RealMatch match = new RealMatch("pl1", "pl2", eb);
		betfairDataUpdaterThread.setMatch(match);
		assertEquals(1, betfairDataUpdaterThread.getEvents().size());
	}
	
	@Test
	public void testNEvents() {		
		int n = 5;
		for (int i = 0; i < n; i++) {
			EventBetfair eb = new EventBetfair("name", i);
			RealMatch match = new RealMatch("pl1", "pl2", eb);
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
