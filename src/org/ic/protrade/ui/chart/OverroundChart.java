package org.ic.protrade.ui.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Slider;
import org.ic.protrade.domain.ChartData;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.chartcomputers.OverroundComputer;
import org.ic.protrade.model.chartcomputers.VolumeComputer;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.IAxis.Position;
import org.swtchart.ISeries.SeriesType;

public class OverroundChart extends UpdatableChart {
	private ILineSeries backOverround;
	private ILineSeries layOverround;
	
	private IBarSeries pl1Volume;
	private IBarSeries pl2Volume;
	
	private boolean isBackOverround;
	private boolean isLayOverround;
	
	private static final String yOverroundTitle = "Overround";
	private static final String yVolumeTitle = "Volume";
	
	public OverroundChart(Composite parent, int style, OddsChart syncWith,
			Slider slider) {
		super(parent, style);
		this.slider = slider;
		this.player1Name = "Player 1";
		this.player2Name = "Player 2";
		init(parent, syncWith, slider);
	}

	public OverroundChart(Composite parent, int style, Match match,
			OddsChart syncWith, ChartData chartData, Slider slider) {
		super(parent, style);
		this.chartData = chartData;
		this.slider = slider;
		this.player1Name = match.getPlayerOne().toString();
		this.player2Name = match.getPlayerTwo().toString();

		init(parent, syncWith, slider);
		
		makeMenu();
		
	}

	private void init(Composite parent, OddsChart syncWith,
			Slider slider) {

		getTitle().setVisible(false);

		yAxisTitle = yAxisInvertedTitle = yVolumeTitle;
		
		//createSeries(pl1Name, pl2Name);
		
		chartMenu = new ChartMenu();
		IAxisSet axisSet = this.getAxisSet();
		axisSet.createYAxis();
		IAxis yAxis2 = axisSet.getYAxis(1);
		
		Color defaultAxisColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD);
		
		getLegend().setPosition(SWT.BOTTOM);
		IAxis yAxis = getAxisSet().getYAxis(0);
		configureAxis(yAxis, yVolumeTitle, font, defaultAxisColor,null);
		configureAxis(yAxis2, yOverroundTitle, font, defaultAxisColor, Position.Secondary);
		
