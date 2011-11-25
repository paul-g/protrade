package org.ic.tennistrader.domain;

import java.util.ArrayList;
import java.util.Date;

import org.ic.tennistrader.utils.Pair;

public class MOddsMarketData {
    String player1;
    String player2;
    double pl1MatchedPrice;
    double pl2MatchedPrice;
    ArrayList<Pair<Double, Double>> pl1Lay;
    ArrayList<Pair<Double, Double>> pl1Back;
    ArrayList<Pair<Double, Double>> pl2Lay;
    ArrayList<Pair<Double, Double>> pl2Back;
    Date date;
    String Exchange;
    String matchStatus;
    String location;
    int delay;

    public MOddsMarketData() {
        this.delay = 0;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public ArrayList<Pair<Double, Double>> getPl1Lay() {
        return pl1Lay;
    }

    public void setPl1Lay(ArrayList<Pair<Double, Double>> pl1Lay) {
        this.pl1Lay = pl1Lay;
    }

    public ArrayList<Pair<Double, Double>> getPl1Back() {
        return pl1Back;
    }

    public void setPl1Back(ArrayList<Pair<Double, Double>> pl1Back) {
        this.pl1Back = pl1Back;
    }

    public ArrayList<Pair<Double, Double>> getPl2Lay() {
        return pl2Lay;
    }

    public void setPl2Lay(ArrayList<Pair<Double, Double>> pl2Lay) {
        this.pl2Lay = pl2Lay;
    }

    public ArrayList<Pair<Double, Double>> getPl2Back() {
        return pl2Back;
    }

    public void setPl2Back(ArrayList<Pair<Double, Double>> pl2Back) {
        this.pl2Back = pl2Back;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String exchange) {
        Exchange = exchange;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

	public double getPl1MatchedPrice() {
		return pl1MatchedPrice;
	}

	public void setPl1MatchedPrice(double pl1MatchedPrice) {
		this.pl1MatchedPrice = pl1MatchedPrice;
	}

	public double getPl2MatchedPrice() {
		return pl2MatchedPrice;
	}

	public void setPl2MatchedPrice(double pl2MatchedPrice) {
		this.pl2MatchedPrice = pl2MatchedPrice;
	}
    
    

}
