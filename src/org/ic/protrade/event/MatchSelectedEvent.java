package org.ic.protrade.event;

import org.ic.protrade.domain.match.Match;

public class MatchSelectedEvent {

    private Match match;

    public MatchSelectedEvent(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }
    
}
