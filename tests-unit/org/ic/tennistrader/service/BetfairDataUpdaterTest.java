package org.ic.tennistrader.service;

import static org.junit.Assert.*;

import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.match.RealMatch;
import org.junit.Before;
import org.junit.Test;

public class BetfairDataUpdaterTest {
	BetfairDataUpdater betfairDataUpdater;
	/*
	Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};    
    	final RealMatch match = context.mock(RealMatch.class);
		RealMatch match = mock(RealMatch.class);
		EventBetfair eb = context.mock(EventBetfair.class);		
		context.checking(new Expectations() {{
            one (betfairDataUpdater).getEvents().size();
                will(returnValue(1));
        }});		 
	*/
	
	@Before
	public void setUp() {
		betfairDataUpdater = new BetfairDataUpdater();
	}
	
	@Test
	public void testOneEvent() {		
		EventBetfair eb = new EventBetfair("name", 1);
		RealMatch match = new RealMatch("pl1", "pl2", eb);
		betfairDataUpdater.addEvent(match);
		assertEquals(1, betfairDataUpdater.getEvents().size());
	}
	
	@Test
	public void testNEvents() {		
		EventBetfair eb = new EventBetfair("name", 1);
		int n = 5;
		for (int i = 0; i < n; i++) {
			RealMatch match = new RealMatch("pl1", "pl2", eb);
			betfairDataUpdater.addEvent(match);
		}
		assertEquals(n, betfairDataUpdater.getEvents().size());
	}
	
	@Test 
	public void testStopCondition() {
		betfairDataUpdater.start();
		assertTrue(betfairDataUpdater.isAlive());
		betfairDataUpdater.setStop();
		betfairDataUpdater.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		assertFalse(betfairDataUpdater.isAlive());
	}

}
