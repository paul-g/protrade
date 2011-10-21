package src.domain;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
	private String name;
	private List<Match> matches;
	
	public Tournament(String name) {
		this.name = name;
		this.matches = new ArrayList<Match>();
	}
	
	public Tournament (String name, List<Match> matches) {
		this.name = name;
		this.matches = matches;
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
	
	public String toString() {
		return this.name;
	}

}
