package org.ic.tennistrader.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.markets.CompleteMarketData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.markets.SetBettingMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.model.betting.BetManager;
import org.ic.tennistrader.model.prediction.SetMarketManager;
import org.ic.tennistrader.service.threads.BetfairDataUpdaterThread;
import org.ic.tennistrader.service.threads.MatchRecorderThread;
import org.ic.tennistrader.service.threads.file_readers.FracsoftMatchOddsReader;
import org.ic.tennistrader.service.threads.file_readers.FracsoftReader;
import org.ic.tennistrader.service.threads.file_readers.FracsoftSetBettingReader;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.ic.tennistrader.utils.Pair;

public class DataManager {
	// one Betfair updater and many Fracsoft updater
	private static BetfairDataUpdaterThread dataUpdater = null;
	private static HashMap<Match, MatchRecorderThread> recordersMap = new HashMap<Match, MatchRecorderThread>();
	private static HashMap<Match, FracsoftReader<Pair<MOddsMarketData, Score>>> fileReaders = new HashMap<Match, FracsoftReader<Pair<MOddsMarketData, Score>>>();
	private static HashMap<Match, FracsoftReader<SetBettingMarketData>> fileSetReaders = new HashMap<Match, FracsoftReader<SetBettingMarketData>>();

	// map of updatable widgets waiting for updates from the same betfair event
	// id
	// private static HashMap<Integer, List<UpdatableWidget>> listeners = new
	// HashMap<Integer, List<UpdatableWidget>>();
	// map of updatable widgets waiting for updates from the same match from
	// file
	private static ConcurrentHashMap<Match, List<UpdatableWidget>> listeners = new ConcurrentHashMap<Match, List<UpdatableWidget>>();
	private static Logger log = Logger.getLogger(DataManager.class);
	private static boolean started = false;

	private static final List<Listener> generalEventListeners = new ArrayList<Listener>();

	public static void registerForMatchUpdate(final UpdatableWidget widget,
			final Match match) {
		if (match.isFromFile())
			registerFromFile(widget, match);
		else
			registerLive(widget, (RealMatch) match);

		if (recordersMap.get(match) == null) {
			// MatchRecorderThread mrt = new MatchRecorderThread(match);
			// recordersMap.put(match, mrt);
			// mrt.start();
		}
	}

	public void addGeneralEventlListener(Listener listener) {
		generalEventListeners.add(listener);
	}

	public static void unregister(final UpdatableWidget widget,
			final Match match) {
		if (match.isFromFile())
			unregisterFromFile(widget, match);
		else
			unregisterLive(widget, (RealMatch) match);
	}

	private static void registerLive(final UpdatableWidget widget,
			final RealMatch match) {
		if (dataUpdater == null)
			dataUpdater = new BetfairDataUpdaterThread();
		dataUpdater.setMatch(match);
		List<UpdatableWidget> widgets;
		// add the widget as listener to the given match
		if (listeners.containsKey(match)) {
			widgets = listeners.get(match);
		} else {
			widgets = Collections
					.synchronizedList(new ArrayList<UpdatableWidget>());
			/*
			 * System.out.println("Going to call set betting fetcher");
			 * BetfairExchangeHandler.getSetBettingMarketData(match);
			 */
		}
		synchronized (widgets) {
			widgets.add(widget);
		}
		listeners.put(match, widgets);

		widget.setDisposeListener(new ThreadDisposeListener(widget, match));

		// start the thread
		if (!started) {
			started = true;
			start();
		}
	}

	private static void unregisterLive(UpdatableWidget widget, RealMatch match) {
		// System.out.println("Unregister live entered");
		List<UpdatableWidget> widgets = null;
		if (listeners.containsKey(match)) {
			widgets = listeners.get(match);
		}
		if (widgets != null) {
			synchronized (widgets) {
				widgets.remove(widget);
			}
			if (widgets.size() == 0)
				removeMatch(match);
		}

	}

	private static void removeMatch(RealMatch match) {
		listeners.remove(match);
		dataUpdater.removeEvent(match.getEventBetfair());
	}

	private static void registerFromFile(final UpdatableWidget widget,
			final Match match) {
		List<UpdatableWidget> widgets;
		boolean isNewMatch = !listeners.containsKey(match);
		if (isNewMatch)
			widgets = Collections
					.synchronizedList(new ArrayList<UpdatableWidget>());
		else
			widgets = listeners.get(match);
		synchronized (widgets) {
			widgets.add(widget);
		}
		listeners.put(match, widgets);
		widget.setDisposeListener(new ThreadDisposeListener(widget, match));
		if (isNewMatch) {
			startFromFile(match);
		}
	}

	private static void unregisterFromFile(UpdatableWidget widget, Match match) {
		// System.out.println("Unregister live entered");
		List<UpdatableWidget> widgets = null;
		if (listeners.containsKey(match)) {
			widgets = listeners.get(match);
		}
		if (widgets != null) {
			synchronized (widgets) {
				widgets.remove(widget);
			}
			if (widgets.size() == 0) {
				FracsoftReader<Pair<MOddsMarketData, Score>> fracsoftReaderThread = fileReaders
						.get(match);
				// removeMatch(match);
				fracsoftReaderThread.setStop();
				fracsoftReaderThread.interrupt();

				if (fileSetReaders.containsKey(match)) {
					FracsoftReader<SetBettingMarketData> reader = fileSetReaders
							.get(match);
					reader.setStop();
					reader.interrupt();
				}

				listeners.remove(match);
			}
		}
	}

