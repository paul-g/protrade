package org.ic.tennistrader.model;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.utils.Pair;

public abstract class SeriesComputer {
	public abstract double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex);
	/*{
		if (chartData != null) {
			if (player.equals(PlayerEnum.PLAYER1)) {
				return computeValues(chartData.getPl1YSeries(), startIndex);
			}
			else
			{
				return computeValues(chartData.getPl2YSeries(), startIndex);
			}
		}
		return new double[0];
	}*/

	protected abstract double[] computeValues(ArrayList<Double> oldValues,
			int startIndex);

	public double[] computePlusErrors(PlayerEnum player, ChartData chartData,
			int startIndex) {		
		ArrayList<Double> ySeries = setYSeries(player, chartData);
		ArrayList<Pair<Double, Double>> layValues = setLayValues(player, chartData);
		int size = ySeries.size() - startIndex;
		double[] errors = new double[size];
		for(int i = 0; i < size; i++) {
			errors[i] = layValues.get(startIndex + i).first() 
			- ySeries.get(startIndex + i);
		}
		return errors;
	}

	public double[] computeMinusErrors(PlayerEnum player, ChartData chartData,
			int startIndex) {
		int size = chartData.getPl1YSeries().size() - startIndex;
		double[] errors = new double[size];
		ArrayList<Double> ySeries = setYSeries(player, chartData);
		ArrayList<Pair<Double, Double>> layValues = setLayValues(player, chartData);		
		for(int i = 0; i < size; i++) {
			errors[i] = ySeries.get(startIndex + i) - layValues.get(startIndex + i).second();
		}
		return errors;
	}

	private ArrayList<Pair<Double, Double>> setLayValues(PlayerEnum player, ChartData chartData) {
		if (player.equals(PlayerEnum.PLAYER1)) {
			return chartData.getPl1Lay();
		} else {
			return chartData.getPl2Lay();
		}
	}
	
	private ArrayList<Double> setYSeries(PlayerEnum player, ChartData chartData) {
		if (player.equals(PlayerEnum.PLAYER1)) {
			return chartData.getPl1YSeries();
		} else {
			return chartData.getPl2YSeries();
		}
	}

	/*
	public abstract double[] addValue(PlayerEnum player, ChartData chartData,
			double player1newValue, double player2newValue);
	*/	
}
