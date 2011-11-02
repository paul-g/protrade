package src.service;

import src.domain.match.RealMatch;

public interface DataUpdater extends Runnable{
    public void addEvent(RealMatch match);
}
