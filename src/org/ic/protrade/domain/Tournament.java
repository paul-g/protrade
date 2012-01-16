package org.ic.protrade.domain;

import java.util.List;

import org.ic.protrade.domain.markets.EventBetfair;
import org.ic.protrade.domain.match.LiveMatch;

public class Tournament {
	private String name;
	private EventBetfair eventBetfair;
	private List<LiveMatch> matches;
	
	public Tournament(String name, EventBetfair eb) {
		this.name = name;
		this.eventBetfair = eb;
	}
	
	public Tournament (String name, List<LiveMatch> matches, EventBetfair eb) {
		this.name = name;
		this.matches = matches;
		this.eventBetfair = eb;
	}

	public void addMatch(LiveMatch newMatch) {
		this.matches.add(newMatch);
	}
	
	public List<LiveMatch> getMatches() {
		return this.matches;
	}
	
	public void setMatches(List<LiveMatch> matches) {
		this.matches = matches;
	}
	
	public void setEventBetfair(EventBetfair eventBetfair) {
		this.eventBetfair = eventBetfair;
	}

	public EventBetfair getEventBetfair() {
		return eventBetfair;
	}
	
	public String toString() {
		return this.name;
	}
}
