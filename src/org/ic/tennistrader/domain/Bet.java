package org.ic.tennistrader.domain;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.utils.Pair;

public class Bet {
    private Pair<Double, Double> value;
    private double unmatchedValue;
    private Match match;
    private PlayerEnum player;
    private BetTypeEnum type;
    private double profit;
    private double firstPlayerWinnerProfit;
    private double secondPlayerWinnerProfit;

    public Bet(Match m, PlayerEnum p, BetTypeEnum type, Pair<Double, Double> value){
        this.match = m;
        this.player = p;
        this.type = type;
        this.value = value;
        this.profit = 0;
        this.unmatchedValue = 0;
    }
    
    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public PlayerEnum getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEnum player) {
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

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getProfit() {
		return profit;
	}

	public void setFirstPlayerWinnerProfit(double firstPlayerWinnerProfit) {
		this.firstPlayerWinnerProfit = firstPlayerWinnerProfit;
	}

	public double getFirstPlayerWinnerProfit() {
		return firstPlayerWinnerProfit;
	}

	public void setSecondPlayerWinnerProfit(double secondPlayerWinnerProfit) {
		this.secondPlayerWinnerProfit = secondPlayerWinnerProfit;
	}

	public double getSecondPlayerWinnerProfit() {
		return secondPlayerWinnerProfit;
	}

    public void setUnmatchedValue(double unmatchedValue) {
        this.unmatchedValue = unmatchedValue;
    }

    public double getUnmatchedValue() {
        return unmatchedValue;
    }
}
