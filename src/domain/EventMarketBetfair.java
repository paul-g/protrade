package src.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class EventMarketBetfair {
	protected List<EventMarketBetfair> children; // size() > 0 if event, no children if market
	protected int betfairId;
	
	public EventMarketBetfair() {
		this.children = new ArrayList<EventMarketBetfair>();
	}
	
	public List<EventMarketBetfair> getChildren() {
		return this.children;
	}
	
	public void setChildren(List<EventMarketBetfair> c) {
		this.children = c;
	}
	
	public void addChild(EventMarketBetfair child) {
		this.children.add(child);
	}
	
	public void setBetfairId(int id) {
		this.betfairId = id;
	}
	
	public int getBetfairId() {
		return this.betfairId;
	}
}
