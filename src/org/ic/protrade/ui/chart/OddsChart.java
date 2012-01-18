package org.ic.protrade.ui.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Slider;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.data.match.PlayerEnum;
import org.ic.protrade.domain.ChartData;
import org.ic.protrade.model.chartcomputers.BackValuesComputer;
import org.ic.protrade.model.chartcomputers.MAComputer;
import org.ic.protrade.model.chartcomputers.PredictedComputer;
import org.ic.protrade.model.chartcomputers.SetEndComputer;
import org.swtchart.IAxis;
import org.swtchart.IAxis.Position;
import org.swtchart.IAxisSet;
import org.swtchart.IAxisTick;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ITitle;
import org.swtchart.LineStyle;
import org.swtchart.Range;

public class OddsChart extends UpdatableChart {
	private final String xAxisTitle = "Time";

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

		init(parent);
		makeMenu();
	}

	private void init(Composite parent) {
		setBackgroundMode(SWT.INHERIT_DEFAULT);

		yAxisTitle = "Decimal Odds";
		yAxisInvertedTitle = "Implied Odds (%)";

		createSeries();

		Color defaultAxisColor = Display.getCurrent().getSystemColor(
				SWT.COLOR_BLACK);

		IAxis xAxis = getAxisSet().getXAxis(0);
		configureAxis(xAxis, xAxisTitle, LineStyle.NONE, false,
				defaultAxisColor, null, null);

		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD);
		IAxis yAxis = getAxisSet().getYAxis(0);
		configureAxis(yAxis, yAxisTitle, LineStyle.NONE, false,
				defaultAxisColor, Position.Secondary, font);
		yAxis.getTitle().setVisible(true);

		getTitle().setVisible(false);


		getLegend().setPosition(SWT.TOP);

		yAxis.setRange(new Range(1, 3));

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
		
		initialSeries.add(player1Back);
		initialSeries.add(ma1);
	}

	private void configureAxis(IAxis axis, String title, LineStyle lineStyle,
			boolean visible, Color defaultAxisColor, Position pos, Font font) {
		IAxisTick tick = axis.getTick();
		if (pos != null)
			axis.setPosition(pos);
		if (defaultAxisColor != null)
			tick.setForeground(defaultAxisColor);
		axis.getGrid().setStyle(lineStyle);
		ITitle t = axis.getTitle();
		t.setText(title);
		t.setVisible(false);
		if (font != null)
			t.setFont(font);
		if (defaultAxisColor != null)
			t.setForeground(defaultAxisColor);
	}
	
	/**
	 * Populates the chart with the given market data
	 */
	@Override
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

	// switches between the two odds representations
	@Override
	public void invertAxis() {
		this.inverted = !this.inverted;
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
