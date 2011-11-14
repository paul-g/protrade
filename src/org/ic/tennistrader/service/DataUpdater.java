package org.ic.tennistrader.service;

import org.ic.tennistrader.domain.match.RealMatch;

public abstract class DataUpdater extends Thread{
    public abstract void addEvent(RealMatch match);
}