package org.ic.protrade.model.chartcomputers;

import java.util.ArrayList;

import org.ic.protrade.domain.ChartData;
import org.ic.protrade.domain.match.PlayerEnum;

public class SetEndComputer extends SeriesComputer {

	@Override
	public double[] computeValues(PlayerEnum player, ChartData chartData,
			int startIndex, boolean inverted) {
		int size = chartData.getEndOfSets().size() - startIndex;
		double[] values = new double[size];
		for (int i = 0; i < size; i++)
			values[i] = chartData.getEndOfSets().get(i + startIndex);
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
