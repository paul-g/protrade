package org.ic.protrade.model.chartcomputers;

import java.util.ArrayList;

import org.ic.protrade.data.match.PlayerEnum;
import org.ic.protrade.domain.ChartData;

public class PredictedComputer extends SeriesComputer {

	@Override
	public double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex, boolean inverted) {
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
				if (inverted)
					values[i - startIndex] = Math.pow(oldValues.get(i), -1) * 100;
				else
					values[i - startIndex] = oldValues.get(i);
		}
		return values;
	}

	/*
	@Override
	protected double[] computeValues(ArrayList<Double> oldValues, int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

}
