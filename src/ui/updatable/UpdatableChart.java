package src.ui.updatable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import src.domain.MOddsMarketData;
import src.domain.match.Match;
import src.utils.Pair;

public class UpdatableChart extends Chart implements UpdatableWidget {
	private ILineSeries firstSeries;
	private ILineSeries secondSeries;
	private ILineSeries maPl1Series;
	private ILineSeries maPl2Series;
	private int sampleSize = 80;
	private boolean decimalOdds;
	private String xAxisTitle = "Time";
	private String yAxisDecimalTitle = "Decimal Odds";
	private String yAxisFractionalTitle = "Fractional Odds";
	private boolean pl2Selected;
	private boolean pl1Selected;
	private boolean maPl2Selected;
	private boolean maPl1Selected;
	private double[] pl1YSeries;
	private double[] pl2YSeries;
	private double[] maPl1;
	private double[] maPl2;
	private Match match;

	public UpdatableChart(Composite parent, int style, Match match) {
		super(parent, style);
		setSeriesStyles();
		this.match = match;
		decimalOdds = true;
		this.getAxisSet().getXAxis(0).getTitle().setText(xAxisTitle);
		this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		pl1Selected = true;
		this.getTitle().setText(match.getName());
		makeMenus(parent);
	}

	private void setSeriesStyles() {
		ISeriesSet seriesSet = this.getSeriesSet();
		// build first series
		firstSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
		"back odds player 1");
		Color color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		firstSeries.setLineColor(color);
		firstSeries.setSymbolSize(4);
		firstSeries.setSymbolType(PlotSymbolType.CROSS);

