package src.service;

import java.util.ArrayList;
import java.util.List;

import src.demo.handler.ExchangeAPI;
import src.demo.handler.GlobalAPI;
import src.demo.handler.ExchangeAPI.Exchange;
import src.demo.util.APIContext;
import src.demo.util.Display;
import src.demo.util.InflatedMarketPrices;
import src.demo.util.InflatedMarketPrices.InflatedPrice;
import src.demo.util.InflatedMarketPrices.InflatedRunner;
import src.domain.EventMarketBetfair;
import src.domain.Match;
import src.domain.Tournament;
import src.exceptions.LoginFailedException;
import src.generated.exchange.BFExchangeServiceStub.Market;
import src.generated.exchange.BFExchangeServiceStub.Runner;
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
	      msg += event.toString() + " with id - " + event.getBetfairId();
	      System.out.println(msg);
	      if (level > 0) {
	    	  //System.out.println(getMarketOdds((Match)event));
	      }
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
		/*
		for(Tournament t: tours)
			printEvents(0, t);
		*/
		
		
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
				Tournament newTournament= new Tournament(e.getEventName(), e.getEventId());
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
				tournaments.add(new Match(ms.getMarketName(), "snd player", ms.getMarketId()));
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
		return new Match(((Tournament)emb).toString(), "", emb.getBetfairId());
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

	public static String getMarketOdds(Match m) {
		String msg = "";
		try {
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, m
					.getBetfairId());
			// add the list of possible events
			/*
			 * BFEvent[] events = resp.getEventItems().getBFEvent(); if (events
			 * == null) { events = new BFEvent[] {}; } else { // we remove
			 * Coupons, since they don't contain markets ArrayList<BFEvent>
			 * nonCouponEvents = new ArrayList<BFEvent>(events.length);
			 * for(BFEvent e: events) { if(!e.getEventName().equals("Coupons"))
			 * { nonCouponEvents.add(e); } } events = (BFEvent[])
			 * nonCouponEvents.toArray(new BFEvent[]{}); } for (BFEvent e :
			 * events) { Tournament newTournament= new
			 * Tournament(e.getEventName(), e.getEventId());
			 * //newTournament.getChildren
			 * ().addAll(getMatchesData(e.getEventId()));
			 * //newTournament.setChildren(getMatchesData(e.getEventId()));
			 * newTournament.getChildren().addAll(getEventData(e.getEventId()));
			 * tournaments.add(newTournament); }
			 */

			// add the list of possible markets
			MarketSummary[] markets = resp.getMarketItems().getMarketSummary();
			if (markets == null) {
				markets = new MarketSummary[] {};
			}
			MarketSummary marketOdds = null;
			for (MarketSummary ms : markets) {
				// tournaments.add(new Match(ms.getMarketName(), "snd player",
				// ms.getMarketId()));
				if (ms.getMarketName().equals("Match Odds")) {
					System.out.println("YES");
					marketOdds = ms;
				}
			}
			
			if (marketOdds != null) {
				Exchange selectedExchange = marketOdds.getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
				Market selectedMarket = ExchangeAPI.getMarket(selectedExchange, apiContext, marketOdds.getMarketId());
			
				InflatedMarketPrices prices = ExchangeAPI.getMarketPrices(selectedExchange, apiContext, selectedMarket.getMarketId());
				
				//Display.showMarket(selectedExchange, selectedMarket, prices);
				msg = showMarket(selectedExchange, selectedMarket, prices);
				
			}
			
		} catch (Exception e) {
		}
		return msg;
	}
	
	private static String showMarket(Exchange exch, Market m, InflatedMarketPrices prices){
		String msg = "";
		msg += ("Market: "+m.getName()+"("+m.getMarketId()+") on the "+exch+" exchange:") + "\n";
		msg += ("   Start time     : "+m.getMarketTime().getTime()) + "\n";
		msg += ("   Status         : "+m.getMarketStatus()) + "\n";
		msg += ("   Location       : "+m.getCountryISO3()) + "\n";
		msg += ("") + "\n";

		msg += ("Runners:") + "\n";
		for (InflatedRunner r: prices.getRunners()) {
			Runner marketRunner = null;
			
			for (Runner mr: m.getRunners().getRunner()) {
				if (mr.getSelectionId() == r.getSelectionId()) {
					marketRunner = mr;
					break;
				}
			}
			String bestLay = "";
			if (r.getLayPrices().size() > 0) {
				InflatedPrice p = r.getLayPrices().get(0);
				bestLay = String.format("%,10.2f %s @ %,6.2f", p.getAmountAvailable(), prices.getCurrency(), p.getPrice());
			}
			
			String bestBack = "";
			if (r.getBackPrices().size() > 0) {
				InflatedPrice p = r.getBackPrices().get(0);
				bestBack = String.format("%,10.2f %s @ %,6.2f", p.getAmountAvailable(), prices.getCurrency(), p.getPrice());
			}
	
			msg += (String.format("%20s (%6d): Matched Amount: %,10.2f, Last Matched: %,6.2f, Best Back %s, Best Lay:%s"
					, marketRunner.getName(), r.getSelectionId(), r.getTotalAmountMatched(), r.getLastPriceMatched(), bestBack, bestLay)) + "\n";
		}
		msg += ("") + "\n";
		return msg;
	}

	public static APIContext getApiContext() {
		return apiContext;
	}
}
