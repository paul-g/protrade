package org.ic.protrade.model.chart_computers;

import java.util.ArrayList;

import org.ic.protrade.domain.ChartData;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.utils.Pair;

public abstract class SeriesComputer {
	public abstract double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex, boolean inverted);
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

	/*
	protected abstract double[] computeValues(ArrayList<Double> oldValues,
			int startIndex);
	*/
	
	public double[] computePlusErrors(PlayerEnum player, ChartData chartData,
			int startIndex, boolean inverted) {
		ArrayList<Double> ySeries = setYSeries(player, chartData);
		ArrayList<Pair<Double, Double>> layValues = setLayValues(player,
				chartData);
		int size = (ySeries.size() < layValues.size() ? ySeries.size()
				: layValues.size())
				- startIndex;
		double[] errors = new double[size + 1];
		for (int i = 0; i < size; i++) {
			if (inverted)
				errors[i] = (Math
						.pow(layValues.get(startIndex + i).first(), -1) - Math
						.pow(ySeries.get(startIndex + i), -1)) * 100;
			else
				errors[i] = layValues.get(startIndex + i).first()
						- ySeries.get(startIndex + i);
		}
		return errors;
	}

	public double[] computeMinusErrors(PlayerEnum player, ChartData chartData,
			int startIndex, boolean inverted) {
		ArrayList<Double> ySeries = setYSeries(player, chartData);
		ArrayList<Pair<Double, Double>> layValues = setLayValues(player,
				chartData);
		int size = (ySeries.size() < layValues.size() ? ySeries.size()
				: layValues.size())
				- startIndex;
		double[] errors = new double[size];
		for (int i = 0; i < size; i++) {
			if (inverted)
				errors[i] = (Math.pow(ySeries.get(startIndex + i), -1) - Math
						.pow(layValues.get(startIndex + i).second(), -1)) * 100;
			else
				errors[i] = ySeries.get(startIndex + i)
						- layValues.get(startIndex + i).second();
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
