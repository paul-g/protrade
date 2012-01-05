package org.ic.tennistrader.ui.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.model.BackValuesComputer;
import org.ic.tennistrader.model.MAComputer;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.IErrorBar;
import org.swtchart.IErrorBar.ErrorBarType;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.LineStyle;
import org.swtchart.Range;

public class OddsChart extends UpdatableChart {
	private IBarSeries endOfSets;
	
	private ILineSeries firstSeries, secondSeries, maPl1Series, maPl2Series,
			pl1Predicted, pl2Predicted;
	
	private IErrorBar pl1Spread, pl2Spread;
	
	private final int seriesNr = 11;
	
	private boolean decimalOdds;
	
	private final String xAxisTitle = "Time";
	private final String yAxisDecimalTitle = "Decimal Odds";
	private final String yAxisFractionalTitle = "Implied Odds (%)";
	
	boolean pl1Selected;
	boolean pl2Selected;
	/*
	private boolean maPl1Selected, maPl2Selected, spPl1Selected, spPl2Selected,
			predPl1Selected, predPl2Selected;
	*/
	
	public OddsChart(Composite parent, int style, Slider slider) {
		super(parent, style);
		this.slider = slider;
		init(parent, "Player 1", "Player 2");
	}

	public OddsChart(Composite parent, int style, Match match,
			Slider slider, ChartData chartData) {
		super(parent, style);
		this.slider = slider;
		this.chartData = chartData;
		String pl1Name = match.getPlayerOne().toString();
		String pl2Name = match.getPlayerTwo().toString();

		//initSlider();
		init(parent, pl1Name, pl2Name);
	}

	private void init(Composite parent, String pl1Name, String pl2Name) {
		setBackgroundMode(SWT.INHERIT_DEFAULT);

		//setSeriesStyles(pl1Name, pl2Name);
		createSeries();
		
		
		decimalOdds = true;
		pl1Selected = true;

		IAxis xAxis = getAxisSet().getXAxis(0);
		configureAxis(xAxis, xAxisTitle, LineStyle.NONE, false);

		IAxis yAxis = getAxisSet().getYAxis(0);
		configureAxis(yAxis, yAxisDecimalTitle, LineStyle.NONE, false);

		getTitle().setVisible(false);
		
		
		
		//makeMenus(parent, pl1Name, pl2Name);
		makeMenu();
		
		
		
		

		getLegend().setPosition(SWT.TOP);
		// addListeners();

		yAxis.setRange(new Range(1, 3));
		// this.getAxisSet().adjustRange();

		xAxis.getTick().setVisible(false);

		IAxis yAxis2 = getAxisSet().getYAxis(1);
		configureAxis(yAxis2, null, LineStyle.NONE, false);
		yAxis2.getTick().setVisible(false);
		
	}

