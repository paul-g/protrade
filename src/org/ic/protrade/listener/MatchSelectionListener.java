package org.ic.protrade.listener;

import org.ic.protrade.domain.match.Match;

public interface MatchSelectionListener {
    public void handleMatchSelection(Match match);

}
