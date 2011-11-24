package org.ic.tennistrader.model;

import java.util.ArrayList;
import java.util.List;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.ui.BetsDisplay;

import static org.ic.tennistrader.utils.Pair.pair;

public class BetManager {    
    private static List<Bet> matchedBets = new ArrayList<Bet>();
    private static List<Bet> unmatchedBets = new ArrayList<Bet>();
    
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
}