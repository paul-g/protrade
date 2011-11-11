package org.ic.tennistrader.demo;
import java.util.ArrayList;

import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetCategoryTypeEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetPersistenceTypeEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.CancelBets;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.CancelBetsResult;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.GetAccountFundsResp;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.MUBet;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.Market;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.PlaceBets;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.PlaceBetsResult;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.Runner;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.UpdateBets;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.UpdateBetsResult;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.BFEvent;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.EventType;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.MarketSummary;
import org.apache.log4j.*;

import org.ic.tennistrader.demo.handler.ExchangeAPI;
import org.ic.tennistrader.demo.handler.GlobalAPI;
import org.ic.tennistrader.demo.handler.ExchangeAPI.Exchange;
import org.ic.tennistrader.demo.util.APIContext;
import org.ic.tennistrader.demo.util.Display;
import org.ic.tennistrader.demo.util.InflatedCompleteMarketPrices;
import org.ic.tennistrader.demo.util.InflatedMarketPrices;


/** 
 * Demonstration of the Betfair API.
 * 
 * This is the main control class for running the Betfair API demo. 
 * User display and input is handled by the Display class
 * API Management is handled by the classes in the apihandler package 
 */ 
public class APIDemo {

	// Menus
	private static final String[] MAIN_MENU = new String[] 
	    {"View account", "Choose Market", "View Market", "View Complete Market", "Bet Management", "View Usage", "Exit"};
	   
	private static final String[] BETS_MENU = new String[] 
 	    {"Place Bet", "Update Bet", "Cancel Bet", "Back"};

	// The session token
	private static APIContext apiContext = new APIContext();
	
	// the current chosen market and Exchange for that market
	private static Market selectedMarket;
	private static Exchange selectedExchange;
	
	// Fire up the API demo
	public static void main(String[] args)  throws Exception {
	
		// Initialise logging and turn logging off. Change OFF to DEBUG for detailed output.
		Logger rootLog = LogManager.getRootLogger();
		Level lev = Level.toLevel("OFF");
		rootLog.setLevel(lev);
		
		Display.println("Welcome to the Betfair API Demo");
		String username = args.length < 1 ? Display.getStringAnswer("Betfair username:") : args[0];
		String password = args.length < 2 ? Display.getStringAnswer("Betfair password:") : args[1];
		
		// Perform the login before anything else.
		try
		{
			GlobalAPI.login(apiContext, username, password);
		}
		catch (Exception e)
		{
			// If we can't log in for any reason, just exit.
			Display.showException("*** Failed to log in", e);
			System.exit(1);
		}
		
		boolean finished = false;
		
		while (!finished) {
			try	{
				int choice = Display.getChoiceAnswer("\nChoose an operation", MAIN_MENU);
				switch (choice) {
					case 0: // View account
						showAccountFunds(Exchange.UK);
						showAccountFunds(Exchange.AUS);
						break;
					case 1: // Choose Market 
						chooseMarket();
						break;
					case 2: // View Market 
						viewMarket();
						break;
					case 3: // Show Complete Market
						viewCompleteMarket();
						break;
					case 4: // Show Bets 
						manageBets();
						break;
					case 5: // Show Usage
						int type = Display.getChoiceAnswer("\nType of stats required", new String[] {"Combined", "Timed"});
						if (type == 0) {
							Display.showCombinedUsage(apiContext.getUsage());
						} else {
							Display.showTimedUsage(apiContext.getUsage());
						}
						break;
					case 6: // Exit
						finished = true;
						break;
				}
			} catch (Exception e) {
				// Print out the exception and carry on.
				Display.showException("*** Failed to call API", e);
			}
			
		}
		
		// Logout before shutting down.
		try
		{
			GlobalAPI.logout(apiContext);
		}
		catch (Exception e)
		{
			// If we can't log out for any reason, there's not a lot to do.
			Display.showException("Failed to log out", e);
		}
		Display.println("Logout successful");
	}

