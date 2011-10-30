package src.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.Range;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class UpdatableChart extends Chart implements UpdatableWidget {
	private ILineSeries firstSeries;
	private ILineSeries secondSeries;

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
		setSeriesStyles();
	}
	
	private void setSeriesStyles() {
		ISeriesSet seriesSet = this.getSeriesSet();		
		// build first series
		firstSeries = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "back odds");		
		// set colours for first series
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		firstSeries.setLineColor(color);
		firstSeries.setSymbolSize(4);		
		// build second series
		secondSeries = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "MA-Fast");		
		// set series' symbol types
		firstSeries.setSymbolType(PlotSymbolType.CROSS);
		secondSeries.setSymbolType(PlotSymbolType.DIAMOND);		
	}

	/*
	 * Populates the chart with the given market data
	 */
	public void fillData(MOddsMarketData data) {
		// temporarily for filling charts with random data
		Date[] xSeries = new Date[60];
		double[] ySeries = new double[60];
		Date[] xSeries2 = new Date[60];
		double[] ySeries2 = new double[60];
		Random randomGenerator = new Random();
		Calendar calendar = Calendar.getInstance();
		// set random values on the graphs, one every 2 seconds
		for (int i = 0; i < 60; i++) {
			calendar.add(Calendar.MILLISECOND, 2000); 
			xSeries[i] = xSeries2[i] = calendar.getTime();
			ySeries[i] = randomGenerator.nextDouble() + 1;
			ySeries2[i] = (Math.cos(i) + 2) / 2;
		}				
		// set first series values		
		firstSeries.setXDateSeries(xSeries);
		firstSeries.setYSeries(ySeries);
		// set second series values
		secondSeries.setXDateSeries(xSeries2);
		secondSeries.setYSeries(ySeries2);		
		this.getAxisSet().adjustRange();
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
