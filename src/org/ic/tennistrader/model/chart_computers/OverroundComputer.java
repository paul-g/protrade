package org.ic.tennistrader.model.chart_computers;

import java.util.ArrayList;

import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.PlayerEnum;

public class OverroundComputer extends SeriesComputer{
	@Override
	public double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex, boolean inverted){
		double[] values = new double[0];
		ArrayList<Double> oldValues;
		if (chartData != null) {
			if (player.equals(PlayerEnum.PLAYER1)) {
				oldValues = chartData.getBackOverround();
			}
			else {
				oldValues = chartData.getLayOverround();
			}
			int size = oldValues.size() - startIndex;
			values = new double[size];
			for (int i = 0; i < size; i++) {
				values[i] = oldValues.get(i + startIndex);
			}
		}
		return values;
	}
	//@Override
	public double[] addValue(PlayerEnum player, ChartData chartData,
			double player1newValue, double player2newValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	protected double[] computeValues(ArrayList<Double> oldValues, int startIndex) {
		int size = oldValues.size();
		return new double[1];
	}
	*/

}
