package org.ic.tennistrader.service.threads;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;

public abstract class MatchThread extends StoppableThread {
    protected Match match;
    
    protected MatchThread() {}
    
    protected MatchThread(Match match){
        this.match=match;
    }

    public void setMatch(RealMatch match) {
        this.match = match;
    }
}
