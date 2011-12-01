package org.ic.tennistrader.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.ui.BetsDisplay;
import org.ic.tennistrader.utils.Pair;

import static org.ic.tennistrader.utils.Pair.pair;

public class BetManager {
    //TODO this should be synchronized
    private static HashMap<Match, VirtualBetMarketInfo> matchMarketData = new HashMap<Match, VirtualBetMarketInfo>();
    private static List<Bet> matchedBets = new ArrayList<Bet>();
    private static List<Bet> unmatchedBets = new ArrayList<Bet>();
    private static Logger log = Logger.getLogger(BetManager.class);
    
    public static void placeBet(Match match, PlayerEnum player, BetTypeEnum betType, double odds, double amount) {
        Bet newBet = new Bet(match, player, betType, pair(odds, amount));
        
        Double maxAmount = getMaxAvailableAmount(newBet);        
        if (amount > maxAmount) {
            newBet.setUnmatchedValue(amount - maxAmount);
            unmatchedBets.add(newBet);
        } else {
            matchedBets.add(newBet);
        }
        
        double firstWinnerProfit = getBetProfit(newBet, isBetSuccessful(newBet, PlayerEnum.PLAYER1));
        newBet.setFirstPlayerWinnerProfit(firstWinnerProfit);
        double secondWinnerProfit = getBetProfit(newBet, isBetSuccessful(newBet, PlayerEnum.PLAYER2));
        newBet.setSecondPlayerWinnerProfit(secondWinnerProfit);        
        
        addMatchedBetAmount(newBet);
        
        BetsDisplay.addBet(newBet);            
    }
    
