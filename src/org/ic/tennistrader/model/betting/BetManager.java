package org.ic.tennistrader.model.betting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.ui.betting.BetsDisplay;
import org.ic.tennistrader.utils.Pair;
import static org.ic.tennistrader.utils.Pair.pair;

public class BetManager {
    private static ConcurrentHashMap<Match, VirtualBetMarketInfo> matchMarketData = new ConcurrentHashMap<Match, VirtualBetMarketInfo>();
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
        updatePossibleProfits(newBet);
        
        addMatchedBetAmount(newBet);
        
        if (BetsDisplay.getComposite() != null)
            BetsDisplay.addBet(newBet);            
    }
    
    private static void updatePossibleProfits(Bet newBet) {
		if (matchMarketData.containsKey(newBet.getMatch())) {
			VirtualBetMarketInfo marketInfo = matchMarketData.get(newBet.getMatch());
			marketInfo.addFirstPlayerWinnerProfit(newBet.getFirstPlayerWinnerProfit());
			marketInfo.addSecondPlayerWinnerProfit(newBet.getSecondPlayerWinnerProfit());
		}
	}

    private static void addMatchedBetAmount(Bet bet) {
        if (matchMarketData.containsKey(bet.getMatch())) {
            VirtualBetMarketInfo virtualMarketInfo = matchMarketData.get(bet
                    .getMatch());
            if (bet.getType().equals(BetTypeEnum.B)) {
                if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                    virtualMarketInfo.addMatchedBackBetsFirstPlayer(bet
                            .getOdds(), bet.getAmount()
                            - bet.getUnmatchedValue());
                } else {
                    virtualMarketInfo.addMatchedBackBetsSecondPlayer(bet
                            .getOdds(), bet.getAmount()
                            - bet.getUnmatchedValue());
                }
            } else {
                if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                    virtualMarketInfo.addMatchedLayBetsFirstPlayer(bet
                            .getOdds(), bet.getAmount()
                            - bet.getUnmatchedValue());
                } else {
                    virtualMarketInfo.addMatchedLayBetsSecondPlayer(bet
                            .getOdds(), bet.getAmount()
                            - bet.getUnmatchedValue());
                }
            }
        }
    }

    private static Double getMaxAvailableAmount(Bet bet) {
        Double maxMarketAmount = null, playedAmount = null;
        if (matchMarketData.containsKey(bet.getMatch())) {
            VirtualBetMarketInfo data = matchMarketData.get(bet.getMatch());
            if (bet.getType().equals(BetTypeEnum.B)) {
                if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                    maxMarketAmount = data.getBackFirstPlayerAvailableMatches()
                            .get(bet.getOdds());
                    playedAmount = data.getMatchedBackBetsFirstPlayer().get(
                            bet.getOdds());
                } else {
                    maxMarketAmount = data
                            .getBackSecondPlayerAvailableMatches().get(
                                    bet.getOdds());
                    playedAmount = data.getMatchedBackBetsSecondPlayer().get(
                            bet.getOdds());
                }
            } else {
                if (bet.getPlayer().equals(PlayerEnum.PLAYER1)) {
                    maxMarketAmount = data.getLayFirstPlayerAvailableMatches()
                            .get(bet.getOdds());
                    playedAmount = data.getMatchedLayBetsFirstPlayer().get(
                            bet.getOdds());
                } else {
                    maxMarketAmount = data.getLaySecondPlayerAvailableMatches()
                            .get(bet.getOdds());
                    playedAmount = data.getMatchedLayBetsSecondPlayer().get(
                            bet.getOdds());
                }
            }
        }
        double maxAvAmount = 0.0;
        if (maxMarketAmount != null)
            maxAvAmount = maxMarketAmount;
        if (playedAmount != null)
            maxAvAmount -= playedAmount;
        return maxAvAmount;
    }
    
	public static void updateMarketAvailableMatches(Match match,
			MOddsMarketData newMarketData) {
		VirtualBetMarketInfo virtualMarketInfo;
		if (matchMarketData.containsKey(match))
			virtualMarketInfo = matchMarketData.get(match);
		else
			virtualMarketInfo = new VirtualBetMarketInfo();
		addNewMarketValues(virtualMarketInfo
				.getBackFirstPlayerAvailableMatches(), newMarketData
				.getPl1Back());
		addNewMarketValues(virtualMarketInfo
				.getLayFirstPlayerAvailableMatches(), newMarketData.getPl1Lay());
		addNewMarketValues(virtualMarketInfo
				.getBackSecondPlayerAvailableMatches(), newMarketData
				.getPl2Back());
		addNewMarketValues(virtualMarketInfo
				.getLaySecondPlayerAvailableMatches(), newMarketData
				.getPl2Lay());
		matchMarketData.put(match, virtualMarketInfo);
		reviewUnmatchedBets(match);
	}

	private static void reviewUnmatchedBets(Match match) {
		if (matchMarketData.containsKey(match)) {
			VirtualBetMarketInfo virtualMarketInfo = matchMarketData.get(match);
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
			for (Bet newMatchedBet : newMatchedBets) {
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

	public static boolean isBetSuccessful(Bet bet, PlayerEnum winner) {
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
	
	public static double getBetProfit(Bet bet, boolean betSuccessful) {
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
	
    protected static void setMatchedBets(List<Bet> matchedBets) {
        BetManager.matchedBets = matchedBets;
    }

    protected static List<Bet> getMatchedBets() {
        return matchedBets;
    }

    protected static void setUnmatchedBets(List<Bet> unmatchedBets) {
        BetManager.unmatchedBets = unmatchedBets;
    }

    protected static List<Bet> getUnmatchedBets() {
        return unmatchedBets;
    }
    
    public static double getFirstPlayerWinnerProfit(Match match) {
    	if (matchMarketData.containsKey(match))
    		return matchMarketData.get(match).getFirstPlayerWinnerProfit();
    	else
    		return 0.0;
    }
    
    public static double getSecondPlayerWinnerProfit(Match match) {
    	if (matchMarketData.containsKey(match))
    		return matchMarketData.get(match).getSecondPlayerWinnerProfit();
    	else
    		return 0.0;
    }
    
    public static double getMatchLiability(Match match) {
    	if (matchMarketData.containsKey(match))
    		return matchMarketData.get(match).getLiability();
    	return 0;
    }
    
    public static double getTotalLiabiltiy() {
    	double totalLiability = 0;
    	for (Match match : matchMarketData.keySet()) {
    		totalLiability += matchMarketData.get(match).getLiability();
    	}
    	return totalLiability;
    }
    
	public static boolean isValidPrice(Double odds) {
		if (odds > 1000)
			return false;
		if (odds >= 20 && (Math.floor(odds) != odds) )
			return false;
		if (odds >= 100)
			return odds % 10 == 0;
		if (odds >= 50)
			return odds % 5 == 0;
		if (odds >= 30)
			return odds % 2 == 0;
		if (odds >= 20)
			return true;
		if (odds >= 4 && (Math.floor(odds * 10) != odds * 10))
			return false;
		if (odds >= 10)
			return (odds * 10) % 5 == 0;
		if (odds >= 6)
			return (odds * 10) % 2 == 0;
		if (odds >= 4)
			return true;
		if (odds > 1 && (Math.floor(odds * 100) != odds * 100))
			return false;
		if (odds >= 3)
			return (odds * 100) % 5 == 0;
		if (odds >= 2)
			return (odds * 100) % 2 == 0;
		if (odds > 1)
			return true;
		return false;
	}
	
	public static void setMatchMarketData(
			ConcurrentHashMap<Match, VirtualBetMarketInfo> matchMarketData) {
		BetManager.matchMarketData = matchMarketData;
	}
}