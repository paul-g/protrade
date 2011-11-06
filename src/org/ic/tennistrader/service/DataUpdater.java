package org.ic.tennistrader.service;

import org.ic.tennistrader.domain.match.RealMatch;

public interface DataUpdater extends Runnable{
    public void addEvent(RealMatch match);
}
