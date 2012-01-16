package org.ic.tennistrader.domain;

import static org.ic.tennistrader.utils.Pair.pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.score.PredictionCalculator;
import org.ic.tennistrader.utils.Pair;

public class ChartData {
	private ArrayList<Double> pl1YSeries;
	private ArrayList<Double> pl2YSeries;
	private ArrayList<Double> pl1Predicted;
	private ArrayList<Double> pl2Predicted;
	private ArrayList<Pair<Double, Double>> pl1Lay;
	private ArrayList<Pair<Double, Double>> pl2Lay;
	private ArrayList<Double> maPl1;
	private ArrayList<Double> maPl2;
	private ArrayList<Date> xSeries;
	private ArrayList<Double> backOverround;
	private ArrayList<Double> layOverround;
	private ArrayList<Double> pl1Volume;
	private ArrayList<Double> pl2Volume;
	private double pl1TotalPrevVol, pl2TotalPrevVol;
	private int dataSize;
	private final Match match;
	private int prevNrSets;

	private final ArrayList<Integer> endOfSets;

	public ChartData(Match match) {
		this.match = match;
		try {
			prevNrSets = match.getScore().getPlayerOneSets()
					+ match.getScore().getPlayerTwoSets();
		} catch (NullPointerException e) {
			prevNrSets = 0;
		}
		dataSize = 0;
		
		pl1YSeries = new ArrayList<Double>();
		pl2YSeries = new ArrayList<Double>();
		pl1Predicted = new ArrayList<Double>();
		pl2Predicted = new ArrayList<Double>();
		pl1Lay = new ArrayList<Pair<Double, Double>>();
		pl2Lay = new ArrayList<Pair<Double, Double>>();
		maPl1 = new ArrayList<Double>();
		maPl2 = new ArrayList<Double>();
		xSeries = new ArrayList<Date>();
		backOverround = new ArrayList<Double>();
		layOverround = new ArrayList<Double>();
		pl1Volume = new ArrayList<Double>();
		pl2Volume = new ArrayList<Double>();
		endOfSets = new ArrayList<Integer>();

		/*
		pl1YSeries.add(0.0);
		pl2YSeries.add(0.0);
		pl1Predicted.add(0.0);
		pl2Predicted.add(0.0);
		pl1Lay.add(pair(0.0, 0.0));
		pl2Lay.add(pair(0.0, 0.0));
		maPl1.add(0.0);
		maPl2.add(0.0);
		xSeries.add(Calendar.getInstance().getTime());
		backOverround.add(0.0);
		layOverround.add(0.0);
		pl1Volume.add(0.0);
		pl2Volume.add(0.0);
		endOfSets.add(0);
		*/
	}

	public void updateData(MOddsMarketData data) {
		// xSeries = addTime(xSeries, data.getDate());
		xSeries.add(this.getDataSize(), Calendar.getInstance().getTime());

		pl1YSeries = addValue(pl1YSeries, data.getPl1LastMatchedPrice());
		pl2YSeries = addValue(pl2YSeries, data.getPl2LastMatchedPrice());

		updatePrediction();

		pl1Lay = addLay(pl1Lay, data.getPl1Back(), data.getPl1Lay());
		pl2Lay = addLay(pl2Lay, data.getPl2Back(), data.getPl2Lay());
		maPl1 = addMaValue(maPl1, pl1YSeries);
		maPl2 = addMaValue(maPl2, pl2YSeries);
		backOverround = addOverround(backOverround, data.getPl1Back(),
				data.getPl2Back());
		layOverround = addOverround(layOverround, data.getPl1Lay(),
				data.getPl2Lay());
		pl1Volume = addVolume(pl1Volume, data.getPlayer1TotalAmountMatched(), 1);
		pl2Volume = addVolume(pl2Volume, data.getPlayer2TotalAmountMatched(), 2);
		setDataSize(pl1YSeries.size());
		checkScoreEndOfSet();
	}

	private void updatePrediction() {
		try {
			double[] result = new PredictionCalculator(match)
					.calculateOddsWithStaticPWOS(match);
			pl1Predicted = addValue(pl1Predicted, result[0]);
			pl2Predicted = addValue(pl2Predicted, result[1]);
		} catch (NullPointerException e) {
			// System.out.println("NULL");
			pl1Predicted = addValue(pl1Predicted, 0.0001);
			pl2Predicted = addValue(pl2Predicted, 0.0001);
		}
	}

	private ArrayList<Date> addTime(ArrayList<Date> xSeries, Date date) {
		int i = this.getDataSize();
		if (date == null) {
			if (i == 0)
				xSeries.add(i, new Date());
			else
				xSeries.add(i, xSeries.get(i - 1));
		} else {
			xSeries.add(i, date);
		}
		return xSeries;
	}

