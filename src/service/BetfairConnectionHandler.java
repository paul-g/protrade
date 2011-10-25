package src.service;

import java.util.ArrayList;
import java.util.List;

import src.demo.handler.GlobalAPI;
import src.demo.util.APIContext;
import src.demo.util.Display;
import src.domain.EventMarketBetfair;
import src.domain.Match;
import src.domain.Tournament;
import src.exceptions.LoginFailedException;
import src.generated.global.BFGlobalServiceStub.BFEvent;
import src.generated.global.BFGlobalServiceStub.EventType;
import src.generated.global.BFGlobalServiceStub.GetEventsResp;
import src.generated.global.BFGlobalServiceStub.MarketSummary;

public class BetfairConnectionHandler {
	private static APIContext apiContext = new APIContext();
	private static final String TENNIS_EVENT_NAME = "TENNIS";
	
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
	      msg += event.toString();
	      System.out.println(msg);
	      for (EventMarketBetfair e : event.getChildren()) {
	        printEvents(level + 1, e);
	      }
	}
	
	
	public static List<Tournament> getTournamentsData() {			
		////List<Tournament> tournaments = new ArrayList<Tournament>();
		List<EventMarketBetfair> events = new ArrayList<EventMarketBetfair>();;
		
		try {
			int tennisEventID = getTennisEventID();	
			events = getEventData(tennisEventID);
			//for (EventMarketBetfair emb : events)
				//tournaments.add( (Tournament) emb );
			
		} catch (Exception e){
			
		}
		
		List<Tournament> tours = new ArrayList<Tournament>();
		for (EventMarketBetfair emb: events) {
			if (emb instanceof Tournament) {
				Tournament newTournament = (Tournament) emb;
				filterMatches(newTournament);
				
				tours.add(newTournament);
			}
		}
		for(Tournament t: tours)
			printEvents(0, t);
		
		
		
		/*
		try {
			int tennisEventID = getTennisEventID();	
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, tennisEventID);			
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
				Tournament newTournament= new Tournament(e.getEventName());
				newTournament.getChildren().addAll(getMatchesData(e.getEventId()));
				//newTournament.setChildren(getMatchesData(e.getEventId()));
				
				tournaments.add(newTournament);
			}		 
			
			/*
			// add the list of possible markets
			MarketSummary[] markets = resp.getMarketItems().getMarketSummary();
			if (markets == null) {
				markets = new MarketSummary[] {};
			}
			for (MarketSummary ms : markets) {
				tournaments.add(new Tournament(ms.getMarketName()));
			}		
			*/	/*
		} catch (Exception e){
			System.out.println("Error getting list of tournaments - " + e.getMessage());
		}
		*/
		
		return tours;		
	}
	
	
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
				Tournament newTournament= new Tournament(e.getEventName());
				//newTournament.getChildren().addAll(getMatchesData(e.getEventId()));
				//newTournament.setChildren(getMatchesData(e.getEventId()));
				newTournament.getChildren().addAll(getEventData(e.getEventId()));
				
				tournaments.add(newTournament);
			}		 
			
			
			// add the list of possible markets
			MarketSummary[] markets = resp.getMarketItems().getMarketSummary();
			if (markets == null) {
				markets = new MarketSummary[] {};
			}
			for (MarketSummary ms : markets) {
				tournaments.add(new Match(ms.getMarketName(), "snd player"));
			}		
				
		} catch (Exception e){
			System.out.println("Error getting list of tournaments - " + e.getMessage());
		}
		return tournaments;
	}

	public static void filterMatches(Tournament tournament) {
		List<Match> matches = new ArrayList<Match>();
		matches = getMatches(matches, tournament);
		tournament.setChildren(new ArrayList<EventMarketBetfair>());
		tournament.getChildren().addAll(matches);
	}
	
	public static List<Match> getMatches(List<Match> matches, Tournament tournament) {
		for (EventMarketBetfair emb : tournament.getChildren()) {
			if (isMatch(emb))
				matches.add(getMatchData(emb));
			else if (emb instanceof Tournament)
				matches = getMatches(matches, (Tournament)emb);
		}
		
		return matches;
	}
	
	public static boolean isMatch(EventMarketBetfair emb) {
		return ( (emb instanceof Tournament) && 
				 ((Tournament)emb).toString().contains(" v ") );	
	}
	
	public static Match getMatchData(EventMarketBetfair emb) {
		return new Match(((Tournament)emb).toString(), "");
	}
	
	
	/*
	public static List<Match> getMatchesData(int tournamentEventId) {
		List<Match> matches = new ArrayList<Match>();
		try {
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, tournamentEventId);
			BFEvent[] events = resp.getEventItems().getBFEvent();
			if (events == null) {
				events = new BFEvent[] {};
			} else {
				// we remove Coupons, since they don't contain markets
				ArrayList<BFEvent> nonCouponEvents = new ArrayList<BFEvent>(
						events.length);
				for (BFEvent e : events) {
					if (!e.getEventName().equals("Coupons")) {
						nonCouponEvents.add(e);
					}
				}
				events = (BFEvent[]) nonCouponEvents.toArray(new BFEvent[] {});
			}
			for (BFEvent e : events) {
				Match newMatch = new Match(e.getEventName(), "");
				
				matches.add(newMatch);
			}

			/*
			 * // add the list of possible markets MarketSummary[] markets =
			 * resp.getMarketItems().getMarketSummary(); if (markets == null) {
			 * markets = new MarketSummary[] {}; } for (MarketSummary ms :
			 * markets) { tournaments.add(new Tournament(ms.getMarketName())); }
			 */	/*
		} catch (Exception e){
			System.out.println("Error getting list of tournaments - " + e.getMessage());
		}	
		return matches;
	}
	*/
	
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
	
	public static APIContext getApiContext() {
		return apiContext;
	}
}
