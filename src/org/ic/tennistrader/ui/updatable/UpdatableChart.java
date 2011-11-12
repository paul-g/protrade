package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Slider;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.utils.Pair;

public class UpdatableChart extends Chart implements UpdatableWidget {
	private ILineSeries firstSeries;
	private ILineSeries secondSeries;
	private ILineSeries maPl1Series;
	private ILineSeries maPl2Series;
	private int sampleSize = 200; 
	private boolean decimalOdds;
	private String xAxisTitle = "Time";
	private String yAxisDecimalTitle = "Decimal Odds";
	private String yAxisFractionalTitle = "Fractional Odds";
	private boolean pl2Selected;
	private boolean pl1Selected;
	private boolean maPl2Selected;
	private boolean maPl1Selected;
	private ArrayList<Double> pl1YSeries;
	private ArrayList<Double> pl2YSeries;
	private ArrayList<Double> maPl1;
	private ArrayList<Double> maPl2;
	private ArrayList<Date> xSeries;
	private Match match;
	private Slider slider;

	public UpdatableChart(Composite parent, int style, Match match, Slider slider) {
		super(parent, style);
		createSlider(slider);
		setSeriesStyles();
		this.match = match;
		decimalOdds = true;
		this.getAxisSet().getXAxis(0).getTitle().setText(xAxisTitle);
		this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		pl1Selected = true;
		this.getTitle().setText(match.getName());
		makeMenus(parent);
		
		IAxisSet axisSet = this.getAxisSet();
		IAxis yAxis = axisSet.getYAxis(0);
		
	}

	private void createSlider(Slider slider) {
		this.slider=slider;
		slider.setMaximum(1);
		slider.setSelection(0);
		
        slider.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	Slider slider = (Slider)event.widget;
            	if ( slider.getMaximum() > sampleSize) showSeries(slider.getSelection(), true);
            }
          });
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
		int i = 0;
		// if graph already displaying values
		if (firstSeries.getYSeries() != null
				&& secondSeries.getYSeries() != null
				&& maPl1Series.getYSeries() != null
				&& maPl2Series.getYSeries() != null) {
			i=pl1YSeries.size();
			// if not reached max sample size
			//if (prevXSeries.length < sampleSize) {
//				xSeries = new Date[prevXSeries.length + 1];
//				pl1YSeries = new double[prevXSeries.length + 1];
//				pl2YSeries = new double[prevXSeries.length + 1];
//				maPl1 = new double[prevXSeries.length + 1];
//				maPl2 = new double[prevXSeries.length + 1];

//				for (i = 0; i < prevXSeries.length; i++) {
//					xSeries[i] = prevXSeries[i];
//					pl1YSeries[i] = prevPl1YSeries[i];
//					pl2YSeries[i] = prevPl2YSeries[i];
//					maPl1[i] = prevMaPl1Series[i];
//					maPl2[i] = prevMaPl2Series[i];
//				}
		} else {
			xSeries = new ArrayList<Date>();
			pl1YSeries = new ArrayList<Double>();
			pl2YSeries = new ArrayList<Double>();
			maPl1 = new ArrayList<Double>();
			maPl2 = new ArrayList<Double>();
		}

		xSeries.add(i, Calendar.getInstance().getTime());

		pl1YSeries = addValue(pl1YSeries, i, data.getPl1Back());
		pl2YSeries = addValue(pl2YSeries, i, data.getPl2Back());
		maPl1 = addMaValue(maPl1, i, pl1YSeries);
		maPl2 = addMaValue(maPl2, i, pl2YSeries);

		if (i>=sampleSize) {
			if (slider.getSelection() == slider.getMaximum()-1){
				slider.setMaximum(i+1);
				slider.setSelection(i);
			}
			slider.setMaximum(i+1);
			if (i==sampleSize) slider.setMinimum(sampleSize-1);
			updateSlide();
		}
		// set serieses values
		showSeries(i,false);
		if (!this.isDisposed())
			this.getAxisSet().adjustRange();
	}

	public void showSeries(int i, boolean dragged) {
		int size = (i+1)<sampleSize ? (i+1) : sampleSize;
		Date showXSeries[] = new Date[size];
		double[] showPl1YSeries = new double[size];
		double[] showPl2YSeries = new double[size];
		double[] showMaPl1 = new double[size];
		double[] showMaPl2 = new double[size];
		int z = i<sampleSize ? 0 : 1;
		if (slider.getMaximum() == slider.getSelection()+1 || dragged){
			for (int a = 0; a<size; a++){
				int b = (i-sampleSize+1)*z+a;				
				showXSeries[a] = xSeries.get(b);
				showPl1YSeries[a] = pl1YSeries.get(b);
				showPl2YSeries[a] =	pl2YSeries.get(b);
				showMaPl1[a] = maPl1.get(b);
				showMaPl2[a] = maPl2.get(b);
			}
			firstSeries.setXDateSeries(showXSeries);
			firstSeries.setYSeries(showPl1YSeries);
			secondSeries.setXDateSeries(showXSeries);
			secondSeries.setYSeries(showPl2YSeries);
			maPl1Series.setXDateSeries(showXSeries);
			maPl1Series.setYSeries(showMaPl1);
			maPl2Series.setXDateSeries(showXSeries);
			maPl2Series.setYSeries(showMaPl2);
		}
		
		
		
	}

	private ArrayList<Double> addMaValue(ArrayList<Double> maPl12, int i,
			ArrayList<Double> pl1ySeries2) {
		if (pl1ySeries2 != null) {
			double sum = 0;
			if (i < 10) {
				for (int a = i; a >= 0; a--) sum+=pl1ySeries2.get(a);
				maPl12.add(i,sum/(i+1));
			} else {
				sum = maPl12.get(i-1)*10 - pl1ySeries2.get(i-10) + pl1ySeries2.get(i);
				maPl12.add(i,sum/10);
			}
		}
		return maPl12;
	}

	private ArrayList<Double> addValue(ArrayList<Double> pl1ySeries2, int i,
			ArrayList<Pair<Double, Double>> oddData) {
		// if data has been read from Betfair
		if (oddData != null && oddData.size() > 0) {
			pl1ySeries2.add(i,oddData.get(0).getI());
			if (!decimalOdds) // convert to fractional odds if necessary
				pl1ySeries2.add(i,pl1ySeries2.get(i)-1);
		} else {
			if (i > 0) // keep previous value if it exists
				pl1ySeries2.add(i,pl1ySeries2.get(i-1));
			else
				// put zero if no previous value
				pl1ySeries2.add(i,(double)0);
		}
		return pl1ySeries2;
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
				}
			});
		}
		//fillData(newData);
		//updateDisplay();
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
