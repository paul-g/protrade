package org.ic.tennistrader.model;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public abstract class SeriesComputer {
	public double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex) {
		if (chartData != null) {
			if (player.equals(PlayerEnum.PLAYER1))
				return computeValues(chartData.getPl1YSeries(), startIndex);
			else
				return computeValues(chartData.getPl2YSeries(), startIndex);
		}
		return new double[0];
	}

	protected abstract double[] computeValues(ArrayList<Double> oldValues,
			int startIndex);

	public abstract double[] addValue(PlayerEnum player, ChartData chartData,
			double player1newValue, double player2newValue);
}
