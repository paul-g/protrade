package org.ic.tennistrader.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;
import org.ic.tennistrader.utils.Pair;

public class BetManager {    
    private static List<Bet> matchedBets = new ArrayList<Bet>();
    private static List<Bet> unmatchedBets = new ArrayList<Bet>();
    private static HashMap<UpdatableMarketDataGrid, Match> matches = new HashMap<UpdatableMarketDataGrid, Match>();
    
    public static void placeBet(UpdatableMarketDataGrid grid, boolean player1, BetTypeEnum betType, double odds, double amount) {
        Match match = matches.get(grid);
        Bet newBet = new Bet(match, player1 ? match.getPlayerOne() : match.getPlayerTwo(), betType, new Pair<Double, Double>(odds, amount));
        matchedBets.add(newBet);
        System.out.println("Placed bet " + amount + "@ " + odds + " as " + betType + " for " + (player1?"pl1":"pl2"));
    }
    
    public static void registerGrid(Match match, UpdatableMarketDataGrid table) {
        matches.put(table, match);
    }
    
    
    
    
    
    private static List<Pair<Double, Double>>  activeBets = new ArrayList<Pair<Double,Double>>();
    
    public static List<Pair<Double, Double>> getActiveBets() {
        return activeBets;
    }

    public static void addBet(double odds, double amount){
        activeBets.add(new Pair<Double, Double>(odds, amount));
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