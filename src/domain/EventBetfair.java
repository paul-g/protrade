package src.domain;

import java.util.List;

public class EventBetfair extends EventMarketBetfair{
	
	public EventBetfair(String name, int id) {
		super(name, id);
	}
	
	public EventBetfair (String name, List<EventMarketBetfair> matches, int id) {
		super(name, id);
		this.children = matches;
	}
		
	public String toString() {
		return this.name;
	}
}
