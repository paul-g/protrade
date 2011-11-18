package org.ic.tennistrader.service;

import java.util.ArrayList;
import java.util.List;

import org.ic.tennistrader.utils.Pair;

public class BetManager {
    
    private static List<Pair<Double, Double>>  activeBets = new ArrayList<Pair<Double,Double>>();
    
    public static List<Pair<Double, Double>> getActiveBets() {
        return activeBets;
    }

    public static void addBet(double odds, double amount){
        activeBets.add(new Pair<Double, Double>(odds, amount));
    }
}