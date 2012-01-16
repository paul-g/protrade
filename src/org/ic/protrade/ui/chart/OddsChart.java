package org.ic.protrade.ui.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.ic.protrade.domain.ChartData;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.chartcomputers.BackValuesComputer;
import org.ic.protrade.model.chartcomputers.MAComputer;
import org.ic.protrade.model.chartcomputers.PredictedComputer;
import org.ic.protrade.model.chartcomputers.SetEndComputer;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IAxis.Position;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.IAxisTick;
import org.swtchart.ITitle;
import org.swtchart.LineStyle;
import org.swtchart.Range;

public class OddsChart extends UpdatableChart {
	/*
	 * private IBarSeries endOfSets; /home/corina/workspace private ILineSeries
	 * firstSeries, secondSeries, maPl1Series, maPl2Series, pl1Predicted,
	 * pl2Predicted;
	 * 
	 * private IErrorBar pl1Spread, pl2Spread;
	 * 
	 * private final int seriesNr = 11;
	 */

	private final String xAxisTitle = "Time";
	private final String yAxisDecimalTitle = "Decimal Odds";
	private final String yAxisFractionalTitle = "Implied Odds (%)";

	/*
	 * boolean pl1Selected; boolean pl2Selected; /* private boolean
	 * maPl1Selected, maPl2Selected, spPl1Selected, spPl2Selected,
	 * predPl1Selected, predPl2Selected;
	 */

	public OddsChart(Composite parent, int style, Slider slider) {
		super(parent, style);
		this.slider = slider;
		this.player1Name = "Player 1";
		this.player2Name = "Player 2";
		init(parent);
	}

	public OddsChart(Composite parent, int style, Match match, Slider slider,
			ChartData chartData) {
		super(parent, style);
		this.slider = slider;
		this.chartData = chartData;
		this.player1Name = match.getPlayerOne().toString();
		this.player2Name = match.getPlayerTwo().toString();

		// initSlider();
		init(parent);
		makeMenu();

	}

	private void init(Composite parent) {
		setBackgroundMode(SWT.INHERIT_DEFAULT);

		yAxisTitle = "Decimal Odds";
		yAxisInvertedTitle = "Implied Odds (%)";
		
		// setSeriesStyles(pl1Name, pl2Name);
		createSeries();

		// pl1Selected = true;

		Color defaultAxisColor = Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK);

