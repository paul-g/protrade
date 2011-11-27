package org.ic.tennistrader.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.BetsDisplay;

import static org.ic.tennistrader.utils.Pair.pair;

public class BetManager {    
    private static List<Bet> matchedBets = new ArrayList<Bet>();
    private static List<Bet> unmatchedBets = new ArrayList<Bet>();
    private static Logger log = Logger.getLogger(BetManager.class);
    
    public static void placeBet(Match match, Player player, BetTypeEnum betType, double odds, double amount) {        
        Bet newBet = new Bet(match, player, betType, pair(odds, amount));
        matchedBets.add(newBet);
        BetsDisplay.addBet(newBet);            
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

	public static void setBetsOutcome(Match match) {
		System.out.println("In BetManager.setBetsOutcome");
		PlayerEnum winner;
		try {
			winner = match.getWinner();
			for (Bet bet : matchedBets) {
				if (bet.getMatch().equals(match)) {
					settleBet(bet, isBetSuccessful(bet, winner));
					BetsDisplay.addSettledBet(bet);
				}
			}
		} catch (MatchNotFinishedException e1) {
			log.info("Trying to settle bets of an unfinished match");
		}
	}

	private static boolean isBetSuccessful(Bet bet, PlayerEnum winner) {
		boolean won;
		if (winner == PlayerEnum.PLAYER1) {
			if ((bet.getPlayer().equals(bet.getMatch().getPlayerOne()) && bet
					.getType().equals(BetTypeEnum.B))
					|| (bet.getPlayer().equals(bet.getMatch().getPlayerTwo()) && bet
							.getType().equals(BetTypeEnum.L))) {
				won = true;
			} else
				won = false;
		} else {
			if ((bet.getPlayer().equals(bet.getMatch().getPlayerOne()) && bet
					.getType().equals(BetTypeEnum.B))
					|| (bet.getPlayer().equals(bet.getMatch().getPlayerTwo()) && bet
							.getType().equals(BetTypeEnum.L))) {
				won = false;
			} else
				won = true;
		}
		return won;
	}
	
	private static void settleBet(Bet bet, boolean betSuccessful) {
		if (betSuccessful) {
			bet.setIncome( bet.getOdds() * bet.getAmount() );
			/*
			if (bet.getType().equals(BetTypeEnum.B))
				bet.setIncome( bet.getOdds() * bet.getAmount() );
			else
				bet.setIncome( bet.getOdds() * bet.getAmount() );
			*/
		} else {
			if (bet.getType().equals(BetTypeEnum.B))
				bet.setIncome( (-1) * bet.getAmount() );
			else
				bet.setIncome( (-1) * (bet.getOdds() - 1) * bet.getAmount() );
		}		
	}
}