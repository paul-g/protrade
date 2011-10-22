package src.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class EventMarketBetfair {
	protected List<EventMarketBetfair> children; // size() > 0 if event, no children if market
	
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
}