		IAxis xAxis = getAxisSet().getXAxis(0);
		configureAxis(xAxis, xAxisTitle, LineStyle.NONE, false,
				defaultAxisColor,null, null);

		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD);
		IAxis yAxis = getAxisSet().getYAxis(0);
		configureAxis(yAxis, yAxisDecimalTitle, LineStyle.NONE, false,
				defaultAxisColor,Position.Secondary, font);
		yAxis.getTitle().setVisible(true);

		getTitle().setVisible(false);

		// makeMenus(parent, pl1Name, pl2Name);

		getLegend().setPosition(SWT.TOP);
		// addListeners();

		yAxis.setRange(new Range(1, 3));
		// this.getAxisSet().adjustRange();

		xAxis.getTick().setVisible(false);
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		xAxis.getTick().setFormat(format);

		IAxis yAxis2 = getAxisSet().getYAxis(1);
		configureAxis(yAxis2, null, LineStyle.NONE, false, null, null, null);
		yAxis2.getTick().setVisible(false);

	}

	private void createSeries() {
		IAxisSet axisSet = this.getAxisSet();
		axisSet.createYAxis();
		axisSet.getYAxis(1).setRange(new Range(0, 1));

		chartMenu = new ChartMenu();

		LineProp prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		prop.setStep(false);
		prop.setArea(true);
		SeriesProperties player1Back = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.BACK_ODDS, PlayerEnum.PLAYER1, "BACK ODDS",
				this.player1Name, new BackValuesComputer(), prop);
		chartMenu.addSeriesItem(player1Back);

		prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		prop.setStep(false);
		prop.setArea(true);
		SeriesProperties player2Back = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.BACK_ODDS, PlayerEnum.PLAYER2, "BACK ODDS",
				this.player2Name, new BackValuesComputer(), prop);
		chartMenu.addSeriesItem(player2Back);

		prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		prop.setStep(false);
		SeriesProperties ma1 = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.MOVING_AVERAGE, PlayerEnum.PLAYER1, "MA",
				this.player1Name, new MAComputer(), prop);
		chartMenu.addSeriesItem(ma1);

		prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
		prop.setStep(false);
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.MOVING_AVERAGE, PlayerEnum.PLAYER2, "MA",
				this.player2Name, new MAComputer(), prop));

		prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.PREDICTED, PlayerEnum.PLAYER1, "Predicted",
				this.player1Name, new PredictedComputer(), prop));

		prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.PREDICTED, PlayerEnum.PLAYER2, "Predicted",
				this.player2Name, new PredictedComputer(), prop));

		LineProp endOfSetsProp = new LineProp();
		endOfSetsProp.setyAxisId(1);
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.BAR,
				MarketSeriesType.PREDICTED, PlayerEnum.PLAYER1, "End of sets",
				"", new SetEndComputer(), endOfSetsProp));
		/*
		 * endOfSets.setYAxisId(1);
		 */

		// addSeries(new BackValuesComputer(), player1Back);
		initialSeries.add(player1Back);
		initialSeries.add(ma1);
		// addSeries(new BackValuesComputer(), player2Back);
	}

	private void configureAxis(IAxis axis, String title, LineStyle lineStyle,
			boolean visible, Color defaultAxisColor, Position pos, Font font) {
		IAxisTick tick = axis.getTick();
		if (pos != null) axis.setPosition(pos);
		if (defaultAxisColor != null) tick.setForeground(defaultAxisColor);
		axis.getGrid().setStyle(lineStyle);
		ITitle t = axis.getTitle();
		t.setText(title);		
		t.setVisible(false);
		if (font != null) t.setFont(font);
		if (defaultAxisColor != null) t.setForeground(defaultAxisColor);
	}

	private void initSlider() {
		slider.setMaximum(1);
		slider.setSelection(0);
		slider.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Slider slider = (Slider) event.widget;
				if (slider.getMaximum() > sampleSize)
					showSeries(slider.getSelection(), true);
			}
		});
	}

	/*
	 * private void setSeriesStyles(String pl1Name, String pl2Name) {
	 * 
	 * ISeriesSet seriesSet = this.getSeriesSet(); IAxisSet axisSet =
	 * this.getAxisSet(); axisSet.createYAxis();
	 * axisSet.getYAxis(1).setRange(new Range(0, 1));
	 * 
	 * endOfSets = createBarSeries(seriesSet, "end of Sets", 100, false);
	 * endOfSets.setYAxisId(1); // build first series Display display =
	 * Display.getCurrent(); Color color =
	 * display.getSystemColor(SWT.COLOR_BLUE);
	 * 
	 * firstSeries = createLineSeries(seriesSet, "back odds " + pl1Name, color,
	 * LineStyle.SOLID, PlotSymbolType.NONE, 0, false, SWT.ON, true, true);
	 * pl1Spread = createSpread(firstSeries, ErrorBarType.BOTH, false); // build
	 * second series Color color2 = display.getSystemColor(SWT.COLOR_RED);
	 * 
	 * secondSeries = createLineSeries(seriesSet, "back odds " + pl2Name,
	 * color2, LineStyle.SOLID, PlotSymbolType.NONE, 0, false, SWT.ON, false,
	 * false); pl2Spread = createSpread(secondSeries, ErrorBarType.BOTH, false);
	 * // building moving averages player 1 Color color3 =
	 * display.getSystemColor(SWT.COLOR_DARK_MAGENTA); maPl1Series =
	 * createLineSeries(seriesSet, "MA " + pl1Name, color3, LineStyle.SOLID,
	 * PlotSymbolType.SQUARE, 4, false, SWT.ON, false, false); // building
	 * moving averages player 2 Color color4 =
	 * display.getSystemColor(SWT.COLOR_GREEN); maPl2Series =
	 * createLineSeries(seriesSet, "MA " + pl2Name, color4, LineStyle.SOLID,
	 * PlotSymbolType.TRIANGLE, 4, false, SWT.ON, false, false); pl1Predicted =
	 * createLineSeries(seriesSet, "Predicted odds " + pl1Name, color,
	 * LineStyle.DOT, PlotSymbolType.NONE, 0, false, SWT.ON, false, false);
	 * pl2Predicted = createLineSeries(seriesSet, "Predicted odds " + pl2Name,
	 * color2, LineStyle.DOT, PlotSymbolType.NONE, 0, false, SWT.ON, false,
	 * false); }
	 * 
	 * 
	 * private IBarSeries createBarSeries(ISeriesSet seriesSet, String title,
	 * int i, boolean b) { IBarSeries bar = (IBarSeries)
	 * seriesSet.createSeries(SeriesType.BAR, title); bar.setBarPadding(100);
	 * return bar; }
	 * 
	 * private IErrorBar createSpread(ILineSeries series, ErrorBarType type,
	 * boolean visible) { IErrorBar spread = series.getYErrorBar();
	 * spread.setType(type); spread.setVisible(visible); return spread; }
	 * 
	 * private ILineSeries createLineSeries(ISeriesSet seriesSet, String text,
	 * Color color, LineStyle lineStyle, PlotSymbolType type, int symbSize,
	 * boolean step, int antialias, boolean area, boolean visible) { ILineSeries
	 * line = (ILineSeries) seriesSet.createSeries( SeriesType.LINE, text);
	 * line.setLineColor(color); line.setLineStyle(lineStyle);
	 * line.setSymbolType(PlotSymbolType.NONE); line.setSymbolSize(symbSize);
	 * line.enableStep(step); line.setAntialias(antialias);
	 * line.enableArea(area); line.setVisible(visible); return line; }
	 */

	/**
	 * Populates the chart with the given market data
	 */
	protected void updateData(MOddsMarketData data) {
		// add new market data to the data structures
		int dataSize = chartData.getDataSize() - 1;
		// update size of the slider and selection based on what the user was
		// previously viewing
		updateSlider(dataSize);

		// set serieses values
		showSeries(dataSize, false);
		if (!this.isDisposed()) {
			this.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						OddsChart.this.getAxisSet().getXAxis(0).adjustRange();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			// this.getAxisSet().getYAxis(0).adjustRange();
		}
	}

	private void updateSlider(final int i) {
		this.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (i >= sampleSize) {
					if (slider.getSelection() == slider.getMaximum() - 1) {
						slider.setMaximum(i + 1);
						slider.setSelection(i);
					}
					slider.setMaximum(i + 1);
					if (i == sampleSize)
						slider.setMinimum(sampleSize - 1);
					updateSlide();
				} else {
					slider.setMaximum(i + 1);
					slider.setMinimum(i);
					slider.setSelection(i);
				}
			}
		});
	}

	/*
	 public void showSeries(int i, boolean dragged) { 
		int seriesNr = 8; 
		 int size = (i) <
	 sampleSize ? (i) : sampleSize; Date showXSeries[] = new Date[size];
	 ArrayList<double[]> dataArray = new ArrayList<double[]>(); for (int k =
	 0; k < seriesNr; k++) { dataArray.add(k, new double[size]); } int z = i <
	 sampleSize ? 0 : 1; if (slider.getMaximum() == slider.getSelection() + 1
	 || dragged) { // variables for updating series according decimal/implied setting
	  int pow = 1; int k = 1; if (!decimalOdds) { pow = -1; k = 100; }
	 
			for (int a = 0; a < size; a++) {
				int nr = 0;
				int b = (i - sampleSize + 1) * z + a;
				showXSeries[a] = chartData.getxSeries().get(b);
				dataArray.get(nr)[a] = Math.pow(chartData.getPl1YSeries()
						.get(b), pow)
						* k;
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getPl2YSeries()
						.get(b), pow)
						* k;
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getMaPl1().get(b),
						pow)
						* k;
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getMaPl2().get(b),
						pow)
						* k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(chartData.getPl1Lay().get(b)
						.first(), pow) - Math.pow(chartData.getPl1YSeries()
						.get(b), pow))
						* k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(chartData.getPl1YSeries().get(
						b), pow) - Math.pow(chartData.getPl1Lay().get(b)
						.second(), pow))
						* k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(chartData.getPl2Lay().get(b)
						.first(), pow) - Math.pow(chartData.getPl2YSeries()
						.get(b), pow))
						* k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(chartData.getPl2YSeries().get(
						b), pow) - Math.pow(chartData.getPl2Lay().get(b)
						.second(), pow))
						* k;
				nr++;
				dataArray.get(nr)[a] = 0; // chartData.endOfSets.get(b);
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getPl1Predicted()
						.get(b), pow)
						* k;
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getPl2Predicted()
						.get(b), pow)
						* k;

			}
	 
	 /*
	 firstSeries.setXDateSeries(showXSeries);
	 firstSeries.setYSeries(dataArray.get(0));
	 secondSeries.setXDateSeries(showXSeries);
	 secondSeries.setYSeries(dataArray.get(1));
	 maPl1Series.setXDateSeries(showXSeries);
	 maPl1Series.setYSeries(dataArray.get(2));
	 maPl2Series.setXDateSeries(showXSeries);
	 Pl2Series.setYSeries(dataArray.get(3));
	 pl1Spread.setPlusErrors(dataArray.get(4));
	 pl1Spread.setMinusErrors(dataArray.get(5));
	 pl2Spread.setPlusErrors(dataArray.get(6));
	 pl2Spread.setMinusErrors(dataArray.get(7));
	 endOfSets.setXDateSeries(showXSeries);
	 endOfSets.setYSeries(dataArray.get(8));
	 pl1Predicted.setXDateSeries(showXSeries);
	 pl1Predicted.setYSeries(dataArray.get(9));
	 pl2Predicted.setXDateSeries(showXSeries);
	 pl2Predicted.setYSeries(dataArray.get(10)); 
	 
	 updateDisplay(); } }
	 */

	// switches between the two odds representations
	public void invertAxis() {
		this.inverted = !this.inverted;
		/*
		decimalOdds = !decimalOdds;
		if (decimalOdds)
			this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		else
			this.getAxisSet().getYAxis(0).getTitle().setText(
					yAxisFractionalTitle);
		*/
		// TODO commented because of failing test
		// showSeries(slider.getSelection(), true);
		updateDisplay();
	}

	private void updateSlide() {
		final Composite comp = slider.getParent();
		if (comp != null) {
			comp.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!slider.isDisposed())
						slider.redraw();
					if (!comp.isDisposed())
						comp.update();
				}
			});
		}
	}

	@Override
	public void handleBettingMarketEndOFSet() {
		chartData.addMarketEndOfSet();
	}
}
