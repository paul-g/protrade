package src.domain;

import java.util.List;

public class Tournament extends EventMarketBetfair{
	private String name;
	//private List<Match> matches;
	
	public Tournament(String name) {
		super();
		this.name = name;
	}
	
	public Tournament (String name, List<EventMarketBetfair> matches) {
		super();
		this.name = name;
		this.children = matches;
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
