package src.ui.updatable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import src.domain.MOddsMarketData;
import src.utils.Pair;

public class UpdatableChart extends Chart implements UpdatableWidget {
	private ILineSeries firstSeries;
	private ILineSeries secondSeries;
	private int sampleSize = 80;
	private boolean decimalOdds;
	private String xAxisTitle = "Time";
	private String yAxisDecimalTitle = "Decimal Odds";
	private String yAxisFractionalTitle = "Fractional Odds";
	private boolean pl2Selected;
	private boolean pl1Selected;
	private double[] pl1YSeries;
	private double[] pl2YSeries;

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
		setSeriesStyles();
		decimalOdds = true;
		this.getAxisSet().getXAxis(0).getTitle().setText(xAxisTitle);
		this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		pl1Selected = true;
	}

	private void setSeriesStyles() {
		ISeriesSet seriesSet = this.getSeriesSet();
		// build first series
		firstSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
				"back odds player 1");
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		firstSeries.setLineColor(color);
		firstSeries.setSymbolSize(4);
		firstSeries.setSymbolType(PlotSymbolType.CROSS);

		// /build second series
		secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
				"back odds player 2");
		Color colorSr2 = new Color(Display.getDefault(), 0, 255, 0);
		firstSeries.setLineColor(colorSr2);
		secondSeries.setSymbolSize(4);
		secondSeries.setSymbolType(PlotSymbolType.DIAMOND);
		secondSeries.setVisible(false);
	}

	/*
	 * Populates the chart with the given market data
	 */
	public void fillData(MOddsMarketData data) {
		Date[] newXSeries;
		int i = 0;
		// if graph already displaying values
		if (firstSeries.getYSeries() != null) {
			Date[] prevXSeries = firstSeries.getXDateSeries();
			double[] prevPl1YSeries = pl1YSeries;
			double[] prevPl2YSeries = pl2YSeries;
			// if not reached max sample size
			if (prevXSeries.length < sampleSize) {
				newXSeries = new Date[prevXSeries.length + 1];
				pl1YSeries = new double[prevXSeries.length + 1];
				pl2YSeries = new double[prevXSeries.length + 1];
				for (i = 0; i < prevXSeries.length; i++) {
					newXSeries[i] = prevXSeries[i];
					pl1YSeries[i] = prevPl1YSeries[i];
					pl2YSeries[i] = prevPl2YSeries[i];
				}
			} else { // discard least recent value
				newXSeries = new Date[sampleSize];
				pl1YSeries = new double[sampleSize];
				pl2YSeries = new double[sampleSize];
				for (i = 1; i < sampleSize; i++) {
					newXSeries[i - 1] = prevXSeries[i];
					pl1YSeries[i - 1] = prevPl1YSeries[i];
					pl2YSeries[i - 1] = prevPl2YSeries[i];
				}
				i--;
			}
		} else {
			newXSeries = new Date[1];
			pl1YSeries = new double[1];
			pl2YSeries = new double[1];
		}

		newXSeries[i] = Calendar.getInstance().getTime();
		// if data has been read from Betfair
		pl1YSeries = addValue(pl1YSeries, data, i, true);
		pl2YSeries = addValue(pl2YSeries, data, i, false);

		// set first series values

		firstSeries.setVisible(pl1Selected);
		secondSeries.setVisible(pl2Selected);
		firstSeries.setXDateSeries(newXSeries);
		firstSeries.setYSeries(pl1YSeries);
		secondSeries.setXDateSeries(newXSeries);
		secondSeries.setYSeries(pl2YSeries);
		if (!this.isDisposed())
			this.getAxisSet().adjustRange();
	}

	private double[] addValue(double[] series, MOddsMarketData data, int i,
			boolean pl1) {
		ArrayList<Pair<Double, Double>> oddData;
		oddData = pl1 ? data.getPl1Back() : data.getPl2Back();
		if (oddData != null && oddData.size() > 0) {
			series[i] = oddData.get(0).getI();

		} else {
			if (i > 0) // keep previous value if it exists
				series[i] = series[i - 1];
			else
				// put zero if no previous value
				series[i] = 0;
		}
		return series;
	}

	public void invertAxis() {
		int changeValue = decimalOdds ? (-1) : 1;
		decimalOdds = !decimalOdds;
		if (decimalOdds)
			this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		else
			this.getAxisSet().getYAxis(0).getTitle().setText(
					yAxisFractionalTitle);

		resetOddValues(changeValue);
		updateDisplay();
	}

	private void resetOddValues(int changeValue) {
		if (firstSeries.getYSeries() != null) {
			double[] ySeries = firstSeries.getYSeries();
			for (int i = 0; i < ySeries.length; i++)
				ySeries[i] += changeValue;
			firstSeries.setYSeries(ySeries);
		}
	}

	/*
	 * updates the chart with the new given market data
	 */
	public void handleUpdate(final MOddsMarketData newData) {
		fillData(newData);
		updateDisplay();
	}

	private void updateDisplay() {
		final Composite comp = this.getParent();
		final UpdatableChart chart = this;
		if (comp != null) {
			comp.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!chart.isDisposed())
						chart.redraw();
					if (!comp.isDisposed())
						comp.update();
				}
			});
		}
	}

	public void changeSelected(int i) {
		if (i == 1)	pl1Selected = !pl1Selected;
		else if (i==2) pl2Selected = !pl2Selected;
		updateDisplay();
	}

}
