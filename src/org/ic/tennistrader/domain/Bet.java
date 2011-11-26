package org.ic.tennistrader.domain;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.utils.Pair;

public class Bet {
    private Pair<Double, Double> value;
    private Match match;
    private Player player;
    private BetTypeEnum type;
    private double income; // includes profit and stake

    public Bet(Match m, Player p, BetTypeEnum type, Pair<Double, Double> value){
        this.match = m;
        this.player = p;
        this.type = type;
        this.value = value;
        this.income = 0;
    }
    
    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BetTypeEnum getType() {
        return type;
    }

    public void setType(BetTypeEnum betType) {
        this.type = betType;
    }
    
    
    public void setValue(Pair<Double, Double> value) {
        this.value = value;
    }

    /*
    public Pair<Double, Double> getValue() {
        return value;
    }
    */
    
    public double getAmount() {
    	return this.value.second();
    }
    
    public double getOdds() {
    	return this.value.first();
    }

	public void setIncome(double profit) {
		this.income = profit;
	}

	public double getIncome() {
		return income;
	}
}
