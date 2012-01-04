package org.ic.tennistrader.model;

import java.util.ArrayList;
import java.util.List;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public class MAComputer extends SeriesComputer {
	@Override
	public double[] computeValues(ArrayList<Double> oldValues) {
		int size = oldValues.size();
		double[] values = new double[size];
		int i;
		double sum = 0;
		for (i = 0; i < 10 && i < size; i++) {
			sum += oldValues.get(i);
			values[i] = sum / (i + 1);
		}
		for (i = 10; i < size; i++) {
			sum = values[i - 1] * 10 - oldValues.get(i - 10)
					+ oldValues.get(i);
			values[i] = sum / 10;
		}
		return values;
	}

	@Override
	public double[] addValue(PlayerEnum player, ChartData chartData, double player1newValue, double player2newValue) {
		ArrayList<Double> oldValues, backValues;
		double newValue;
		if (player.equals(PlayerEnum.PLAYER1)) {
			oldValues = chartData.getMaPl1();
			backValues = chartData.getPl1YSeries();
			newValue = player1newValue;
		}
		else {
			oldValues = chartData.getMaPl2();
			backValues = chartData.getPl2YSeries();
			newValue = player2newValue;
		}
		double[] values = new double[oldValues.size() + 1];
		int i = 0;
		for (i = 0; i < oldValues.size(); i++)
			values[i] = oldValues.get(i);
		double sum = 0;
		i = oldValues.size();
		if (i < 10) {
			for (int a = i - 1; a >= 0; a--) {
				sum += backValues.get(a);
			}
			sum += newValue;
			values[i] = sum / (i + 1);
		} else {
			sum = values[i - 1] * 10 - backValues.get(i - 10) + newValue;
			values[i] = sum / 10;
		}
		return values;
	}
}