	// Check if a market is selected
	private static boolean isMarketSelected() {
		if (selectedMarket == null) {
			Display.println("You must select a market");
			return false;
		}
		return true;
	}

	// Retrieve and display the account funds for the specified exchange
	private static void showAccountFunds(Exchange exch) throws Exception {
		GetAccountFundsResp funds = ExchangeAPI.getAccountFunds(exch, apiContext);
		Display.showFunds(exch, funds);
	}
	
	// Select a market by the following process
	// * Select a type of event
	// * Recursively select an event of this type
	// * Select a market within this event if one exists.
	private static void chooseMarket() throws Exception {
		// Get available event types.
		EventType[] types = GlobalAPI.getActiveEventTypes(apiContext);
		int typeChoice = Display.getChoiceAnswer("Choose an event type:", types);

		// Get available events of this type
		selectedMarket = null;
		int eventId = types[typeChoice].getId();
		while (selectedMarket == null) {
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, eventId);
			BFEvent[] events = resp.getEventItems().getBFEvent();
			if (events == null) {
				events = new BFEvent[] {};
			} else {
				// The API returns Coupons as event names, but Coupons don't contain markets so we remove any
				// events that are Coupons.
				ArrayList<BFEvent> nonCouponEvents = new ArrayList<BFEvent>(events.length);
				for(BFEvent e: events) {
					if(!e.getEventName().equals("Coupons")) {
						nonCouponEvents.add(e);
					}
				}
				events = (BFEvent[]) nonCouponEvents.toArray(new BFEvent[]{});
			}
			MarketSummary[] markets = resp.getMarketItems().getMarketSummary();
			if (markets == null) {
				markets = new MarketSummary[] {};
			}
			int choice = Display.getChoiceAnswer("Choose a Market or Event:", events, markets);

			// Exchange ID of 1 is the UK, 2 is AUS
			if (choice < events.length) {
				eventId = events[choice].getEventId(); 
			} else {
				choice -= events.length;
				selectedExchange = markets[choice].getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
				selectedMarket = ExchangeAPI.getMarket(selectedExchange, apiContext, markets[choice].getMarketId());
			}				
		}
	}
	
	// Retrieve and view information about the selected market
	private static void viewMarket() throws Exception {
		if (isMarketSelected()) {
			InflatedMarketPrices prices = ExchangeAPI.getMarketPrices(selectedExchange, apiContext, selectedMarket.getMarketId());
			
			// Now show the inflated compressed market prices.
			Display.showMarket(selectedExchange, selectedMarket, prices);
		}
	}
	
	private static void viewCompleteMarket () throws Exception {
		if (isMarketSelected()) {
			InflatedCompleteMarketPrices prices = ExchangeAPI.getCompleteMarketPrices(selectedExchange, apiContext, selectedMarket.getMarketId());
			
			// Now show the inflated compressed complete market prices.
			Display.showCompleteMarket(selectedExchange, selectedMarket, prices);
		}
	}

	// show all my matched and unmatched bets specified market.
	private static void manageBets() throws Exception {
		if (isMarketSelected()) {
			boolean finished = false;
			while (!finished) {
				// show current bets
				MUBet[] bets = ExchangeAPI.getMUBets(selectedExchange, apiContext, selectedMarket.getMarketId());
				Display.showBets(selectedMarket, bets);
				
				int choice = Display.getChoiceAnswer("Choose an operation", BETS_MENU);
				switch (choice) {
					case 0: // Place Bet
						placeBet();
						break;
					case 1: // Update Bet
						updateBet(bets[Display.getIntAnswer("Choose a bet:", 1, bets.length) - 1]);
						break;
					case 2: // Cancel Bet
						cancelBet(bets[Display.getIntAnswer("Choose a bet:", 1, bets.length) - 1]);
						break;
					case 3: // Back
						finished = true;
						break;
				}
			}
		}
	}
	
	// Place a bet on the specified market.
	private static void placeBet() throws Exception {
		if (isMarketSelected()) {
			Runner[] runners = selectedMarket.getRunners().getRunner();
			int choice = Display.getChoiceAnswer("Choose a Runner:", runners);
			
			// Set up the individual bet to be placed
			PlaceBets bet = new PlaceBets();
			bet.setMarketId(selectedMarket.getMarketId());
			bet.setSelectionId(runners[choice].getSelectionId());
			bet.setBetCategoryType(BetCategoryTypeEnum.E);
			bet.setBetPersistenceType(BetPersistenceTypeEnum.NONE);
			bet.setBetType(BetTypeEnum.Factory.fromValue(Display.getStringAnswer("Bet type:")));
			bet.setPrice(Display.getDoubleAnswer("Price:", false));
			bet.setSize(Display.getDoubleAnswer("Size:", false));
			
			if (Display.confirm("This action will actually place a bet on the Betfair exchange")) {
				// We can ignore the array here as we only sent in one bet.
				PlaceBetsResult betResult = ExchangeAPI.placeBets(selectedExchange, apiContext, new PlaceBets[] {bet})[0];
				
				if (betResult.getSuccess()) {
					Display.println("Bet "+betResult.getBetId()+" placed. "+betResult.getSizeMatched() +" matched @ "+betResult.getAveragePriceMatched());
				} else {
					Display.println("Failed to place bet: Problem was: "+betResult.getResultCode());
				}
			}
		}
	}
	
	// Place a bet on the specified market.
	private static void updateBet(MUBet bet) throws Exception {
		if (isMarketSelected()) {
			double newPrice = Display.getDoubleAnswer("New Price [Unchanged - "+bet.getPrice()+"]:", true);
			double newSize = Display.getDoubleAnswer("New Size [Unchanged - "+bet.getSize()+"]:", true);

			if (newPrice == 0.0d) {
				newPrice = bet.getPrice();
			}
			if (newSize == 0.0d) {
				newSize = bet.getSize();
			}

			// Set up the individual bet to be edited
			UpdateBets upd = new UpdateBets(); 
			upd.setBetId(bet.getBetId());
			upd.setOldBetPersistenceType(bet.getBetPersistenceType());
			upd.setOldPrice(bet.getPrice());
			upd.setOldSize(bet.getSize());
			upd.setNewBetPersistenceType(bet.getBetPersistenceType());
			upd.setNewPrice(newPrice);
			upd.setNewSize(newSize);
			
			if (Display.confirm("This action will actually edit a bet on the Betfair exchange")) {
				// We can ignore the array here as we only sent in one bet.
				UpdateBetsResult betResult = ExchangeAPI.updateBets(selectedExchange, apiContext, new UpdateBets[] {upd})[0];
				
				if (betResult.getSuccess()) {
					Display.println("Bet "+betResult.getBetId()+" updated. New bet is "+betResult.getNewSize() +" @ "+betResult.getNewPrice());
				} else {
					Display.println("Failed to update bet: Problem was: "+betResult.getResultCode());
				}
			}
		}
	}
	
	// Place a bet on the specified market.
	private static void cancelBet(MUBet bet) throws Exception {
		if (isMarketSelected()) {
			if (Display.confirm("This action will actually cancel a bet on the Betfair exchange")) {
				CancelBets canc = new CancelBets();
				canc.setBetId(bet.getBetId());
				
				// We can ignore the array here as we only sent in one bet.
				CancelBetsResult betResult = ExchangeAPI.cancelBets(selectedExchange, apiContext, new CancelBets[] {canc})[0];
				
				if (betResult.getSuccess()) {
					Display.println("Bet "+betResult.getBetId()+" cancelled.");
				} else {
					Display.println("Failed to cancel bet: Problem was: "+betResult.getResultCode());
				}
			}
		}
	}
}
