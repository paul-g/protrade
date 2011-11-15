package org.ic.tennistrader.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public class LiveDataFetcher {
    // one Betfair updater and many Fracsoft updater
    private static DataUpdater dataUpdater = null;
    private static List<DataUpdater> fileUpdaters = new ArrayList<DataUpdater>();
    // map of updatable widgets waiting for updates from the same betfair event id
    private static HashMap<Integer, List<UpdatableWidget>> listeners = new HashMap<Integer, List<UpdatableWidget>>();
    // map of updatable widgets waiting for updates from the same match from file
    private static HashMap<Match, List<UpdatableWidget>> fileListeners = new HashMap<Match, List<UpdatableWidget>>();
    private static Logger log = Logger.getLogger(LiveDataFetcher.class);
    private static Composite comp;
    private static boolean started = false;

    public static void registerLive(UpdatableWidget widget, RealMatch match, Composite composite) {
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
        // start the thread
        if (!started){
            started = true;
            start();
        }
    }
    
    public static void registerFromFile(UpdatableWidget widget, Match match, String fileName, final Composite comp) {
        List<UpdatableWidget> widgets;
        boolean isNewMatch = !fileListeners.containsKey(match);
        if (isNewMatch)
            widgets = new ArrayList<UpdatableWidget>();
        else
            widgets = fileListeners.get(match);
        widgets.add(widget);
        fileListeners.put(match, widgets);
        if(isNewMatch) {
            startFromFile(match, fileName, comp);
        }
    }

    public static void start() {
        log.info("Start Betfair thread");
        dataUpdater.start();
        /*
        comp.getDisplay().timerExec(0, new Runnable() {
            @Override
            public void run() {
                dataUpdater.run();
                if (!comp.isDisposed() )
                    comp.getDisplay().timerExec(5000, this);
            }
        });
        */
    }
    
    private static void startFromFile(Match match, String fileName,
            final Composite comp) {
    	log.info("Start Fracsoft thread");
        final DataUpdater fracsoftUpdater;
        try {
            fracsoftUpdater = new FracsoftReader(match, fileName);
            fileUpdaters.add(fracsoftUpdater);
            fracsoftUpdater.start();
            /*
            comp.getDisplay().timerExec(0, new Runnable() {
                @Override
                public void run() {
                    fracsoftUpdater.run();
                    if (!comp.isDisposed() )
                        comp.getDisplay().timerExec(1000, this);
                }
            });
            */
        } catch(FileNotFoundException fnfe) {
            log.error("Fracsoft file to be opened not found");
        }
        log.info("After started Fracsoft thread");
    }

    public static void handleEvent(HashMap<EventBetfair, MOddsMarketData> data) {
    	//log.info("Handle Betfair event-----------------------------------");
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
    	if (dataUpdater != null)
    		dataUpdater.setStop();
    	for (DataUpdater du : fileUpdaters) {
    		du.setStop();
    	}
    }
}