package org.ic.protrade.service.threads;

import java.util.ArrayList;
import java.util.List;

import org.ic.protrade.data.market.EventBetfair;

public class BetfairUpdaterEvents {
	private final List<EventBetfair> events;

	public BetfairUpdaterEvents() {
		events = new ArrayList<EventBetfair>();
	}

	public synchronized List<EventBetfair> getEvents() {
		return this.events;
	}

	public synchronized void addEvent(EventBetfair eb) {
		if (!events.contains(eb)) {
			this.events.add(eb);
		}
	}

	public void removeEvent(EventBetfair eventBetfair) {
		this.events.remove(eventBetfair);
	}
}
