package org.ic.tennistrader.domain.markets;

import java.util.HashMap;

public class SetBettingMarketData {
	private HashMap<MatchScore, MarketPrices> matchScoreMarketData;

	public SetBettingMarketData() {
		this.matchScoreMarketData = new HashMap<MatchScore, MarketPrices>();
	}

	public void addSetScoreMarketPrices(MatchScore matchScore,
			MarketPrices marketPrices) {
		this.matchScoreMarketData.put(matchScore, marketPrices);
		/*
		System.out.println("Added set score market data: "
				+ matchScore.toString() + " with prices "
				+ marketPrices.toString());
		*/
	}

	public MarketPrices getSetScoreMarketPrices(MatchScore matchScore) {
		if (this.matchScoreMarketData.containsKey(matchScore))
			return this.matchScoreMarketData.get(matchScore);
		return null;
	}

	public MarketPrices getSetScoreMarketPrices(int firstPlayerScore,
			int secondPlayerScore) {
		MatchScore matchScore = new MatchScore(firstPlayerScore,
				secondPlayerScore);
		return getSetScoreMarketPrices(matchScore);
	}
	
	public HashMap<MatchScore, MarketPrices> getMatchScoreMarketData() {
		return matchScoreMarketData;
	}

	public void setMatchScoreMarketData(
			HashMap<MatchScore, MarketPrices> matchScoreMarketData) {
		this.matchScoreMarketData = matchScoreMarketData;
	}
}