	public ArrayList<Integer> getEndOfSets() {
		return endOfSets;
	}

	private double[] calcPrediction(double[] predicted) {
		double pl1Pred = predicted[8];
		if (pl1Pred == 0)
			pl1Pred = 0.0001;
		double pl2Pred = predicted[9];
		if (pl2Pred == 0)
			pl2Pred = 0.0001;
		// TODO Auto-generated method stub
		return new double[] { pl1Pred, pl2Pred };
	}

	public void addMarketEndOfSet() {
		// System.out.println("Adding end of set from market");
		endOfSets.add(1);
	}

	private void checkScoreEndOfSet() {
		int nrSets;
		try {
			nrSets = match.getScore().getPlayerOneSets()
					+ match.getScore().getPlayerTwoSets();
		} catch (NullPointerException e) {
			nrSets = prevNrSets;
		}
		if (prevNrSets < nrSets) {
			// System.out.println("Adding end of set from score");
			endOfSets.add(1);
			prevNrSets = nrSets;
		} else
			endOfSets.add(0);

	}

	private ArrayList<Double> addVolume(ArrayList<Double> volume, double value,
			int selection) {
		if (getDataSize() == 0) {
			if (selection == 1)
				pl1TotalPrevVol = value;
			if (selection == 2)
				pl2TotalPrevVol = value;
			volume.add(0.0);
			return volume;
		}
		double prev = 0;
		if (selection == 1) {
			prev = pl1TotalPrevVol;
			volume.add(value - prev);
			pl1TotalPrevVol = value;
		} else if (selection == 2) {
			prev = pl2TotalPrevVol;
			volume.add(value - prev);
			pl2TotalPrevVol = value;
		}

		return volume;
	}

	private ArrayList<Double> addOverround(ArrayList<Double> overround,
			ArrayList<Pair<Double, Double>> back,
			ArrayList<Pair<Double, Double>> lay) {
		int i = this.getDataSize();
		if (back != null && lay != null && !back.isEmpty() && !lay.isEmpty()) {
			overround.add(100 * (1 / back.get(0).first() + 1 / lay.get(0)
					.first()));
		} else {
			if (i > 0) {
				// keep previous value if it exists
				overround.add(i, overround.get(i - 1));
			} else {
				// put zero if no previous value
				overround.add(i, 0.0);
			}
		}
		return overround;
	}

	private ArrayList<Pair<Double, Double>> addLay(
			ArrayList<Pair<Double, Double>> array,
			ArrayList<Pair<Double, Double>> back,
			ArrayList<Pair<Double, Double>> lay) {
		int i = this.getDataSize();
		double plus;
		double minus;
		// if data has been read from Betfair
		if (back != null && lay != null && !back.isEmpty() && !lay.isEmpty()) {
			minus = back.get(back.size() - 1).first();
			plus = lay.get(lay.size() - 1).first();
			array.add(pair(plus, minus));
		} else {
			if (i > 0) {
				// keep previous value if it exists
				array.add(i, array.get(i - 1));
			} else {
				// put zero if no previous value
				array.add(i, pair(0.0, 0.0));
			}
		}
		return array;
	}

	private ArrayList<Double> addMaValue(ArrayList<Double> maPl12,
			ArrayList<Double> pl1ySeries2) {
		int i = this.getDataSize();
		if (pl1ySeries2 != null) {
			double sum = 0;
			if (i < 10) {
				for (int a = i; a >= 0; a--) {
					sum += pl1ySeries2.get(a);
				}
				maPl12.add(i, sum / (i + 1));
			} else {
				sum = maPl12.get(i - 1) * 10 - pl1ySeries2.get(i - 10)
						+ pl1ySeries2.get(i);
				maPl12.add(i, sum / 10);
			}
		}
		return maPl12;
	}

	private ArrayList<Double> addValue(ArrayList<Double> series, double d) {
		int i = this.getDataSize();
		series.add(i, d);
		return series;
	}

	public ArrayList<Double> getPl1YSeries() {
		return pl1YSeries;
	}

	public ArrayList<Double> getPl2YSeries() {
		return pl2YSeries;
	}

	public ArrayList<Pair<Double, Double>> getPl1Lay() {
		return pl1Lay;
	}

	public ArrayList<Pair<Double, Double>> getPl2Lay() {
		return pl2Lay;
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

	public ArrayList<Double> getPl1Predicted() {
		return pl1Predicted;
	}

	public void setPl1Predicted(ArrayList<Double> pl1Predicted) {
		this.pl1Predicted = pl1Predicted;
	}

	public ArrayList<Double> getPl2Predicted() {
		return pl2Predicted;
	}

	public void setPl2Predicted(ArrayList<Double> pl2Predicted) {
		this.pl2Predicted = pl2Predicted;
	}

}