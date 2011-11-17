package org.ic.tennistrader.model.connection;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.ic.tennistrader.demo.handler.ExchangeAPI;
import org.ic.tennistrader.demo.handler.GlobalAPI;
import org.ic.tennistrader.demo.handler.ExchangeAPI.Exchange;
import org.ic.tennistrader.demo.util.InflatedMarketPrices;
import org.ic.tennistrader.demo.util.InflatedMarketPrices.InflatedPrice;
import org.ic.tennistrader.demo.util.InflatedMarketPrices.InflatedRunner;
import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.Market;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.Runner;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.GetEventsResp;
import org.ic.tennistrader.generated.global.BFGlobalServiceStub.MarketSummary;
import org.ic.tennistrader.utils.Pair;

public class BetfairExchangeHandler extends BetfairConnectionHandler {
	private static Logger log = Logger.getLogger(BetfairExchangeHandler.class);

	// returns the match odds market data info
	public static MOddsMarketData getMarketOdds(EventBetfair m) {
		MOddsMarketData modds = new MOddsMarketData();
		try {
			GetEventsResp resp = GlobalAPI.getEvents(apiContext, m
					.getBetfairId());
			// add the list of possible events - not needed since we only want
			// markets
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
					// System.out.println("YES");
					marketOdds = ms;
				}
			}
			// create the string to display the Match Odds
			if (marketOdds != null) {
				Exchange selectedExchange = marketOdds.getExchangeId() == 1 ? Exchange.UK
						: Exchange.AUS;
				Market selectedMarket = ExchangeAPI.getMarket(selectedExchange,
						apiContext, marketOdds.getMarketId());
				InflatedMarketPrices prices = ExchangeAPI.getMarketPrices(
						selectedExchange, apiContext, selectedMarket
								.getMarketId());
				// Display.showMarket(selectedExchange, selectedMarket, prices);
				modds.setExchange(selectedExchange.toString());
				modds.setDate(selectedMarket.getMarketTime().getTime());
				modds.setMatchStatus(selectedMarket.getMarketStatus()
						.toString());
				modds.setLocation(selectedMarket.getCountryISO3());

				/*
				 * modds.setPl1Back(setBack(prices,1));
				 * modds.setPl2Back(setBack(prices,2));
				 * modds.setPl1Lay(setLay(prices,1));
				 * modds.setPl2Lay(setLay(prices,2));
				 */

				modds.setDelay(prices.getInPlayDelay());

				/*
				 * String[] names = getNames(m.getName());
				 * modds.setPlayer1(names[0]); modds.setPlayer2(names[1]);
				 */
				// msg = showMarket(selectedExchange, selectedMarket, prices);

				int i = 0;
				for (InflatedRunner r : prices.getRunners()) {
					Runner marketRunner = null;
					for (Runner mr : selectedMarket.getRunners().getRunner()) {
						if (mr.getSelectionId() == r.getSelectionId()) {
							marketRunner = mr;
							break;
						}
					}
					if (i == 0) {
						modds.setPlayer1(marketRunner.getName());
						modds.setPl1Back(setBackValues(r));
						modds.setPl1Lay(setLayValues(r));
					} else {
						modds.setPlayer2(marketRunner.getName());
						modds.setPl2Back(setBackValues(r));
						modds.setPl2Lay(setLayValues(r));
					}
					i++;
				}
				// System.out.println("Player 1 " + modds.getPlayer1() +
				// ", player2 " + modds.getPlayer2());
			}
		} catch (Exception e) {
			log.info("Error fetching market info for the match - "
					+ e.getMessage());
		}
		return modds;
		// return msg;
	}

	// Returns the string containing the given market info
	@SuppressWarnings("unused")
	private static String showMarket(Exchange exch, Market m,
			InflatedMarketPrices prices) {
		String msg = "";
		msg += ("Market: " + m.getName() + "(" + m.getMarketId() + ") on the "
				+ exch + " exchange:")
				+ "\n";
		msg += ("   Start time     : " + m.getMarketTime().getTime()) + "\n";
		msg += ("   Status         : " + m.getMarketStatus()) + "\n";
		msg += ("   Location       : " + m.getCountryISO3()) + "\n";
		msg += ("") + "\n";

		msg += ("Runners:") + "\n";
		for (InflatedRunner r : prices.getRunners()) {
			Runner marketRunner = null;

			for (Runner mr : m.getRunners().getRunner()) {
				if (mr.getSelectionId() == r.getSelectionId()) {
					marketRunner = mr;
					break;
				}
			}

			String bestLay = "";
			if (r.getLayPrices().size() > 0) {
				InflatedPrice p = r.getLayPrices().get(0);
				bestLay = String.format("%,10.2f %s @ %,6.2f", p
						.getAmountAvailable(), prices.getCurrency(), p
						.getPrice());
			}

			String bestBack = "";
			if (r.getBackPrices().size() > 0) {
				InflatedPrice p = r.getBackPrices().get(0);
				bestBack = String.format("%,10.2f %s @ %,6.2f", p
						.getAmountAvailable(), prices.getCurrency(), p
						.getPrice());
			}

			msg += (String
					.format(
							"%20s (%6d): Matched Amount: %,10.2f, Last Matched: %,6.2f, Best Back %s, Best Lay:%s",
							marketRunner.getName(), r.getSelectionId(), r
									.getTotalAmountMatched(), r
									.getLastPriceMatched(), bestBack, bestLay))
					+ "\n";
		}
		msg += ("") + "\n";
		return msg;
	}

	/*
	public static String[] getNames(String title) {
		if (!MatchUtils.isMatch(title))
			;
		String[] str = title.split(" v ");
		return str;
	}

	public static ArrayList<Pair<Double, Double>> setBack(
			InflatedMarketPrices prices, int nr) {
		if (prices.getRunners().size() >= 2) {
			InflatedRunner r = prices.getRunners().get(nr - 1);
			ArrayList<Pair<Double, Double>> result = setBackValues(r);
			return result;
		}
		return null;
	}

	public static ArrayList<Pair<Double, Double>> setLay(
			InflatedMarketPrices prices, int nr) {
		if (prices.getRunners().size() >= 2) {
			InflatedRunner r = prices.getRunners().get(nr - 1);
			ArrayList<Pair<Double, Double>> result = setLayValues(r);
			return result;
		}
		return null;
	}
	*/
	
	private static ArrayList<Pair<Double, Double>> setBackValues(
			InflatedRunner r) {
		ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
		for (InflatedPrice p : r.getBackPrices()) {
			result.add(new Pair<Double, Double>(p.getPrice(), p
					.getAmountAvailable()));
		}
		return result;
	}

	private static ArrayList<Pair<Double, Double>> setLayValues(InflatedRunner r) {
		ArrayList<Pair<Double, Double>> result = new ArrayList<Pair<Double, Double>>();
		for (InflatedPrice p : r.getLayPrices()) {
			result.add(new Pair<Double, Double>(p.getPrice(), p
					.getAmountAvailable()));
		}
		return result;
	}

	public void getBetHistory() {
		// ExchangeAPI.getBetHistory(context, int marketId, )
	}
}
