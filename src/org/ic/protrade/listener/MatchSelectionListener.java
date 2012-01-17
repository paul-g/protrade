package org.ic.protrade.listener;

import org.ic.protrade.data.match.Match;

public interface MatchSelectionListener {
	public void handleMatchSelection(Match match);

}
