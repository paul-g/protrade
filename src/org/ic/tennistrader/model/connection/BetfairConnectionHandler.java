package org.ic.tennistrader.model.connection;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.ic.tennistrader.domain.Tournament;
import org.ic.tennistrader.domain.markets.EventBetfair;
import org.ic.tennistrader.domain.markets.EventMarketBetfair;
import org.ic.tennistrader.domain.markets.MarketBetfair;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.domain.profile.AccountFunds;
import org.ic.tennistrader.domain.profile.ProfileData;
import org.ic.tennistrader.exceptions.LoginFailedException;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.GetAccountFundsResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.BFEvent;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.EventType;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.MarketSummary;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.ViewProfileResp;
import org.ic.tennistrader.model.connection.ExchangeAPI.Exchange;
import org.ic.tennistrader.utils.MatchUtils;

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
	
	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	private static void printTours(Tournament tournament) {
	      log.info(tournament.toString());
	      for (RealMatch m : tournament.getMatches())
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
		for (EventMarketBetfair emb: events) {
			if (emb instanceof EventBetfair) {
				Tournament newTournament = new Tournament(emb.getName(), (EventBetfair)emb);
				filterMatches(newTournament);
				tournaments.add(newTournament);
			}
		}
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
	private static void filterMatches(Tournament tournament) {
		List<RealMatch> matches = new ArrayList<RealMatch>();
		matches = getMatches(matches, tournament.getEventBetfair());
		tournament.setMatches(matches);
	}
	
	private static List<RealMatch> getMatches(List<RealMatch> matches, EventBetfair eventBetfair) {
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
	
	public static RealMatch getMatch(EventBetfair eb) {
		return new RealMatch("", "", eb);
	}
	
    public static ProfileData getProfileData() throws Exception {
        ProfileData profileData = new ProfileData();        
        // get account funds info
        GetAccountFundsResp ukFunds = ExchangeAPI.getAccountFunds(Exchange.UK,
                apiContext);
        GetAccountFundsResp ausFunds = ExchangeAPI.getAccountFunds(Exchange.AUS,
                apiContext);
        AccountFunds ukAccountFunds = new AccountFunds(ukFunds);
        AccountFunds ausAccountFunds = new AccountFunds(ausFunds);
        profileData.setAusAccountFunds(ausAccountFunds);
        profileData.setUkAccountFunds(ukAccountFunds);                
        // get the personal profile info
        ViewProfileResp resp = GlobalAPI.getProfile(apiContext);        
        setProfileInfo(profileData, resp);        
        return profileData;
    }

    private static void setProfileInfo(ProfileData profileData,
            ViewProfileResp resp) {
        // set names
        profileData.setTitle(resp.getTitle().toString());
        profileData.setFirstName(resp.getFirstName());
        profileData.setSurname(resp.getSurname());
        profileData.setUsername(resp.getUserName());
        // set address info
        profileData.setAddress1(resp.getAddress1());
        profileData.setAddress2(resp.getAddress2());
        profileData.setAddress3(resp.getAddress3());        
        profileData.setTownCity(resp.getTownCity());
        profileData.setCountyState(resp.getCountyState());
        profileData.setPostCode(resp.getPostCode());
        profileData.setCountry(resp.getCountryOfResidence());
        // set contact info
        profileData.setHomePhone(resp.getHomeTelephone());
        profileData.setMobilePhone(resp.getMobileTelephone());
        profileData.setEmailAddress(resp.getEmailAddress());
        // set additional info
        profileData.setCurrency(resp.getCurrency());
        profileData.setTimeZone(resp.getTimeZone());        
        // set Gamcare values
        profileData.setGamcareFrequency(resp.getGamcareFrequency().toString());
        profileData.setGamcareLimit(resp.getGamcareLimit());
        profileData.setGamcareLossLimit(resp.getGamcareLossLimit());
        profileData.setGamcareLossLimitFrequency(resp.getGamcareLossLimitFrequency().toString());
        profileData.setGamcareUpdateDate(resp.getGamcareUpdateDate());
    }

	public static APIContext getApiContext() {
		return apiContext;
	}
}
