package src.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import src.Main;
import src.demo.handler.ExchangeAPI;
import src.demo.handler.GlobalAPI;
import src.demo.handler.ExchangeAPI.Exchange;
import src.demo.util.APIContext;
import src.demo.util.Display;
import src.demo.util.InflatedMarketPrices;
import src.demo.util.InflatedMarketPrices.InflatedPrice;
import src.demo.util.InflatedMarketPrices.InflatedRunner;
import src.domain.EventMarketBetfair;
import src.domain.MarketBetfair;
import src.domain.EventBetfair;
import src.domain.Match;
import src.domain.Tournament;
import src.exceptions.LoginFailedException;
import src.generated.exchange.BFExchangeServiceStub.Market;
import src.generated.exchange.BFExchangeServiceStub.Runner;
import src.generated.global.BFGlobalServiceStub.BFEvent;
import src.generated.global.BFGlobalServiceStub.EventType;
import src.generated.global.BFGlobalServiceStub.GetEventsResp;
import src.generated.global.BFGlobalServiceStub.MarketSummary;
import src.utils.MatchUtils;

public class BetfairConnectionHandler {
	protected static APIContext apiContext = new APIContext();
	private static final String TENNIS_EVENT_NAME = "TENNIS";
	private static Logger log = Logger.getLogger(BetfairConnectionHandler.class);
	
	public static void login(String username, String password) throws LoginFailedException{
		try {
			GlobalAPI.login(apiContext, username, password);
		}
		catch (Exception e){
			throw new LoginFailedException(e.getMessage());
		}		
	}
	
	public static void logout() throws Exception{
		GlobalAPI.logout(apiContext);
	}
	
	private static void printEvents(int level, EventMarketBetfair event) {
	      String msg = "";
	      for(int i = 0 ; i < level; i++)
	        msg += "\t";
	      msg += event.toString() + " with id - " + event.getBetfairId();
	      System.out.println(msg);
	      if (level > 0) {
	    	  //System.out.println(getMarketOdds((Match)event));
	      }
	      for (EventMarketBetfair e : event.getChildren()) {
	        printEvents(level + 1, e);
	      }
	}	
	
	private static void printTours(Tournament tournament) {
	      log.info(tournament.toString());
	      for (Match m : tournament.getMatches())
	    	  log.info("\t" + m.toString() + " --or-- " + m.getEventBetfair().getName());
	}	
	
	public static List<Tournament> getTournamentsData() {
		List<Tournament> tournaments = new ArrayList<Tournament>();
		
		// get list of events and markets
		List<EventMarketBetfair> events = new ArrayList<EventMarketBetfair>();		
		try {
			int tennisEventID = getTennisEventID();	
			events = getEventData(tennisEventID);			
		} catch (Exception e){
			log.info("Failed to get Tennis Event ID - " + e.getMessage());
		}
		// filter data to get tournaments and matches only
		List<EventBetfair> tours = new ArrayList<EventBetfair>();
		for (EventMarketBetfair emb: events) {
			if (emb instanceof EventBetfair) {
				Tournament newTournament = new Tournament(emb.getName(), (EventBetfair)emb);
				filterMatches(newTournament);
				tournaments.add(newTournament);
			}
		}
		/*
		log.info("Go into printTours, when tournaments has " + tournaments.size() + " elements");
		for(Tournament t: tournaments)
			printTours(t);
		*/	
		return tournaments;		
	}
	
	// returns the event id of tennis
	private static int getTennisEventID() throws Exception{
		int eventId = -1;		
		EventType[] types = GlobalAPI.getActiveEventTypes(apiContext);		
		for (EventType et : types) {
			if (et.getName().toUpperCase().compareTo(TENNIS_EVENT_NAME) == 0)
				eventId = et.getId();
		}		
		if (eventId == -1)
			throw new Exception("No event id obtained");
		return eventId;
	}	
	
	// returns a list of events and markets of the given event
	public static List<EventMarketBetfair> getEventData(int eventId) {
		List<EventMarketBetfair> tournaments = new ArrayList<EventMarketBetfair>();		
		try {	
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, eventId);			
			// add the list of possible events
			BFEvent[] events = resp.getEventItems().getBFEvent();			
			if (events == null) {
				events = new BFEvent[] {};
			} else {
				// we remove Coupons, since they don't contain markets
				ArrayList<BFEvent> nonCouponEvents = new ArrayList<BFEvent>(events.length);
				for(BFEvent e: events) {
					if(!e.getEventName().equals("Coupons")) {
						nonCouponEvents.add(e);
					}
				}
				events = (BFEvent[]) nonCouponEvents.toArray(new BFEvent[]{});
			}
			for (BFEvent e : events) {
				EventBetfair newTournament= new EventBetfair(e.getEventName(), e.getEventId());
				newTournament.getChildren().addAll(getEventData(e.getEventId()));				
				tournaments.add(newTournament);
			}						
			// add the list of possible markets
			MarketSummary[] markets = resp.getMarketItems().getMarketSummary();
			if (markets == null) {
				markets = new MarketSummary[] {};
			}
			for (MarketSummary ms : markets) {
				tournaments.add(new MarketBetfair(ms.getMarketName(), ms.getMarketId()));
			}
		} catch (Exception e){
			log.info("Error getting list of tournaments - " + e.getMessage());
		}
		return tournaments;
	}

	// filter events to get only the list of matches
	public static void filterMatches(Tournament tournament) {
		List<Match> matches = new ArrayList<Match>();
		matches = getMatches(matches, tournament.getEventBetfair());
		tournament.setMatches(matches);
	}
	
	public static List<Match> getMatches(List<Match> matches, EventBetfair eventBetfair) {
		for (EventMarketBetfair emb : eventBetfair.getChildren()) {
			if (emb instanceof EventBetfair) {
				if (isMatch((EventBetfair)emb)) {
					matches.add(getMatch((EventBetfair)emb));
				}
				else {
					matches = getMatches(matches, (EventBetfair)emb);
				}
			}
		}		
		return matches;
	}
	
	public static boolean isMatch(EventBetfair emb) {
		return MatchUtils.isMatch(emb.toString());	
	}
	
	public static Match getMatch(EventBetfair eb) {
		return new Match("", "", eb);
	}
		
	public static APIContext getApiContext() {
		return apiContext;
	}
}
