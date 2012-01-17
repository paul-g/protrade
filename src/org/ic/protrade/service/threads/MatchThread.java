package org.ic.protrade.service.threads;

import org.ic.protrade.data.match.LiveMatch;
import org.ic.protrade.data.match.Match;

public abstract class MatchThread extends StoppableThread {
	protected Match match;

	protected MatchThread() {
	}

	protected MatchThread(Match match) {
		this.match = match;
	}

	public void setMatch(LiveMatch match) {
		this.match = match;
	}
}
