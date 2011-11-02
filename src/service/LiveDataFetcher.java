package src.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import src.domain.EventBetfair;
import src.domain.MOddsMarketData;
import src.domain.Match;
import src.ui.updatable.UpdatableWidget;

public class LiveDataFetcher {
    // one Betfair updater
    private static DataUpdater dataUpdater = null;
    // map of updatable widgets waiting for updates from the same betfair event id
    private static HashMap<Integer, List<UpdatableWidget>> listeners = new HashMap<Integer, List<UpdatableWidget>>();
    // map of updatable widgets waiting for updates from the same match from file
    private static HashMap<Match, List<UpdatableWidget>> fileListeners = new HashMap<Match, List<UpdatableWidget>>();
    private static Logger log = Logger.getLogger(LiveDataFetcher.class);
    private static Composite comp;
    private static boolean started = false;

    public static void registerLive(UpdatableWidget widget, Match match, Composite composite) {
        comp = composite;
        if (dataUpdater == null)
            dataUpdater = new BetfairDataUpdater();
            /*
            try {
                dataUpdater = new FracsoftReader(match, "fracsoft-data/fracsoft1.csv");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            */
        dataUpdater.addEvent(match);
        List<UpdatableWidget> widgets;
        if (listeners.containsKey(match.getEventBetfair().getBetfairId())) {
            widgets = listeners.get(match.getEventBetfair().getBetfairId());
        } else {
            widgets = new ArrayList<UpdatableWidget>();
        }
        widgets.add(widget);
        listeners.put(match.getEventBetfair().getBetfairId(), widgets);
        
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

    private static void startFromFile(Match match, String fileName,
            final Composite comp) {
        final DataUpdater fracsoftUpdater;
        try {
            fracsoftUpdater = new FracsoftReader(match, fileName);
            comp.getDisplay().timerExec(0, new Runnable() {
                @Override
                public void run() {
                    fracsoftUpdater.run();
                    if (!comp.isDisposed() )
                        comp.getDisplay().timerExec(1000, this);
                }
            });
        } catch(FileNotFoundException fnfe) {
            log.error("Fracsoft file to be opened not found");
        }
    }

    public static void start() {
        log.info("go to run thread");
        comp.getDisplay().timerExec(0, new Runnable() {
            @Override
            public void run() {
                dataUpdater.run();
                if (!comp.isDisposed() )
                    comp.getDisplay().timerExec(1000, this);
            }
        });
        log.info("after run in thread");
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
}
