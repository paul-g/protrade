package org.ic.tennistrader.listener;

import org.ic.tennistrader.domain.match.Match;

public interface MatchSelectionListener {
    public void handleMatchSelection(Match match);
}
