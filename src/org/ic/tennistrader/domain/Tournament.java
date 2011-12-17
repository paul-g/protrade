package org.ic.tennistrader.domain;

import java.util.List;

import org.ic.tennistrader.domain.match.RealMatch;

public class Tournament {
	private String name;
	private EventBetfair eventBetfair;
	private List<RealMatch> matches;
	
	public Tournament(String name, EventBetfair eb) {
		this.name = name;
		this.eventBetfair = eb;
	}
	
	public Tournament (String name, List<RealMatch> matches, EventBetfair eb) {
		this.name = name;
		this.matches = matches;
		this.eventBetfair = eb;
	}

	public void addMatch(RealMatch newMatch) {
		this.matches.add(newMatch);
	}
	
	public List<RealMatch> getMatches() {
		return this.matches;
	}
	
	public void setMatches(List<RealMatch> matches) {
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
