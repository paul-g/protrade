package org.ic.tennistrader.service;

import org.ic.tennistrader.domain.match.RealMatch;

public abstract class DataUpdater extends Thread{
	protected boolean stop = false;
	
    public abstract void addEvent(RealMatch match);
    
    public abstract void setStop();
}