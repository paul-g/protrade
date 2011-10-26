package src.domain;

import java.util.List;

public class Tournament {
	private String name;
	private EventBetfair eventBetfair;
	private List<Match> matches;
	
	public Tournament(String name, EventBetfair eb) {
		this.name = name;
		this.setEventBetfair(eb);
	}
	
	public Tournament (String name, List<Match> matches, EventBetfair eb) {
		this.name = name;
		this.matches = matches;
		this.setEventBetfair(eb);
	}

	public void addMatch(Match newMatch) {
		this.matches.add(newMatch);
	}
	
	public List<Match> getMatches() {
		return this.matches;
	}
	
	public void setMatches(List<Match> matches) {
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
