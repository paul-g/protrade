package org.ic.tennistrader.ui.chart;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.Range;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;

public class OverroundChart extends Chart implements UpdatableWidget{
	private ILineSeries backOverround;
	private ILineSeries layOverround;
	private IBarSeries pl1Volume;
	private IBarSeries pl2Volume;
	private boolean isBackOverround;
	private boolean isLayOverround;
	private static final String yOverroundTitle = "Overround";
	private static final String yVolumeTitle = "Volume";
	private ChartData chartData;
	private int sampleSize = 200;
	private Slider slider;
	
	public OverroundChart(Composite parent, int style, Match match, UpdatableChart syncWith, ChartData chartData, Slider slider) {
		super(parent, style);
		this.chartData = chartData;
		this.slider = slider;
		getTitle().setVisible(false);
		createSeries(match);
		//setBackOverround(true);
		getLegend().setPosition(SWT.BOTTOM);
		IAxis yAxis  = getAxisSet().getYAxis(0); 
		yAxis.getTitle().setText(yVolumeTitle);
		yAxis.getTitle().setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD));
		getAxisSet().getXAxis(0).getTitle().setVisible(false);
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		//this.getAxisSet().getXAxis(0).adjustRange();
		//this.getAxisSet().adjustRange();
		//this.getAxisSet().getYAxis(0).setRange(new Range(90,110));
		makeMenu(syncWith, parent);
	}

	private void createSeries(Match match) {
		ISeriesSet seriesSet = this.getSeriesSet();
		backOverround = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "back overround");
		Color color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		backOverround.setLineColor(color);
		layOverround = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "lay overround");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		layOverround.setLineColor(color);
		pl1Volume = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, match.getPlayerOne() +  " Volume");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		pl1Volume.setBarColor(color);
		pl1Volume.setBarPadding(90);
		pl2Volume = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, match.getPlayerTwo() + " Volume");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		pl2Volume.setBarColor(color);
		pl2Volume.setBarPadding(90);
		backOverround.setVisible(false);
		layOverround.setVisible(false);
		//pl1Volume.setVisible(false);
		pl2Volume.setVisible(false);
	}
	
	public void visibility(boolean pl1, boolean pl2){
		if (isBackOverround || isLayOverround){
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
	
	public void setPl1Volume(Date[] xData, double[] yData){
		setPl1VolumeX(xData);
		setPl1VolumeY(yData);
	}
	
	public void setPl1VolumeX(Date[] xData){
		pl1Volume.setXDateSeries(xData);
	}
	
	public void setPl1VolumeY(double[] yData){
		pl1Volume.setYSeries(yData);
	}
	
	public void setPl2Volume(Date[] xData, double[] yData){
		setPl2VolumeX(xData);
		setPl2VolumeY(yData);
	}
	
	public void setPl2VolumeX(Date[] xData){
		pl2Volume.setXDateSeries(xData);
	}
	
	public void setPl2VolumeY(double[] yData){
		pl2Volume.setYSeries(yData);
	}
	
	
	public void setLayOverround(Date[] xData, double[] yData){
		setLayOverroundX(xData);
		setLayOverroundY(yData);
	}
	
	public void setLayOverroundX(Date[] xData){
		layOverround.setXDateSeries(xData);
	}
	
	public void setLayOverroundY(double[] yData){
		layOverround.setYSeries(yData);
	}
	public void setBackOverround(Date[] xData, double[] yData){
		setBackOverroundX(xData);
		setBackOverroundY(yData);
	}
	
	public void setBackOverroundX(Date[] xData){
		backOverround.setXDateSeries(xData);
	}
	
	public void setBackOverroundY(double[] yData){
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
		//this.getAxisSet().getXAxis(0).adjustRange();
		//this.getAxisSet().getYAxis(0).setRange(new Range(0,150));
	}

	private void makeMenu(final UpdatableChart updatableChart, Composite parent) {
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
			public void handleEvent(Event arg0) {
				if (!overroundBack.getSelection()
						&& !overroundLay.getSelection()) {
					overroundBack.setSelection(true);
				} else {
					volume.setSelection(false);
				}
				setBackOverround(overroundBack.getSelection());
				visibility(updatableChart.pl1Selected, updatableChart.pl2Selected);
			}
		});
		
		overroundLay.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (!overroundLay.getSelection()
						&& !overroundBack.getSelection()) {
					overroundLay.setSelection(true);
				} else {
					volume.setSelection(false);
				}
				setLayOverround(overroundLay.getSelection());
				visibility(updatableChart.pl1Selected, updatableChart.pl2Selected);
			}
		});
		
		volume.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if (!volume.getSelection()) {
					volume.setSelection(true);
				} else {
					overroundBack.setSelection(false);
					overroundLay.setSelection(false);
					setBackOverround(false);
					setLayOverround(false);
					visibility(updatableChart.pl1Selected, updatableChart.pl2Selected);
					// allignment();
				}
			}
		});
	}
	
	private void showSeries(int i, boolean dragged) {
		int size = i + 1  < sampleSize ? i + 1 : sampleSize;
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
				 nr++; dataArray.get(nr)[a] =
				 chartData.getLayOverround().get(b); nr++;
				 dataArray.get(nr)[a] = chartData.getPl1Volume().get(b); nr++;
				 dataArray.get(nr)[a] = chartData.getPl2Volume().get(b); nr++;
				 
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
	
	private void updateDisplay() {
		final Composite parent = getParent();
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!isDisposed())
					redraw();
				 if (parent != null && !parent.isDisposed())
					parent.update();
			}
		});
	}
	
	private void updateData(MOddsMarketData data) {
		// add new market data to the data structures
		int dataSize = chartData.getDataSize() - 1;

		// set serieses values
		showSeries(dataSize, false);
		if (!isDisposed()) {
			getAxisSet().getXAxis(0).adjustRange();
			getAxisSet().getYAxis(0).adjustRange();
		}
	}

	@Override
	public void handleUpdate(final MOddsMarketData newData) {
		// TODO Auto-generated method stub
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateData(newData);
				if (!isDisposed()) {
					redraw();
					getParent().update();
				}
			}
		});
		
		
	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		addDisposeListener(listener);		
	}

}