	private void createSeries() {
		IAxisSet axisSet = this.getAxisSet();
		axisSet.createYAxis();
		axisSet.getYAxis(1).setRange(new Range(0, 1));
		
		chartMenu = new ChartMenu();
		SeriesProperties player1Back = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.BACK_ODDS, PlayerEnum.PLAYER1,
				"BACK ODDS pl 1", new BackValuesComputer(), new LineProp());
		chartMenu.addSeriesItem(player1Back);
		SeriesProperties player2Back = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.BACK_ODDS, PlayerEnum.PLAYER2,
				"BACK ODDS pl 2", new BackValuesComputer(), new LineProp());
		chartMenu.addSeriesItem(player2Back);
		
		
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.MOVING_AVERAGE, PlayerEnum.PLAYER1,
				"MA pl 1", new MAComputer(), new LineProp()));
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.MOVING_AVERAGE, PlayerEnum.PLAYER2,
				"MA pl 2", new MAComputer(), new LineProp()));
		
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.BACK_ODDS, PlayerEnum.PLAYER1,
				"Predicted pl 1", new BackValuesComputer(), new LineProp()));
		chartMenu.addSeriesItem(new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.BACK_ODDS, PlayerEnum.PLAYER2,
				"Predicted pl 2", new BackValuesComputer(), new LineProp()));
		
		addSeries(new BackValuesComputer(), player1Back);
		//addSeries(new BackValuesComputer(), player2Back);
	}
	
	private void configureAxis(IAxis axis, String title, LineStyle lineStyle,
			boolean visible) {
		axis.getGrid().setStyle(lineStyle);
		ITitle t = axis.getTitle();
		t.setText(title);
		t.setVisible(false);
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
	private void setSeriesStyles(String pl1Name, String pl2Name) {

		ISeriesSet seriesSet = this.getSeriesSet();
		IAxisSet axisSet = this.getAxisSet();
		axisSet.createYAxis();
		axisSet.getYAxis(1).setRange(new Range(0, 1));
		
		endOfSets = createBarSeries(seriesSet, "end of Sets", 100, false);
		endOfSets.setYAxisId(1);
		// build first series
		Display display = Display.getCurrent();
		Color color = display.getSystemColor(SWT.COLOR_BLUE);

		firstSeries = createLineSeries(seriesSet, "back odds " + pl1Name,
				color, LineStyle.SOLID, PlotSymbolType.NONE, 0, false, SWT.ON,
				true, true);
		pl1Spread = createSpread(firstSeries, ErrorBarType.BOTH, false);
		// build second series
		Color color2 = display.getSystemColor(SWT.COLOR_RED);

		secondSeries = createLineSeries(seriesSet, "back odds " + pl2Name,
				color2, LineStyle.SOLID, PlotSymbolType.NONE, 0, false, SWT.ON,
				false, false);
		pl2Spread = createSpread(secondSeries, ErrorBarType.BOTH, false);
		// building moving averages player 1
		Color color3 = display.getSystemColor(SWT.COLOR_DARK_MAGENTA);
		maPl1Series = createLineSeries(seriesSet, "MA " + pl1Name, color3,
				LineStyle.SOLID, PlotSymbolType.SQUARE, 4, false, SWT.ON,
				false, false);
		// building moving averages player 2
		Color color4 = display.getSystemColor(SWT.COLOR_GREEN);
		maPl2Series = createLineSeries(seriesSet, "MA " + pl2Name, color4,
				LineStyle.SOLID, PlotSymbolType.TRIANGLE, 4, false, SWT.ON,
				false, false);
		pl1Predicted = createLineSeries(seriesSet, "Predicted odds " + pl1Name,
				color, LineStyle.DOT, PlotSymbolType.NONE, 0, false, SWT.ON,
				false, false);
		pl2Predicted = createLineSeries(seriesSet, "Predicted odds " + pl2Name,
				color2, LineStyle.DOT, PlotSymbolType.NONE, 0, false, SWT.ON,
				false, false);
	}
	*/

	private IBarSeries createBarSeries(ISeriesSet seriesSet, String title,
			int i, boolean b) {
		IBarSeries bar = (IBarSeries) seriesSet.createSeries(SeriesType.BAR,
				title);
		bar.setBarPadding(100);
		return bar;
	}

	private IErrorBar createSpread(ILineSeries series, ErrorBarType type,
			boolean visible) {
		IErrorBar spread = series.getYErrorBar();
		spread.setType(type);
		spread.setVisible(visible);
		return spread;
	}

	private ILineSeries createLineSeries(ISeriesSet seriesSet, String text,
			Color color, LineStyle lineStyle, PlotSymbolType type,
			int symbSize, boolean step, int antialias, boolean area,
			boolean visible) {
		ILineSeries line = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, text);
		line.setLineColor(color);
		line.setLineStyle(lineStyle);
		line.setSymbolType(PlotSymbolType.NONE);
		line.setSymbolSize(symbSize);
		line.enableStep(step);
		line.setAntialias(antialias);
		line.enableArea(area);
		line.setVisible(visible);
		return line;
	}

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
			try {
				this.getAxisSet().getXAxis(0).adjustRange();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// this.getAxisSet().getYAxis(0).adjustRange();
		}
	}

	private void updateSlider(int i) {
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

	/*
	public void showSeries(int i, boolean dragged) {
		int size = (i) < sampleSize ? (i) : sampleSize;
		Date showXSeries[] = new Date[size];
		ArrayList<double[]> dataArray = new ArrayList<double[]>();
		for (int k = 0; k < seriesNr; k++) {
			dataArray.add(k, new double[size]);
		}
		int z = i < sampleSize ? 0 : 1;
		if (slider.getMaximum() == slider.getSelection() + 1 || dragged) {
			// variables for updating series according decimal/implied setting
			int pow = 1;
			int k = 1;
			if (!decimalOdds) {
				pow = -1;
				k = 100;
			}

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
						pow) * k;
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getMaPl2().get(b),
						pow) * k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(chartData.getPl1Lay().get(b)
						.first(), pow) - Math.pow(chartData.getPl1YSeries()
						.get(b), pow))
						* k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(
						chartData.getPl1YSeries().get(b), pow) - Math.pow(
						chartData.getPl1Lay().get(b).second(), pow)) * k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(chartData.getPl2Lay().get(b)
						.first(), pow) - Math.pow(chartData.getPl2YSeries()
						.get(b), pow))
						* k;
				nr++;
				dataArray.get(nr)[a] = (Math.pow(
						chartData.getPl2YSeries().get(b), pow) - Math.pow(
						chartData.getPl2Lay().get(b).second(), pow)) * k;
				nr++;
				dataArray.get(nr)[a] = chartData.endOfSets.get(b);
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getPl1Predicted()
						.get(b), pow)
						* k;
				nr++;
				dataArray.get(nr)[a] = Math.pow(chartData.getPl2Predicted()
						.get(b), pow)
						* k;

			}

			firstSeries.setXDateSeries(showXSeries);
			firstSeries.setYSeries(dataArray.get(0));
			secondSeries.setXDateSeries(showXSeries);
			secondSeries.setYSeries(dataArray.get(1));
			maPl1Series.setXDateSeries(showXSeries);
			maPl1Series.setYSeries(dataArray.get(2));
			maPl2Series.setXDateSeries(showXSeries);
			maPl2Series.setYSeries(dataArray.get(3));
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
			updateDisplay();
		}
	}
	*/

	// switches between the two odds representations
	public void invertAxis() {
		decimalOdds = !decimalOdds;
		if (decimalOdds)
			this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		else
			this.getAxisSet().getYAxis(0).getTitle()
					.setText(yAxisFractionalTitle);
		
		//TODO commented because of failing test
		//showSeries(slider.getSelection(), true);
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
