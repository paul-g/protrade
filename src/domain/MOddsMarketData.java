package src.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import src.Pair;

public class MOddsMarketData {

	ArrayList<Pair<Double,Double>> pl1Lay;
	ArrayList<Pair<Double,Double>> pl1Back;
	ArrayList<Pair<Double,Double>> pl2Lay;
	ArrayList<Pair<Double,Double>> pl2Back;
	Date date;
	String Exchange;
	String matchStatus;
	String location;
	
	public MOddsMarketData(){
		
	}

	public ArrayList<Pair<Double,Double>> getPl1Lay() {
		return pl1Lay;
	}

	public void setPl1Lay(ArrayList<Pair<Double,Double>> pl1Lay) {
		this.pl1Lay = pl1Lay;
	}

	public ArrayList<Pair<Double,Double>> getPl1Back() {
		return pl1Back;
	}

	public void setPl1Back(ArrayList<Pair<Double,Double>> pl1Back) {
		this.pl1Back = pl1Back;
	}

	public ArrayList<Pair<Double,Double>> getPl2Lay() {
		return pl2Lay;
	}

	public void setPl2Lay(ArrayList<Pair<Double,Double>> pl2Lay) {
		this.pl2Lay = pl2Lay;
	}

	public ArrayList<Pair<Double,Double>> getPl2Back() {
		return pl2Back;
	}

	public void setPl2Back(ArrayList<Pair<Double,Double>> pl2Back) {
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
	
	
}
