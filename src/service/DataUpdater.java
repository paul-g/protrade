package src.service;

import src.domain.Match;

public interface DataUpdater extends Runnable{
    public void addEvent(Match match);
}
