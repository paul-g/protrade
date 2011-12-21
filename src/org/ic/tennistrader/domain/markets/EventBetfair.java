package org.ic.tennistrader.domain.markets;

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
	
	public boolean equals(Object object){
		if (object == null) {
            return false;
		}
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        EventBetfair eb = (EventBetfair)object;
        return eb.getBetfairId() == this.betfairId;		
	}
	
	public int hashCode() {
		return this.betfairId;
	}
}
