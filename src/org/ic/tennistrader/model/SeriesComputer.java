package org.ic.tennistrader.model;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public abstract class SeriesComputer {
	public double[] computeValues(PlayerEnum player, ChartData chartData) {
		if (player.equals(PlayerEnum.PLAYER1))
			return computeValues(chartData.getPl1YSeries());
		else
			return computeValues(chartData.getPl2YSeries());
	}

	public abstract double[] computeValues(ArrayList<Double> oldValues);

	public abstract double[] addValue(PlayerEnum player, ChartData chartData,
			double player1newValue, double player2newValue);
}
