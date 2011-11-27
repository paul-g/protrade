package org.ic.tennistrader.domain;

import java.util.ArrayList;
import java.util.Date;

import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.utils.Pair;

public class MOddsMarketData {
    double pl1MatchedPrice;
    double pl2MatchedPrice;
    double pl1Volume;
    double pl2Volume;
    String Exchange;
    private String player1;
    private String player2;
    private double pl1LastMatchedPrice;
    private double pl2LastMatchedPrice;
    private double player1TotalAmountMatched;
    private double player2TotalAmountMatched;
    private int player1SelectiondId;
    private int player2SelectionId;
    private ArrayList<Pair<Double, Double>> pl1Lay, pl1Back, pl2Lay, pl2Back;
    private Date date;
    String exchange;
    private String matchStatus;
    private String location;
    private int delay;

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
        return exchange;
    }

    public void setExchange(String exchange) {
       this.exchange = exchange;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

	public double getPl1LastMatchedPrice() {
		return pl1LastMatchedPrice;
	}

	public void setPl1LastMatchedPrice(double pl1MatchedPrice) {
		this.pl1LastMatchedPrice = pl1MatchedPrice;
	}

	public double getPl2LastMatchedPrice() {
		return pl2LastMatchedPrice;
	}

	public void setPl2LastMatchedPrice(double pl2MatchedPrice) {
		this.pl2LastMatchedPrice = pl2MatchedPrice;
	}
	
	public String getBackValues(PlayerEnum player){
	    switch (player){
	        case PLAYER1: return getAsCsvString(getPl1Back());
	        case PLAYER2: return getAsCsvString(getPl2Back());
	    }
	    return "";
	}
	
	public String getLayValues(PlayerEnum player){
	    switch (player){
            case PLAYER1: return getAsCsvString(getPl1Lay());
            case PLAYER2: return getAsCsvString(getPl2Lay());
        }
        return "";
	}
	
	private String getAsCsvString(ArrayList<Pair<Double, Double>> list){
	    String l = list.toString();
	    return l.substring(1, l.length() - 1);
	}
	
	public double getLastPriceMatched(PlayerEnum player){
	    switch (player){
            case PLAYER1: return pl1LastMatchedPrice;
            case PLAYER2: return pl2LastMatchedPrice;
        }
        return -1.0;
	}
	
	public double getPlayer1TotalAmountMatched() {
		return player1TotalAmountMatched;
	}

	public void setPlayer1TotalAmountMatched(double player1TotalAmountMatched) {
		this.player1TotalAmountMatched = player1TotalAmountMatched;
	}

	public double getPlayer2TotalAmountMatched() {
		return player2TotalAmountMatched;
	}

	public double getPl1Volume() {
		return pl1Volume;
	}

	public void setPl1Volume(double pl1Volume) {
		this.pl1Volume = pl1Volume;
	}

	public double getPl2Volume() {
		return pl2Volume;
	}

	public void setPl2Volume(double pl2Volume) {
		this.pl2Volume = pl2Volume;
	}

	public void setPlayer2TotalAmountMatched(double player2TotalAmountMatched) {
		this.player2TotalAmountMatched = player2TotalAmountMatched;
	}
	
	public int getPlayer1SelectiondId() {
		return player1SelectiondId;
	}

	public void setPlayer1SelectiondId(int player1SelectiondId) {
		this.player1SelectiondId = player1SelectiondId;
	}

	public int getPlayer2SelectionId() {
		return player2SelectionId;
	}

	public void setPlayer2SelectionId(int player2SelectionId) {
		this.player2SelectionId = player2SelectionId;
	}
}
