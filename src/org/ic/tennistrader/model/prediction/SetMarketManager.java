package org.ic.tennistrader.model.prediction;

import org.ic.tennistrader.domain.markets.MarketPrices;
import org.ic.tennistrader.domain.markets.MatchScore;
import org.ic.tennistrader.domain.markets.SetBettingMarketData;
import org.ic.tennistrader.domain.match.Match;

public class SetMarketManager {
	public static boolean isSetEnd(Match match, SetBettingMarketData data) {
		int count = 0;
		for (MatchScore matchScore : data.getMatchScoreMarketData().keySet()) {
			count += isEndOFSet(data.getMatchScoreMarketPrices(matchScore));
		}				
		//System.out.println("---------------------------------------------Returning end of set " + (count > match.getCurrentSet()));
		if (match.getCurrentSet() == -1) {
			match.setCurrentSet(count);
			return false;
		}			
		return count > match.getCurrentSet();
	}

	private static int isEndOFSet(MarketPrices marketPrices) {
		if (marketPrices.getBackPrices().size() > 0)
			return (marketPrices.getBackPrices().get(0).first() > 990) ? 1 : 0;
		return 0;
	}
}
