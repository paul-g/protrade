package src.service;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class GraphUpdater implements Runnable {
	private Chart chart;
	private Composite comp;

	public GraphUpdater(String chartTitle, Composite comp) {
		chart = new Chart(comp, SWT.NONE);
		chart.getTitle().setText(chartTitle);
		GridData chartData = new GridData();
		chartData.horizontalSpan = 2;
		// chartData.horizontalAlignment = SWT.FILL;
		// chart.setLayoutData(chartData);
		//charts.add(chart);
		//this.chart = chart;
		this.comp = comp;		
	}
	
	@Override
	public void run() {
		comp.getDisplay().timerExec(0, new Runnable() {
			@Override
			public void run() {
				fillChartData(chart);
				chart.redraw();
				if (!comp.isDisposed()) comp.update();
				comp.getDisplay().timerExec(1000, this);
			}
		});
	}
	
	private void fillChartData(Chart chart) {
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
		ISeriesSet seriesSet = chart.getSeriesSet();
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
		final IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();
		// end filling charts
	}

}
