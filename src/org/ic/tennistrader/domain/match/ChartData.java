package org.ic.tennistrader.domain.match;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.ic.tennistrader.domain.MOddsMarketData;

import org.ic.tennistrader.utils.Pair;
import static org.ic.tennistrader.utils.Pair.pair;

public class ChartData {
	private ArrayList<Double> pl1YSeries;
	private ArrayList<Double> pl2YSeries;
	private ArrayList<Pair<Double,Double>> pl1Lay;
	private ArrayList<Pair<Double,Double>> pl2Lay;
	private ArrayList<Double> maPl1;
	private ArrayList<Double> maPl2;
	private ArrayList<Date> xSeries;
	private ArrayList<Double> backOverround;
	private ArrayList<Double> layOverround;
	private ArrayList<Double> pl1Volume;
	private ArrayList<Double> pl2Volume;
	private int dataSize;
	
	public ArrayList<Integer> endOfSets;
	
	public ChartData(){
		dataSize = 0;
		pl1YSeries = new ArrayList<Double>();
		pl2YSeries = new ArrayList<Double>();
		pl1Lay = new ArrayList<Pair<Double,Double>>();
		pl2Lay = new ArrayList<Pair<Double,Double>>();
		maPl1 = new ArrayList<Double>(); 
		maPl2 = new ArrayList<Double>();
		xSeries = new ArrayList<Date>();
		backOverround = new ArrayList<Double>();
		layOverround = new ArrayList<Double>();
		pl1Volume = new ArrayList<Double>();
		pl2Volume = new ArrayList<Double>();
		endOfSets = new ArrayList<Integer>();
	}
	
	public void updateData(MOddsMarketData data){
		xSeries.add(this.getDataSize(), Calendar.getInstance().getTime());
		pl1YSeries = addValue(pl1YSeries,  data.getPl1LastMatchedPrice());
		pl2YSeries = addValue(pl2YSeries,  data.getPl2LastMatchedPrice());
		pl1Lay = addLay(pl1Lay, data.getPl1Back(),data.getPl1Lay());
		pl2Lay = addLay(pl2Lay, data.getPl2Back(),data.getPl2Lay());
		maPl1 = addMaValue(maPl1, pl1YSeries);
		maPl2 = addMaValue(maPl2, pl2YSeries);
		backOverround = addOverround(backOverround, data.getPl1Back(), data.getPl2Back());
		layOverround = addOverround(layOverround, data.getPl1Lay(), data.getPl2Lay());
		pl1Volume = addVolume(pl1Volume, data.getPlayer1TotalAmountMatched());
		pl2Volume = addVolume(pl2Volume, data.getPlayer2TotalAmountMatched());
		setDataSize(pl1YSeries.size());
		addEndOfSet();
		
	}
	
	
	private void addEndOfSet() {
		if (getDataSize() % 10 == 0) {
			endOfSets.add(1);
		} else endOfSets.add(0);
		
		
	}

	private ArrayList<Double> addVolume(ArrayList<Double> volume,
			double value) {
		volume.add(value);
		return volume;
	}

	private ArrayList<Double> addOverround(ArrayList<Double> overround,
			ArrayList<Pair<Double, Double>> back,
			ArrayList<Pair<Double, Double>> lay) {
		int i = this.getDataSize();
		if (back != null && lay != null && back.size() > 0 && lay.size() > 0) {
			overround.add(100* (1/back.get(0).first() + 1/lay.get(0).first()));
		} else {
			if (i > 0) // keep previous value if it exists
				overround.add(i,overround.get(i-1));
			else
				// put zero if no previous value
				overround.add(i,0.0);
		}		
		return overround;
	}

	private ArrayList<Pair<Double, Double>> addLay(ArrayList<Pair<Double, Double>> array, 
			ArrayList<Pair<Double, Double>> back, ArrayList<Pair<Double, Double>> lay) {
		int i = this.getDataSize();
		double plus;
		double minus;
		// if data has been read from Betfair
		if (back != null && lay != null && back.size() > 0 && lay.size() > 0) {
			minus = back.get(back.size()-1).first();
			plus = lay.get(lay.size()-1).first();
			array.add( pair(plus,minus));
		} else {
			if (i > 0) // keep previous value if it exists
				array.add(i,array.get(i-1));
			else
				// put zero if no previous value
				array.add(i, pair(0.0,0.0));
		}
		return array;
	}
	

	private ArrayList<Double> addMaValue(ArrayList<Double> maPl12, 
			ArrayList<Double> pl1ySeries2) {
		int i = this.getDataSize();
		if (pl1ySeries2 != null) {
			double sum = 0;
			if (i < 10) {
				for (int a = i; a >= 0; a--)
					sum += pl1ySeries2.get(a);
				maPl12.add(i, sum / (i + 1));
			} else {
				sum = maPl12.get(i - 1) * 10 - pl1ySeries2.get(i - 10)
						+ pl1ySeries2.get(i);
				maPl12.add(i, sum / 10);
			}
		}
		return maPl12;
	}

	private ArrayList<Double> addValue(ArrayList<Double> pl1ySeries2, 
			double d) {
		int i = this.getDataSize();
		pl1ySeries2.add(i, d);
		return pl1ySeries2;
	}

	
	public ArrayList<Double> getPl1YSeries() {
		return pl1YSeries;
	}

	public void setPl1YSeries(ArrayList<Double> pl1ySeries) {
		pl1YSeries = pl1ySeries;
	}

	public ArrayList<Double> getPl2YSeries() {
		return pl2YSeries;
	}

	public void setPl2YSeries(ArrayList<Double> pl2ySeries) {
		pl2YSeries = pl2ySeries;
	}

	public ArrayList<Pair<Double, Double>> getPl1Lay() {
		return pl1Lay;
	}

	public void setPl1Lay(ArrayList<Pair<Double, Double>> pl1Lay) {
		this.pl1Lay = pl1Lay;
	}

	public ArrayList<Pair<Double, Double>> getPl2Lay() {
		return pl2Lay;
	}

	public void setPl2Lay(ArrayList<Pair<Double, Double>> pl2Lay) {
		this.pl2Lay = pl2Lay;
	}

	public ArrayList<Double> getMaPl1() {
		return maPl1;
	}

	public void setMaPl1(ArrayList<Double> maPl1) {
		this.maPl1 = maPl1;
	}

	public ArrayList<Double> getMaPl2() {
		return maPl2;
	}

	public void setMaPl2(ArrayList<Double> maPl2) {
		this.maPl2 = maPl2;
	}

	public ArrayList<Date> getxSeries() {
		return xSeries;
	}

	public void setxSeries(ArrayList<Date> xSeries) {
		this.xSeries = xSeries;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getDataSize() {
		return dataSize;
	}	

	public ArrayList<Double> getBackOverround() {
		return backOverround;
	}

	public void setBackOverround(ArrayList<Double> backOverround) {
		this.backOverround = backOverround;
	}

	public ArrayList<Double> getLayOverround() {
		return layOverround;
	}

	public void setLayOverround(ArrayList<Double> layOverround) {
		this.layOverround = layOverround;
	}

	public ArrayList<Double> getPl1Volume() {
		return pl1Volume;
	}

	public void setPl1Volume(ArrayList<Double> pl1Volume) {
		this.pl1Volume = pl1Volume;
	}

	public ArrayList<Double> getPl2Volume() {
		return pl2Volume;
	}

	public void setPl2Volume(ArrayList<Double> pl2Volume) {
		this.pl2Volume = pl2Volume;
	}

	
	
}
