package org.ic.tennistrader.model.chart_computers;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public class PredictedComputer extends SeriesComputer {

	@Override
	public double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex) {
		double[] values = new double[0];
		ArrayList<Double> oldValues;
		if (chartData != null) {
			if (player.equals(PlayerEnum.PLAYER1)) {
				oldValues = chartData.getPl1Predicted();
			}
			else {
				oldValues = chartData.getPl2Predicted();
			}
			values = new double[oldValues.size() - startIndex];
			for (int i = startIndex; i < oldValues.size(); i++)
				values[i - startIndex] = oldValues.get(i);
		}
		return values;
	}

	@Override
	protected double[] computeValues(ArrayList<Double> oldValues, int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
