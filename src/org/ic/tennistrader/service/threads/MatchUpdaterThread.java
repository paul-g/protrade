package org.ic.tennistrader.service.threads;

import org.ic.tennistrader.domain.match.Match;


public abstract class MatchUpdaterThread extends MatchThread{
    
    public MatchUpdaterThread(){
        
    }

    protected MatchUpdaterThread(Match match) {
        super(match);
    }
}