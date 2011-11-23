package org.ic.tennistrader.event;

import org.ic.tennistrader.domain.match.Match;

public class MatchSelectedEvent {

    private Match match;

    public MatchSelectedEvent(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }
    
}
