package org.ic.protrade.event;

import org.ic.protrade.data.match.Match;

public class MatchSelectedEvent {

	private final Match match;

	public MatchSelectedEvent(Match match) {
		this.match = match;
	}

	public Match getMatch() {
		return match;
	}

}