		IAxis xAxis = getAxisSet().getXAxis(0);
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		xAxis.getTick().setFormat(format);
		xAxis.getTitle().setForeground(defaultAxisColor);
		xAxis.getTick().setForeground(defaultAxisColor);
		xAxis.getTitle().setVisible(false);
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		
		
		
		LineProp oround = new LineProp();
		oround.setyAxisId(1);
		oround.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		SeriesProperties overround = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.OVERROUND, PlayerEnum.PLAYER1,
				"Overround Back", "", new OverroundComputer(), oround);
		chartMenu.addSeriesItem(overround);
		
		oround = new LineProp();
		oround.setyAxisId(1);
		oround.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA));
		SeriesProperties overroundl = new SeriesProperties(SeriesType.LINE,
				MarketSeriesType.OVERROUND, PlayerEnum.PLAYER2,
				"Overround Lay", "", new OverroundComputer(), oround);
		chartMenu.addSeriesItem(overroundl);
		
		LineProp prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		SeriesProperties volume1 = new SeriesProperties(SeriesType.BAR,
				MarketSeriesType.VOLUME, PlayerEnum.PLAYER1,
				"Volume", this.player1Name, new VolumeComputer(), prop);
		chartMenu.addSeriesItem(volume1);
		
		prop = new LineProp();
		prop.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		SeriesProperties volume2 = new SeriesProperties(SeriesType.BAR,
				MarketSeriesType.VOLUME, PlayerEnum.PLAYER2,
				"Volume", this.player2Name, new VolumeComputer(), prop);
		chartMenu.addSeriesItem(volume2);
		
		//addSeries(new BackValuesComputer(), overround);
		initialSeries.add(overround);
		
		
		// setBackOverround(true);
		
		// this.getAxisSet().getXAxis(0).adjustRange();
		// this.getAxisSet().adjustRange();
		// this.getAxisSet().getYAxis(0).setRange(new Range(90,110));
		
		
		//makeMenu(syncWith, parent);
		
	}

	/* Old ones
	private void createSeries(String pl1Name, String pl2Name) {
		ISeriesSet seriesSet = this.getSeriesSet();
		backOverround = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
				"back overround");
		Color color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		backOverround.setLineColor(color);
		layOverround = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
				"lay overround");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		layOverround.setLineColor(color);

		pl1Volume = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, pl1Name
				+ " Volume");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		pl1Volume.setBarColor(color);
		pl1Volume.setBarPadding(90);

		pl2Volume = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, pl2Name
				+ " Volume");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		pl2Volume.setBarColor(color);
		pl2Volume.setBarPadding(90);
		backOverround.setVisible(false);
		layOverround.setVisible(false);
		// pl1Volume.setVisible(false);
		pl2Volume.setVisible(false);
	}
	*/

	private void configureAxis(IAxis axis, String title, Font font,
			Color defaultAxisColor, Position pos) {
		if (pos != null) axis.setPosition(pos);
		axis.getTitle().setText(title);
		axis.getTitle().setFont(font);
		axis.getTitle().setForeground(defaultAxisColor);
		axis.getTick().setForeground(defaultAxisColor);
	}

	public void visibility(boolean pl1, boolean pl2) {
		if (isBackOverround || isLayOverround) {
			this.getAxisSet().getYAxis(0).getTitle().setText(yOverroundTitle);
			pl1Volume.setVisible(false);
			pl2Volume.setVisible(false);
			backOverround.setVisible(isBackOverround());
			layOverround.setVisible(isLayOverround());
		} else {
			this.getAxisSet().getYAxis(0).getTitle().setText(yVolumeTitle);
			backOverround.setVisible(false);
			layOverround.setVisible(false);
			pl1Volume.setVisible(pl1);
			pl2Volume.setVisible(pl2);
		}
	}

	public void setPl1Volume(Date[] xData, double[] yData) {
		setPl1VolumeX(xData);
		setPl1VolumeY(yData);
	}

	public void setPl1VolumeX(Date[] xData) {
		pl1Volume.setXDateSeries(xData);
	}

	public void setPl1VolumeY(double[] yData) {
		pl1Volume.setYSeries(yData);
	}

	public void setPl2Volume(Date[] xData, double[] yData) {
		setPl2VolumeX(xData);
		setPl2VolumeY(yData);
	}

	public void setPl2VolumeX(Date[] xData) {
		pl2Volume.setXDateSeries(xData);
	}

	public void setPl2VolumeY(double[] yData) {
		pl2Volume.setYSeries(yData);
	}

	public void setLayOverround(Date[] xData, double[] yData) {
		setLayOverroundX(xData);
		setLayOverroundY(yData);
	}

	public void setLayOverroundX(Date[] xData) {
		layOverround.setXDateSeries(xData);
	}

	public void setLayOverroundY(double[] yData) {
		layOverround.setYSeries(yData);
	}

	public void setBackOverround(Date[] xData, double[] yData) {
		setBackOverroundX(xData);
		setBackOverroundY(yData);
	}

	public void setBackOverroundX(Date[] xData) {
		backOverround.setXDateSeries(xData);
	}

	public void setBackOverroundY(double[] yData) {
		backOverround.setYSeries(yData);
	}

	public boolean isBackOverround() {
		return isBackOverround;
	}

	public void setBackOverround(boolean isBackOverround) {
		this.isBackOverround = isBackOverround;
	}

	public boolean isLayOverround() {
		return isLayOverround;
	}

	public void setLayOverround(boolean isLayOverround) {
		this.isLayOverround = isLayOverround;
	}

	public void adjust() {
		this.getAxisSet().adjustRange();
		// this.getAxisSet().getXAxis(0).adjustRange();
		// this.getAxisSet().getYAxis(0).setRange(new Range(0,150));
	}
	
	protected void updateData(MOddsMarketData data) {
		// add new market data to the data structures
		//int dataSize = chartData.getDataSize() - 1;
		// set serieses values
		showSeries(chartData.getDataSize() - 1, false);
		if (!isDisposed()) {
			this.getDisplay().asyncExec(new Runnable(){
				@Override
				public void run() {
					getAxisSet().getXAxis(0).adjustRange();
					getAxisSet().getYAxis(0).adjustRange();
					getAxisSet().getYAxis(1).adjustRange();
				} 				
			});			
		}
	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void invertAxis() {
		// TODO Auto-generated method stub
		
	}
		

	/*
	private void makeMenu(final OddsChart oddsChart, Composite parent) {
		Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		setMenu(menu);
		getPlotArea().setMenu(menu);
		final MenuItem overroundBack = new MenuItem(menu, SWT.CHECK);
		overroundBack.setText("Overround Back");
		overroundBack.setSelection(true);
		final MenuItem overroundLay = new MenuItem(menu, SWT.CHECK);
		overroundLay.setText("Overround Lay");
		overroundLay.setSelection(false);
		final MenuItem volume = new MenuItem(menu, SWT.CHECK);
		volume.setText("Volume");

		overroundBack.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				if (!overroundBack.getSelection()
						&& !overroundLay.getSelection()) {
					overroundBack.setSelection(true);
				} else {
					volume.setSelection(false);
				}
				setBackOverround(overroundBack.getSelection());
				visibility(oddsChart.pl1Selected,
						oddsChart.pl2Selected);
			}
		});

		overroundLay.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				if (!overroundLay.getSelection()
						&& !overroundBack.getSelection()) {
					overroundLay.setSelection(true);
				} else {
					volume.setSelection(false);
				}
				setLayOverround(overroundLay.getSelection());
				visibility(oddsChart.pl1Selected,
						oddsChart.pl2Selected);
			}
		});

		volume.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				if (!volume.getSelection()) {
					volume.setSelection(true);
				} else {
					overroundBack.setSelection(false);
					overroundLay.setSelection(false);
					setBackOverround(false);
					setLayOverround(false);
					visibility(oddsChart.pl1Selected,
							oddsChart.pl2Selected);
					// allignment();
				}
			}
		});
	}
	*/
	
	/*
	public void showSeries(int i, boolean dragged) {
		int size = i + 1 < sampleSize ? i + 1 : sampleSize;
		int seriesNr = 4;
		Date showXSeries[] = new Date[size];
		ArrayList<double[]> dataArray = new ArrayList<double[]>();
		for (int k = 0; k < seriesNr; k++) {
			dataArray.add(k, new double[size]);
		}

		int z = i < sampleSize ? 0 : 1;

		if (slider.getMaximum() == slider.getSelection() + 1 || dragged) {
			// variables for updating series according decimal/implied setting

			for (int a = 0; a < size; a++) {
				int nr = 0;
				int b = (i - sampleSize + 1) * z + a;
				showXSeries[a] = chartData.getxSeries().get(b);

				dataArray.get(nr)[a] = chartData.getBackOverround().get(b);
				nr++;
				dataArray.get(nr)[a] = chartData.getLayOverround().get(b);
				nr++;
				dataArray.get(nr)[a] = chartData.getPl1Volume().get(b);
				nr++;
				dataArray.get(nr)[a] = chartData.getPl2Volume().get(b);
				nr++;

				// dataArray.get(nr)[a] = chartData.endOfSets.get(b);

			}

			setBackOverround(showXSeries, dataArray.get(0));
			setLayOverround(showXSeries, dataArray.get(1));
			setPl1Volume(showXSeries, dataArray.get(2));
			setPl2Volume(showXSeries, dataArray.get(3));
			adjust();
			updateDisplay();
		}
	}
	*/
}