    private static void addMatchedBetAmount(Bet bet) {
        VirtualBetMarketInfo virtualMarketInfo = matchMarketData.get(bet.getMatch());
        if (bet.getType().equals(BetTypeEnum.B)) {
            if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                virtualMarketInfo.addMatchedBackBetsFirstPlayer(bet.getOdds(), bet.getAmount() - bet.getUnmatchedValue());
            }
            else {
                virtualMarketInfo.addMatchedBackBetsSecondPlayer(bet.getOdds(), bet.getAmount() - bet.getUnmatchedValue());
            }
        } else {
            if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                virtualMarketInfo.addMatchedLayBetsFirstPlayer(bet.getOdds(), bet.getAmount() - bet.getUnmatchedValue());
            }
            else {
                virtualMarketInfo.addMatchedLayBetsSecondPlayer(bet.getOdds(), bet.getAmount() - bet.getUnmatchedValue());
            }
        }        
    }

    private static Double getMaxAvailableAmount(Bet bet) {
        Double maxMarketAmount, playedAmount;
        VirtualBetMarketInfo data = matchMarketData.get(bet.getMatch());
        if (bet.getType().equals(BetTypeEnum.B)) {
            if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                maxMarketAmount = data.getBackFirstPlayerAvailableMatches().get(bet.getOdds());
                playedAmount = data.getMatchedBackBetsFirstPlayer().get(bet.getOdds());
            }
            else {
                maxMarketAmount = data.getBackSecondPlayerAvailableMatches().get(bet.getOdds());
                playedAmount = data.getMatchedBackBetsSecondPlayer().get(bet.getOdds());
            }
        } else {
            if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                maxMarketAmount = data.getLayFirstPlayerAvailableMatches().get(bet.getOdds());
                playedAmount = data.getMatchedLayBetsFirstPlayer().get(bet.getOdds());
            }
            else {
                maxMarketAmount = data.getLaySecondPlayerAvailableMatches().get(bet.getOdds());
                playedAmount = data.getMatchedLayBetsSecondPlayer().get(bet.getOdds());
            }
        }
        double maxAvAmount = 0.0;
        if (maxMarketAmount != null)
            maxAvAmount = maxMarketAmount;
        if (playedAmount != null)
            maxAvAmount -= playedAmount;
        return maxAvAmount;
    }
    
    public static void updateMarketAvailableMatches(Match match, MOddsMarketData newMarketData) {
        VirtualBetMarketInfo virtualMarketInfo = matchMarketData.get(match);
        if (virtualMarketInfo == null)
            virtualMarketInfo = new VirtualBetMarketInfo();
        addNewMarketValues(virtualMarketInfo.getBackFirstPlayerAvailableMatches(), newMarketData.getPl1Back());
        addNewMarketValues(virtualMarketInfo.getLayFirstPlayerAvailableMatches(), newMarketData.getPl1Lay());
        addNewMarketValues(virtualMarketInfo.getBackSecondPlayerAvailableMatches(), newMarketData.getPl2Back());
        addNewMarketValues(virtualMarketInfo.getLaySecondPlayerAvailableMatches(), newMarketData.getPl2Lay());
        matchMarketData.put(match, virtualMarketInfo);
        reviewUnmatchedBets(match);
    }

	private static void reviewUnmatchedBets(Match match) {
	    VirtualBetMarketInfo virtualMarketInfo = matchMarketData.get(match);
        if (virtualMarketInfo != null) {
            List<Bet> newMatchedBets = new ArrayList<Bet>();
            for (Bet bet : unmatchedBets) {
                if (bet.getMatch().equals(match)) {
                    double availableAmount = getMaxAvailableAmount(bet);
                    if (availableAmount > 0) {
                        matchBet(virtualMarketInfo, availableAmount, bet);
                        BetsDisplay.updateUnmatchedBet(bet);
                        if (bet.getUnmatchedValue() == 0)
                            newMatchedBets.add(bet);
                    }
                }
            }
            for (Bet newMatchedBet :newMatchedBets) {
                unmatchedBets.remove(newMatchedBet);
                matchedBets.add(newMatchedBet);
            }
        }        
    }

    private static void matchBet(VirtualBetMarketInfo virtualMarketInfo,
            double availableAmount, Bet bet) {
        double toMatch;
        if (availableAmount >= bet.getUnmatchedValue())
            toMatch = bet.getUnmatchedValue();
        else
            toMatch = availableAmount;
        bet.setUnmatchedValue(bet.getUnmatchedValue() - toMatch);
        if (bet.getType().equals(BetTypeEnum.B)) {
            if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                virtualMarketInfo.addMatchedBackBetsFirstPlayer(bet.getOdds(), toMatch);
            }
            else {
                virtualMarketInfo.addMatchedBackBetsSecondPlayer(bet.getOdds(), toMatch);
            }
        } else {
            if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                virtualMarketInfo.addMatchedLayBetsFirstPlayer(bet.getOdds(), toMatch);
            }
            else {
                virtualMarketInfo.addMatchedLayBetsSecondPlayer(bet.getOdds(), toMatch);
            }
        }        
    }
    
    private static void addNewMarketValues(HashMap<Double, Double> availableMatches,
            ArrayList<Pair<Double, Double>> pl1Back) {
        for (Pair<Double, Double> pair : pl1Back) {
            availableMatches.put(pair.first(), pair.second());
        }        
    }

    public static void setBetsOutcome(Match match) {
		PlayerEnum winner;
		try {
			winner = match.getWinner();
			for (Bet bet : matchedBets) {
				if (bet.getMatch().equals(match)) {
					bet.setProfit(getBetProfit(bet, isBetSuccessful(bet, winner)));
					BetsDisplay.addSettledBet(bet);
				}
			}
		} catch (MatchNotFinishedException e1) {
			log.info("Trying to settle bets of an unfinished match");
		}
	}

	private static boolean isBetSuccessful(Bet bet, PlayerEnum winner) {
		if (winner == bet.getPlayer())
		    if (bet.getType().equals(BetTypeEnum.B))
		        return true;
		    else
		        return false;
		else
		    if (bet.getType().equals(BetTypeEnum.B))
                return false;
            else
                return true;
	}
	
	private static double getBetProfit(Bet bet, boolean betSuccessful) {
		double profit;
		if (betSuccessful) {
			if (bet.getType().equals(BetTypeEnum.B))
				//bet.setProfit( (bet.getOdds() - 1) * bet.getAmount() );
				profit = (bet.getOdds() - 1) * bet.getAmount();
			else
				//bet.setProfit( bet.getAmount() );
				profit = bet.getAmount();
		} else {
			if (bet.getType().equals(BetTypeEnum.B))
				//bet.setProfit( (-1) * bet.getAmount() );
				profit = (-1) * bet.getAmount();
			else
				//bet.setProfit( (-1) * (bet.getOdds() - 1) * bet.getAmount() );
				profit = (-1) * (bet.getOdds() - 1) * bet.getAmount();
		}
		return profit;
	}
	
    public static void setMatchedBets(List<Bet> matchedBets) {
        BetManager.matchedBets = matchedBets;
    }

    public static List<Bet> getMatchedBets() {
        return matchedBets;
    }

    public static void setUnmatchedBets(List<Bet> unmatchedBets) {
        BetManager.unmatchedBets = unmatchedBets;
    }

    public static List<Bet> getUnmatchedBets() {
        return unmatchedBets;
    }
}