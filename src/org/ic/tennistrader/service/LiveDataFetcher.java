package org.ic.tennistrader.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public class LiveDataFetcher {
    // one Betfair updater and many Fracsoft updater
    private static BetfairDataUpdater dataUpdater = null;    
    private static HashMap<Match, FracsoftReader> fileReaders = new HashMap<Match, FracsoftReader>();    
    // map of updatable widgets waiting for updates from the same betfair event id
    private static HashMap<Integer, List<UpdatableWidget>> listeners = new HashMap<Integer, List<UpdatableWidget>>();
    // map of updatable widgets waiting for updates from the same match from file
    private static HashMap<Match, List<UpdatableWidget>> fileListeners = new HashMap<Match, List<UpdatableWidget>>();
    private static Logger log = Logger.getLogger(LiveDataFetcher.class);
    private static Composite comp;
    private static boolean started = false;

    public static void registerLive(final UpdatableWidget widget, final RealMatch match, Composite composite) {
        comp = composite;
        if (dataUpdater == null)
            dataUpdater = new BetfairDataUpdater();
        dataUpdater.addEvent(match);
        List<UpdatableWidget> widgets;
        // add the widget as listener to the given match
        if (listeners.containsKey(match.getEventBetfair().getBetfairId())) {
            widgets = listeners.get(match.getEventBetfair().getBetfairId());
        } else {
            widgets = new ArrayList<UpdatableWidget>();
        }
        widgets.add(widget);
        listeners.put(match.getEventBetfair().getBetfairId(), widgets);
        widget.setDisposeListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				unregisterLive(widget, match);
			}        	
        });
        // start the thread
        if (!started){
            started = true;
            start();
        }
    }
    
    protected static void unregisterLive(UpdatableWidget widget, RealMatch match) {
    	System.out.println("Unregister live entered");
    	List<UpdatableWidget> widgets = null;
    	if (listeners.containsKey(match.getEventBetfair().getBetfairId())) {
            widgets = listeners.get(match.getEventBetfair().getBetfairId());
        }
    	if (widgets != null) {
    		widgets.remove(widget);
    		if (widgets.size() == 0)
    			removeMatch(match);
    	}
    	    	
	}

	private static void removeMatch(RealMatch match) {
		listeners.remove(match.getEventBetfair().getBetfairId());
		dataUpdater.removeEvent(match.getEventBetfair());
	}
	
	public static void registerFromFile(final UpdatableWidget widget, final Match match, String fileName, final Composite comp) {
        List<UpdatableWidget> widgets;
        boolean isNewMatch = !fileListeners.containsKey(match);
        if (isNewMatch)
            widgets = new ArrayList<UpdatableWidget>();
        else
            widgets = fileListeners.get(match);
        widgets.add(widget);
        fileListeners.put(match, widgets);
        widget.setDisposeListener(new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				unregisterFromFile(widget, match);
			}        	
        });
        if(isNewMatch) {
            startFromFile(match, fileName, comp);
        }
    }

    protected static void unregisterFromFile(UpdatableWidget widget, Match match) {
		// TODO Auto-generated method stub		
	}

	public static void start() {
        log.info("Started Betfair thread");
        dataUpdater.start();
    }
    
    private static void startFromFile(Match match, String fileName,
            final Composite comp) {    	
        final FracsoftReader fracsoftUpdater;
        try {
            fracsoftUpdater = new FracsoftReader(match, fileName);
            fileReaders.put(match, fracsoftUpdater);            
            log.info("Started Fracsoft thread");
            fracsoftUpdater.start();            
        } catch(FileNotFoundException fnfe) {
            log.error("Fracsoft file to be opened not found");
        }
    }

    public static void handleEvent(HashMap<EventBetfair, MOddsMarketData> data) {
        Iterator<EventBetfair> i = data.keySet().iterator();
        while (i.hasNext()) {
            EventBetfair eb = i.next();
            List<UpdatableWidget> widgets = listeners.get(eb.getBetfairId());
            for (UpdatableWidget w : widgets)
                w.handleUpdate(data.get(eb));
        }
    }

    public static void handleFileEvent(Match match, MOddsMarketData data) {
        if (fileListeners.containsKey(match)) {
            List<UpdatableWidget> widgets = fileListeners.get(match);
            for (UpdatableWidget w : widgets) {
                w.handleUpdate(data);
            }
        }
    }
    
    public static void stopAllThreads() {
    	if (dataUpdater != null) {
    		dataUpdater.setStop();
    		dataUpdater.interrupt();
    	}
    	for (FracsoftReader fr : fileReaders.values()) {
    		fr.setStop();
    		fr.interrupt();
    	}
    }
    
    public static void setPlaybackSpeed(Match match, int updatesPerSecond) {
    	FracsoftReader fracsoftReader = fileReaders.get(match);
    	fracsoftReader.setUpdatesPerSecond(updatesPerSecond);
    }
}