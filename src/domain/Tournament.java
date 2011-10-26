package src.domain;

import java.util.List;

public class Tournament extends EventMarketBetfair{
	private String name;
	//private List<Match> matches;
	
	public Tournament(String name, int id) {
		super();
		this.name = name;
		this.betfairId = id;
	}
	
	public Tournament (String name, List<EventMarketBetfair> matches, int id) {
		super();
		this.name = name;
		this.children = matches;
		this.betfairId = id;
	}
	
	/*
	public void addMatch(Match newMatch) {
		this.matches.add(newMatch);
	}
	
	public List<Match> getMatches() {
		return this.matches;
	}
	
	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}
	*/
	
	public String toString() {
		return this.name;
	}
}
