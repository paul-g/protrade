package org.ic.tennistrader.model;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public class BackValuesComputer extends SeriesComputer {

	@Override
	public double[] computeValues(ArrayList<Double> oldValues) {
		double[] values = new double[oldValues.size()];
		int i = 0;
		for (Double val : oldValues)
			values[i++] = val;
		return values;
	}

	@Override
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
