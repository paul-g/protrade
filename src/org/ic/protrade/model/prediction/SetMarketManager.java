package org.ic.protrade.model.prediction;

import org.ic.protrade.domain.markets.MarketPrices;
import org.ic.protrade.domain.markets.MatchScore;
import org.ic.protrade.domain.markets.SetBettingMarketData;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.PlayerEnum;

public class SetMarketManager {
	public static boolean isSetEnd(Match match, SetBettingMarketData data) {
		int count = 0;
		//MatchScore newMatchScore = null;
		//boolean accurate = (match.getImpossibleScores().size()) > 0 ? true : false;
		for (MatchScore matchScore : data.getMatchScoreMarketData().keySet()) {
			if (isEndOfSet(data.getMatchScoreMarketPrices(matchScore))) {
				//count += isEndOFSet(data.getMatchScoreMarketPrices(matchScore));
				count++;
				/*
				if (!match.getImpossibleScores().contains(matchScore)) {
					newMatchScore = matchScore;
					match.getImpossibleScores().add(matchScore);
				}
				*/
			}
		}				
		//System.out.println("---------------------------------------------Returning end of set " + (count > match.getCurrentSet()));
		if (match.getCurrentSet() == -1) {
			match.setCurrentSet(count);
			getNewMatchScore(match, data);
			return false;
		}
		if (count > match.getCurrentSet()) {
			match.setCurrentSet(count);
			return true;
		}
		return false;
	}
	
	public static MatchScore getLastImpossibleScore(Match match, SetBettingMarketData data) {
		boolean accurate = (match.getImpossibleScores().size()) > 0 ? true : false;		
		MatchScore newMatchScore = getNewMatchScore(match, data);
		if (accurate)
			return newMatchScore;
		return null;
	}
	
	public static PlayerEnum getSetWinner(MatchScore impossibleMatchScore) {
		if (impossibleMatchScore.getFirstPlayerScore() > impossibleMatchScore.getSecondPlayerScore())
			return PlayerEnum.PLAYER2;
		return PlayerEnum.PLAYER1;
	}

	private static MatchScore getNewMatchScore(Match match,
			SetBettingMarketData data) {
		MatchScore newMatchScore = null;
		for (MatchScore matchScore : data.getMatchScoreMarketData().keySet()) {
			if (isEndOfSet(data.getMatchScoreMarketPrices(matchScore)) &&
					!match.getImpossibleScores().contains(matchScore)) {
					newMatchScore = matchScore;
					match.getImpossibleScores().add(matchScore);
			}
		}
		return newMatchScore;
	}
	

	private static boolean isEndOfSet(MarketPrices marketPrices) {
		if (marketPrices.getBackPrices().size() > 0)
			return (marketPrices.getBackPrices().get(0).first() > 990) ? true : false;
		return false;
	}
}
