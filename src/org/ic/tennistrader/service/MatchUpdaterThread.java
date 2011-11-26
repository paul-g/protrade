package org.ic.tennistrader.service;

import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;

public abstract class MatchUpdaterThread extends Thread{
    
	protected boolean stop = false;
	
	protected Match match;
	
    public void addEvent(RealMatch match){
        this.match = match;
    }
    
    @Override
    public void run(){
        final Logger log = Logger.getLogger(this.getClass());
        log.info("Starting " + this.getClass());        
        while(!stop){
            runBody();
        }        
        log.info("Stopped " + this.getClass());
    }
    
    protected abstract void runBody();
    
    public void setStop() {
    	this.stop = true;
    }
}