	private static void start() {
		log.info("Started Betfair thread");
		dataUpdater.start();
		for (Listener l : generalEventListeners) {

		}
	}

	private static void startFromFile(Match match) {
		final FracsoftMatchOddsReader fracsoftUpdater;
		try {
			fracsoftUpdater = new FracsoftMatchOddsReader(match,
					match.getFilename());
			fileReaders.put(match, fracsoftUpdater);
			log.info("Started Fracsoft thread");
			fracsoftUpdater.start();

			if (match.getSetBettingFilename() != null) {
				FracsoftSetBettingReader reader = new FracsoftSetBettingReader(
						match, match.getSetBettingFilename());
				fileSetReaders.put(match, reader);
				reader.start();
			}

		} catch (FileNotFoundException fnfe) {
			log.error("Fracsoft file to be opened not found");
		}
	}

	/*
	 * public static void handleMatchUpdate(Match match){ if
	 * (match.isFromFile()){ List<UpdatableWidget> widgets =
	 * fileListeners.get(match); for (UpdatableWidget w : widgets) {
	 * w.handleUpdate(new MOddsMarketData()); } } else{
	 * 
	 * }
	 * 
	 * }
	 */
	public static void handleEvent(HashMap<Match, CompleteMarketData> data) {
		for (Match match : data.keySet()) {
			// TODO check if market closed - then display result of bets
			CompleteMarketData marketData = data.get(match);
			boolean isEndOfSet = false;
			if (marketData.getSetBettingMarketData().getMatchScoreMarketData()
					.size() > 0)
				isEndOfSet = SetMarketManager.isSetEnd(match,
						marketData.getSetBettingMarketData());
			if (listeners.containsKey(match)) {
				List<UpdatableWidget> widgets = listeners.get(match);
				synchronized (widgets) {
					Iterator<UpdatableWidget> i = widgets.iterator();
					while (i.hasNext()) {
						UpdatableWidget w = i.next();
						w.handleUpdate(marketData.getmOddsMarketData());
						if (isEndOfSet)
							w.handleBettingMarketEndOFSet();
					}
				}
				/*
				 * for (UpdatableWidget w : listeners.get(match)) {
				 * w.handleUpdate(marketData.getmOddsMarketData()); if
				 * (isEndOfSet) w.handleBettingMarketEndOFSet(); }
				 */
			}
		}
		/*
		 * Iterator<EventBetfair> i = data.keySet().iterator(); while
		 * (i.hasNext()) { EventBetfair eb = i.next(); //TODO check if market
		 * closed - then display result of bets List<UpdatableWidget> widgets =
		 * listeners.get(eb.getBetfairId()); for (UpdatableWidget w : widgets)
		 * w.handleUpdate(data.get(eb).getmOddsMarketData()); }
		 */
	}

	public static void handleFileEvent(Match match,
			Pair<MOddsMarketData, Score> dataScore) {
		match.setScore(dataScore.second());
		// System.out.println("Market status - " +
		// dataScore.first().getMatchStatus());
		BetManager.updateMarketAvailableMatches(match, dataScore.first());
		if (dataScore.first().getMatchStatus().toLowerCase().equals("closed"))
			BetManager.setBetsOutcome(match);
		if (listeners.containsKey(match)) {
			List<UpdatableWidget> widgets = listeners.get(match);
			synchronized (widgets) {
				Iterator<UpdatableWidget> i = widgets.iterator();
				while (i.hasNext()) {
					i.next().handleUpdate(dataScore.first());
				}
			}
			/*
			 * for (UpdatableWidget w : widgets) {
			 * w.handleUpdate(dataScore.first()); }
			 */
		}
	}

	public static void handleSetBettingFileEvent(Match match,
			SetBettingMarketData marketData) {
		if (marketData.getMatchScoreMarketData().size() > 0
				&& SetMarketManager.isSetEnd(match, marketData)) {
			if (listeners.containsKey(match)) {
				List<UpdatableWidget> widgets = listeners.get(match);
				synchronized (widgets) {
					Iterator<UpdatableWidget> i = widgets.iterator();
					while (i.hasNext()) {
						i.next().handleBettingMarketEndOFSet();
					}
				}
				/*
				 * for (UpdatableWidget w : widgets) {
				 * w.handleBettingMarketEndOFSet(); }
				 */
			}
		}
	}

	public static void handleEndOfFile(Match match) {
		BetManager.setBetsOutcome(match);
	}

	public static void stopAllThreads() {
		if (dataUpdater != null) {
			dataUpdater.setStop();
			dataUpdater.interrupt();
		}

		for (FracsoftReader<Pair<MOddsMarketData, Score>> fr : fileReaders
				.values()) {
			fr.setStop();
			fr.interrupt();
		}

		for (FracsoftReader<SetBettingMarketData> fr : fileSetReaders.values()) {
			fr.setStop();
			fr.interrupt();
		}

		for (MatchRecorderThread mrt : recordersMap.values()) {
			mrt.setStop();
			mrt.interrupt();
		}
	}

	public static void setPlaybackSpeed(Match match, int updatesPerSecond) {
		if (fileReaders.containsKey(match))
			fileReaders.get(match).setUpdatesPerSecond(updatesPerSecond);
		if (fileSetReaders.containsKey(match))
			fileSetReaders.get(match).setUpdatesPerSecond(updatesPerSecond);
	}
}