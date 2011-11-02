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

		// build second series
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
		if (firstSeries.getYSeries() != null && secondSeries.getYSeries() != null) {
			Date[] prevXSeries = firstSeries.getXDateSeries();
			double[] prevPl1YSeries = firstSeries.getYSeries();//pl1YSeries;
			double[] prevPl2YSeries = secondSeries.getYSeries();//pl2YSeries;
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
		
		pl1YSeries = addValue(pl1YSeries, i, data.getPl1Back());
		pl2YSeries = addValue(pl2YSeries, i, data.getPl2Back());

		// set serieses values
		firstSeries.setXDateSeries(newXSeries);
		firstSeries.setYSeries(pl1YSeries);
		secondSeries.setXDateSeries(newXSeries);
		secondSeries.setYSeries(pl2YSeries);
		if (!this.isDisposed())
			this.getAxisSet().adjustRange();
	}

	private double[] addValue(double[] series, int i, ArrayList<Pair<Double, Double>> oddData) {
		// if data has been read from Betfair
		if (oddData != null && oddData.size() > 0) {
			series[i] = oddData.get(0).getI();			
			if (!decimalOdds) // convert to fractional odds if necessary
				series[i]--;
		} else {
			if (i > 0) // keep previous value if it exists
				series[i] = series[i - 1];
			else // put zero if no previous value				
				series[i] = 0;
		}			
		return series;
	}

	// switches between the two odds representations
	public void invertAxis() {
		int changeValue = decimalOdds ? (-1) : 1;
		decimalOdds = !decimalOdds;
		if (decimalOdds)
			this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		else
			this.getAxisSet().getYAxis(0).getTitle().setText(
					yAxisFractionalTitle);

		firstSeries.setYSeries(adjustSeriesValues(changeValue, firstSeries.getYSeries()));
		secondSeries.setYSeries(adjustSeriesValues(changeValue, secondSeries.getYSeries()));
		
		updateDisplay();
	}

	private double[] adjustSeriesValues(int changeValue, double[] series) {
		if (series != null) {
			for (int i = 0; i < series.length; i++)
				series[i] += changeValue;
		}
		return series;
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
		firstSeries.setVisible(pl1Selected);
		secondSeries.setVisible(pl2Selected);
		updateDisplay();
	}

}
