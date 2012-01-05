package org.ic.tennistrader.model;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public class BackValuesComputer extends SeriesComputer {
	@Override
	public double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex) {
		double[] values = new double[0];
		ArrayList<Double> oldValues;
		if (chartData != null) {
			if (player.equals(PlayerEnum.PLAYER1)) {
				oldValues = chartData.getPl1YSeries();
			}
			else {
				oldValues = chartData.getPl2YSeries();
			}
			values = new double[oldValues.size() - startIndex];
			for (int i = startIndex; i < oldValues.size(); i++)
				values[i - startIndex] = oldValues.get(i);
		}
		return values;
	}
	
	@Override
	public double[] computeValues(ArrayList<Double> oldValues, int startIndex) {
		double[] values = new double[oldValues.size() - startIndex];
		for (int i = startIndex; i < oldValues.size(); i++)
			values[i - startIndex] = oldValues.get(i);
		return values;
	}

	//@Override
	public double[] addValue(PlayerEnum player, ChartData chartData, double player1newValue, double player2newValue) {
		ArrayList<Double> oldValues;
		double newValue;
		if (player.equals(PlayerEnum.PLAYER1)) {
			oldValues = chartData.getPl1YSeries();
			newValue = player1newValue;
		}
		else {
			oldValues = chartData.getPl2YSeries();
			newValue = player2newValue;
		}
		double[] values = new double[oldValues.size() + 1];
		int i = 0;
		for (i = 0; i < oldValues.size(); i++)
			values[i] = oldValues.get(i);
		values[i] = newValue;
		return values;
	}

	

}
