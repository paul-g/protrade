package org.ic.tennistrader.domain.markets;

import java.util.ArrayList;
import java.util.Date;

import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.utils.Pair;

public class MOddsMarketData {
	private String player1;
	private String player2;
	private double pl1LastMatchedPrice;
	private double pl2LastMatchedPrice;
	private double player1TotalAmountMatched;
	private double player2TotalAmountMatched;
	private int player1SelectiondId;
	private int player2SelectionId;
	private double player1PredictedOdds;
	private double player2PredictedOdds;
	private ArrayList<Pair<Double, Double>> pl1Lay, pl1Back, pl2Lay, pl2Back;
	private Date date;
	private String exchange;
	private String matchStatus;
	private String location;
	private int delay;

	public MOddsMarketData() {
		this.delay = 0;
		pl1Lay = new ArrayList<Pair<Double, Double>>();
		pl1Back = new ArrayList<Pair<Double, Double>>();
		pl2Lay = new ArrayList<Pair<Double, Double>>();
		pl2Back = new ArrayList<Pair<Double, Double>>();
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

	public String getBackValues(PlayerEnum player) {
		if (PlayerEnum.PLAYER1.equals(player)) {
			return getAsCsvString(getPl1Back());
		}
		return getAsCsvString(getPl2Back());
	}

	public String getLayValues(PlayerEnum player) {
		if (PlayerEnum.PLAYER1.equals(player)) {
			return getAsCsvString(getPl1Lay());
		}
		return getAsCsvString(getPl2Lay());
	}

	private String getAsCsvString(ArrayList<Pair<Double, Double>> list) {
		if (list != null) {
			String l = list.toString();
			return l.substring(1, l.length() - 1);
		}
		return "";
	}

	public double getLastPriceMatched(PlayerEnum player) {
		if (PlayerEnum.PLAYER1.equals(player)) {
			return pl1LastMatchedPrice;
		}
		return pl2LastMatchedPrice;
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
	
	public double getplayer1PredictedOdds() {
		return player1PredictedOdds;
	}
	
	public void setplayer1PredictedOdds(double player1PredictedOdds) {
		System.out.println("called?");
		if (player1PredictedOdds == 0) {
			this.player1PredictedOdds = 0.00001;
		} else {
			this.player1PredictedOdds = player1PredictedOdds;
		}
	}
	
	public double getplayer2PredictedOdds() {
		return player2PredictedOdds;
	}
	
	public void setplayer2PredictedOdds(double player2PredictedOdds) {
		if (player2PredictedOdds == 0) {
			this.player2PredictedOdds = 0.00001;
		} else {
			this.player2PredictedOdds = player2PredictedOdds;
		}
	}
}