		// build second series
		secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
		"back odds player 2");
		Color colorSr2 = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		secondSeries.setLineColor(colorSr2);
		secondSeries.setSymbolSize(4);
		secondSeries.setSymbolType(PlotSymbolType.DIAMOND);
		secondSeries.setVisible(false);

		// building moving averages player 1
		maPl1Series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
		"MA player 1");
		Color color3 = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA);
		maPl1Series.setLineColor(color3);
		maPl1Series.setSymbolSize(4);
		maPl1Series.setSymbolType(PlotSymbolType.SQUARE);
		maPl1Series.setVisible(false);

		// building moving averages player 2
		maPl2Series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
		"MA player 2");
		Color color4 = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		maPl2Series.setLineColor(color4);
		maPl2Series.setSymbolSize(4);
		maPl2Series.setSymbolType(PlotSymbolType.TRIANGLE);
		maPl2Series.setVisible(false);

	}

	/**
	 * Populates the chart with the given market data
	 */
	public void fillData(MOddsMarketData data) {
		Date[] newXSeries;
		int i = 0;
		// if graph already displaying values
		if (firstSeries.getYSeries() != null
				&& secondSeries.getYSeries() != null
				&& maPl1Series.getYSeries() != null
				&& maPl2Series.getYSeries() != null) {
			Date[] prevXSeries = firstSeries.getXDateSeries();
			double[] prevPl1YSeries = firstSeries.getYSeries();// pl1YSeries;
			double[] prevPl2YSeries = secondSeries.getYSeries();// pl2YSeries;
			double[] prevMaPl1Series = maPl1Series.getYSeries();// pl1YSeries;
			double[] prevMaPl2Series = maPl2Series.getYSeries();// pl2YSeries;
			// if not reached max sample size
			if (prevXSeries.length < sampleSize) {
				newXSeries = new Date[prevXSeries.length + 1];
				pl1YSeries = new double[prevXSeries.length + 1];
				pl2YSeries = new double[prevXSeries.length + 1];
				maPl1 = new double[prevXSeries.length + 1];
				maPl2 = new double[prevXSeries.length + 1];

				for (i = 0; i < prevXSeries.length; i++) {
					newXSeries[i] = prevXSeries[i];
					pl1YSeries[i] = prevPl1YSeries[i];
					pl2YSeries[i] = prevPl2YSeries[i];
					maPl1[i] = prevMaPl1Series[i];
					maPl2[i] = prevMaPl2Series[i];
				}
			} else { // discard least recent value
				newXSeries = new Date[sampleSize];
				pl1YSeries = new double[sampleSize];
				pl2YSeries = new double[sampleSize];
				maPl1 = new double[sampleSize];
				maPl2 = new double[sampleSize];
				for (i = 1; i < sampleSize; i++) {
					newXSeries[i - 1] = prevXSeries[i];
					pl1YSeries[i - 1] = prevPl1YSeries[i];
					pl2YSeries[i - 1] = prevPl2YSeries[i];
					maPl1[i - 1] = prevMaPl1Series[i];
					maPl2[i - 1] = prevMaPl2Series[i];
				}
				i--;
			}
		} else {
			newXSeries = new Date[1];
			pl1YSeries = new double[1];
			pl2YSeries = new double[1];
			maPl1 = new double[1];
			maPl2 = new double[1];
		}

		newXSeries[i] = Calendar.getInstance().getTime();

		pl1YSeries = addValue(pl1YSeries, i, data.getPl1Back());
		pl2YSeries = addValue(pl2YSeries, i, data.getPl2Back());
		maPl1 = addMaValue(maPl1, i, pl1YSeries);
		maPl2 = addMaValue(maPl2, i, pl2YSeries);

		// set serieses values
		firstSeries.setXDateSeries(newXSeries);
		firstSeries.setYSeries(pl1YSeries);
		secondSeries.setXDateSeries(newXSeries);
		secondSeries.setYSeries(pl2YSeries);
		maPl1Series.setXDateSeries(newXSeries);
		maPl1Series.setYSeries(maPl1);
		maPl2Series.setXDateSeries(newXSeries);
		maPl2Series.setYSeries(maPl2);
		if (!this.isDisposed())
			this.getAxisSet().adjustRange();
	}

	private double[] addMaValue(double[] array, int i,
			double[] backData) {
		if (backData != null) {
			double sum = 0;
			if (i < 10) {
				for (int a = i; a >= 0; a--) sum+=backData[a];
				array[i] = sum/(i+1);
			} else {
				sum = array[i-1]*10 - backData[i-10] + backData[i];
				array[i] = sum/10;
			}
		}
		return array;
	}

	private double[] addValue(double[] series, int i,
			ArrayList<Pair<Double, Double>> oddData) {
		// if data has been read from Betfair
		if (oddData != null && oddData.size() > 0) {
			series[i] = oddData.get(0).getI();
			if (!decimalOdds) // convert to fractional odds if necessary
				series[i]--;
		} else {
			if (i > 0) // keep previous value if it exists
				series[i] = series[i - 1];
			else
				// put zero if no previous value
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
			this.getAxisSet().getYAxis(0).getTitle()
			.setText(yAxisFractionalTitle);

		firstSeries.setYSeries(adjustSeriesValues(changeValue,
				firstSeries.getYSeries()));
		secondSeries.setYSeries(adjustSeriesValues(changeValue,
				secondSeries.getYSeries()));

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
		if (i == 1)
			pl1Selected = !pl1Selected;
		else if (i == 2)
			pl2Selected = !pl2Selected;
		else if (i == 3)
			maPl1Selected = !maPl1Selected;
		else if (i == 4)
			maPl2Selected = !maPl2Selected;
		firstSeries.setVisible(pl1Selected);
		secondSeries.setVisible(pl2Selected);
		maPl1Series.setVisible(maPl1Selected);
		maPl2Series.setVisible(maPl2Selected);
		updateDisplay();

	}

	private void makeMenus(Composite parent) {
		Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		final MenuItem toggle = new MenuItem(menu, SWT.PUSH);
		toggle.setText("Toggle");
		this.setMenu(menu);
		this.getPlotArea().setMenu(menu);

		toggle.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				UpdatableChart.this.invertAxis();
			}
		});

		final MenuItem player1 = new MenuItem(menu, SWT.CHECK);
		player1.setText(match.getPlayer1());
		this.setMenu(menu);
		this.getPlotArea().setMenu(menu);

		final MenuItem player2 = new MenuItem(menu, SWT.CHECK);
		player2.setText(match.getPlayer2());
		
		final MenuItem maPlayer1 = new MenuItem(menu, SWT.CHECK);
		maPlayer1.setText("MA " + match.getPlayer1());
		
		final MenuItem maPlayer2 = new MenuItem(menu, SWT.CHECK);
		maPlayer2.setText("MA " + match.getPlayer2());

		player1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				// cannot deselect both
				if (!player1.getSelection() && !player2.getSelection()) {
					player1.setSelection(true);
					return;
				}
				UpdatableChart.this.changeSelected(1);
			}

		});

		player2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				// cannot deselect both
				if (!player2.getSelection() && !player1.getSelection()) {
					player2.setSelection(true);
					return;
				}
				UpdatableChart.this.changeSelected(2);
			}
		});
		
		maPlayer1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				UpdatableChart.this.changeSelected(3);
			}
		});
		
		maPlayer2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				UpdatableChart.this.changeSelected(4);
			}
		});
		
		

		// first player selected by default
		player1.setSelection(true);

		// set the menu
		this.setMenu(menu);
		this.getPlotArea().setMenu(menu);
	}

}
