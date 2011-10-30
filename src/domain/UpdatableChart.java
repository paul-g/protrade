package src.domain;

import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class UpdatableChart extends Chart implements UpdatableWidget {

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * Populates the chart with the given market data
	 */
	public void fillData(MOddsMarketData data) {
		// temporarily for filling charts with random data
		double[] xSeries = new double[60];
		double[] ySeries = new double[60];
		double[] xSeries2 = new double[60];
		double[] ySeries2 = new double[60];
		Random randomGenerator = new Random();
		for (int i = 0; i < 60; i++) {
			xSeries[i] = i;
			xSeries2[i] = i;
			ySeries[i] = randomGenerator.nextDouble() + 1;
			ySeries2[i] = (Math.cos(i) + 2) / 2;
		}
		ISeriesSet seriesSet = this.getSeriesSet();
		ILineSeries series = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "back odds");
		series.setXSeries(xSeries);
		series.setYSeries(ySeries);
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		series.setLineColor(color);
		series.setSymbolSize(4);
		ILineSeries series2 = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "MA-Fast");
		series2.setXSeries(xSeries2);
		series2.setYSeries(ySeries2);
		series.setSymbolType(PlotSymbolType.CROSS);
		series2.setSymbolType(PlotSymbolType.DIAMOND);
		final IAxisSet axisSet = this.getAxisSet();
		axisSet.adjustRange();
	}

	/*
	 * updates the chart with the new given market data
	 */
	public void handleUpdate(final MOddsMarketData newData) {
		// fillData(newData);
		final Composite comp = this.getParent();
		final UpdatableChart chart = this;
		if (comp != null) {
			comp.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					fillData(newData);
					if (!chart.isDisposed())
						chart.redraw();
					if (!comp.isDisposed())
						comp.update();
					//comp.getDisplay().timerExec(1000, this);
				}
			});
		}
	}

}
