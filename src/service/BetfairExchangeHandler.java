package src.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import src.demo.handler.ExchangeAPI;
import src.demo.handler.GlobalAPI;
import src.demo.handler.ExchangeAPI.Exchange;
import src.demo.util.InflatedMarketPrices;
import src.demo.util.InflatedMarketPrices.InflatedPrice;
import src.demo.util.InflatedMarketPrices.InflatedRunner;
import src.domain.EventBetfair;
import src.domain.MarketBetfair;
import src.generated.exchange.BFExchangeServiceStub.Market;
import src.generated.exchange.BFExchangeServiceStub.Runner;
import src.generated.global.BFGlobalServiceStub.GetEventsResp;
import src.generated.global.BFGlobalServiceStub.MarketSummary;

public class BetfairExchangeHandler extends BetfairConnectionHandler {
	private static Logger log = Logger.getLogger(BetfairExchangeHandler.class);
	
	// returns a string with Match Odds info
	public static String getMarketOdds(EventBetfair m) {
		String msg = "";
		try {
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, m
					.getBetfairId());
			// add the list of possible events - not needed since we only want markets			
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
			// create the string to display the Match Odds
			if (marketOdds != null) {
				Exchange selectedExchange = marketOdds.getExchangeId() == 1 ? Exchange.UK : Exchange.AUS;
				Market selectedMarket = ExchangeAPI.getMarket(selectedExchange, apiContext, marketOdds.getMarketId());			
				InflatedMarketPrices prices = ExchangeAPI.getMarketPrices(selectedExchange, apiContext, selectedMarket.getMarketId());				
				//Display.showMarket(selectedExchange, selectedMarket, prices);
				msg = showMarket(selectedExchange, selectedMarket, prices);				
			}			
		} catch (Exception e) {
			log.info("Error fetching market info for the match");
		}
		return msg;
	}
	
	// Returns the string containing the given market info
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
}
