package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
import org.swtchart.IAxis.Position;
import org.swtchart.IErrorBar.ErrorBarType;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.IErrorBar;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.Range;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.ui.MainWindow;
import org.ic.tennistrader.utils.Pair;

public class UpdatableChart extends Chart implements UpdatableWidget {
	private ILineSeries firstSeries;
	private ILineSeries secondSeries;
	private ILineSeries maPl1Series;
	private ILineSeries maPl2Series;
	private IErrorBar pl1Spread;
	private IErrorBar pl2Spread;
	private int sampleSize = 200;
	private boolean decimalOdds;
	private String xAxisTitle = "Time";
	private String yAxisDecimalTitle = "Decimal Odds";
	private String yAxisFractionalTitle = "Implied Odds";
	private boolean pl1Selected;
	private boolean pl2Selected;
	private boolean maPl2Selected;
	private boolean maPl1Selected;
	private boolean spPl1Selected;
	private boolean spPl2Selected;
	private ArrayList<Double> pl1YSeries;
	private ArrayList<Double> pl2YSeries;
	private ArrayList<Pair<Double,Double>> pl1Lay;
	private ArrayList<Pair<Double,Double>> pl2Lay;
	private ArrayList<Double> maPl1;
	private ArrayList<Double> maPl2;
	private ArrayList<Date> xSeries;
	private Match match;
	private Slider slider;

	public UpdatableChart(Composite parent, int style, Match match,
			Slider slider) {
		super(parent, style);
		createSlider(slider);
		this.match = match;
		setSeriesStyles();
		decimalOdds = true;
		this.getAxisSet().getXAxis(0).getTitle().setText(xAxisTitle);
		this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		pl1Selected = true;
		this.getTitle().setText(match.getName());
		makeMenus(parent);
		this.getLegend().setPosition(SWT.TOP);
	}
	
	private void createSlider(Slider slider) {
		this.slider = slider;
		slider.setMaximum(1);
		slider.setSelection(0);

		slider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Slider slider = (Slider) event.widget;
				if (slider.getMaximum() > sampleSize)
					showSeries(slider.getSelection(), true);
			}
		});
	}

	private void setSeriesStyles() {
		
		ISeriesSet seriesSet = this.getSeriesSet();
		// build first series
		String str1 = "back odds " + match.getPlayer1(); 
		firstSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, str1);
		Color color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		firstSeries.setLineColor(color);
		firstSeries.setSymbolSize(4);
		firstSeries.setSymbolType(PlotSymbolType.CROSS);
		pl1Spread = firstSeries.getYErrorBar();	
		pl1Spread.setType(ErrorBarType.BOTH);
		pl1Spread.setVisible(false);
		// build second series
		String str2 = "back odds " + match.getPlayer2();
		secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
				"back odds " + match.getPlayer2());
		Color colorSr2 = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		secondSeries.setLineColor(colorSr2);
		secondSeries.setSymbolSize(4);
		secondSeries.setSymbolType(PlotSymbolType.DIAMOND);
		secondSeries.setVisible(false);
		pl2Spread = secondSeries.getYErrorBar();
		pl2Spread.setType(ErrorBarType.BOTH);
		pl2Spread.setVisible(false);
		// building moving averages player 1
		String str3 = "MA " + match.getPlayer1(); 
		maPl1Series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, str3);
		Color color3 = Display.getCurrent().getSystemColor(
				SWT.COLOR_DARK_MAGENTA);
		maPl1Series.setLineColor(color3);
		maPl1Series.setSymbolSize(4);
		maPl1Series.setSymbolType(PlotSymbolType.SQUARE);
		maPl1Series.setVisible(false);

		// building moving averages player 2
		String str4 = "MA " + match.getPlayer2(); 
		maPl2Series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, str4);
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
			i = pl1YSeries.size();
		} else {
			xSeries = new ArrayList<Date>();
			pl1YSeries = new ArrayList<Double>();
			pl2YSeries = new ArrayList<Double>();
			maPl1 = new ArrayList<Double>();
			maPl2 = new ArrayList<Double>();
			pl1Lay = new ArrayList<Pair<Double,Double>>();
			pl2Lay = new ArrayList<Pair<Double,Double>>();
		}

		xSeries.add(i, Calendar.getInstance().getTime());

		pl1YSeries = addValue(pl1YSeries, i, data.getPl1Back());
		pl2YSeries = addValue(pl2YSeries, i, data.getPl2Back());
		pl1Lay = addLay(pl1Lay, i, data.getPl1Back(),data.getPl1Lay());
		pl2Lay = addLay(pl2Lay, i, data.getPl2Back(),data.getPl2Lay());
		maPl1 = addMaValue(maPl1, i, pl1YSeries);
		maPl2 = addMaValue(maPl2, i, pl2YSeries);

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

		// set serieses values
		showSeries(i, false);
		if (!this.isDisposed())
			this.getAxisSet().adjustRange();
	}

	private ArrayList<Pair<Double, Double>> addLay(ArrayList<Pair<Double, Double>> array, int i,
			ArrayList<Pair<Double, Double>> back, ArrayList<Pair<Double, Double>> lay) {
		double plus;
		double minus;
		// if data has been read from Betfair
		if (back != null || lay!= null) {
			minus = back.get(back.size()-1).getI();
			plus = lay.get(lay.size()-1).getI();
			array.add(new Pair<Double, Double>(plus,minus));
		} else {
			if (i > 0) // keep previous value if it exists
				array.add(i,array.get(i-1));
			else
				// put zero if no previous value
				array.add(i, new Pair<Double, Double>(0.0,0.0));
		}
		return array;
	}

	public void showSeries(int i, boolean dragged) {
		int size = (i + 1) < sampleSize ? (i + 1) : sampleSize;
		Date showXSeries[] = new Date[size];
		double[] showPl1YSeries = new double[size];
		double[] showPl2YSeries = new double[size];
		double[] showMaPl1 = new double[size];
		double[] showMaPl2 = new double[size];
		double[] showPl1Plus = new double[size];
		double[] showPl1Minus = new double[size];
		double[] showPl2Plus = new double[size];
		double[] showPl2Minus = new double[size];
		int z = i < sampleSize ? 0 : 1;
		if (slider.getMaximum() == slider.getSelection() + 1 || dragged) {
			int pow = 1;
			int k = 1;
			if (!decimalOdds) {
				pow = -1;
				k=100;
			}
				
			for (int a = 0; a < size; a++) {
				int b = (i - sampleSize + 1) * z + a;
				showXSeries[a] = xSeries.get(b);
				showPl1YSeries[a] = Math.pow(pl1YSeries.get(b), pow)*k;
				showPl2YSeries[a] = Math.pow(pl2YSeries.get(b), pow)*k;
				showMaPl1[a] = Math.pow(maPl1.get(b), pow)*k;
				showMaPl2[a] = Math.pow(maPl2.get(b), pow)*k;
				showPl1Plus[a] = (Math.pow(pl1Lay.get(b).getI(),pow)  - Math.pow(pl1YSeries.get(b),pow)) * k;
				showPl1Minus[a] = (Math.pow(pl1YSeries.get(b),pow) - Math.pow(pl1Lay.get(b).getJ(),pow)) * k;
				showPl2Plus[a] = (Math.pow(pl2Lay.get(b).getI(),pow) - Math.pow(pl2YSeries.get(b),pow)) * k;
				showPl2Minus[a] = (Math.pow(pl2YSeries.get(b),pow) - Math.pow(pl2Lay.get(b).getJ(),pow)) * k;
			}
			firstSeries.setXDateSeries(showXSeries);
			firstSeries.setYSeries(showPl1YSeries);
			secondSeries.setXDateSeries(showXSeries);
			secondSeries.setYSeries(showPl2YSeries);
			maPl1Series.setXDateSeries(showXSeries);
			maPl1Series.setYSeries(showMaPl1);
			maPl2Series.setXDateSeries(showXSeries);
			maPl2Series.setYSeries(showMaPl2);
			pl1Spread.setPlusErrors(showPl1Plus);
			pl1Spread.setMinusErrors(showPl1Minus);
			pl2Spread.setPlusErrors(showPl2Plus);
			pl2Spread.setMinusErrors(showPl2Minus);
			updateDisplay();
		}
	}

	private ArrayList<Double> addMaValue(ArrayList<Double> maPl12, int i,
			ArrayList<Double> pl1ySeries2) {
		if (pl1ySeries2 != null) {
			double sum = 0;
			if (i < 10) {
				for (int a = i; a >= 0; a--)
					sum += pl1ySeries2.get(a);
				maPl12.add(i, sum / (i + 1));
			} else {
				sum = maPl12.get(i - 1) * 10 - pl1ySeries2.get(i - 10)
						+ pl1ySeries2.get(i);
				maPl12.add(i, sum / 10);
			}
		}
		return maPl12;
	}

	private ArrayList<Double> addValue(ArrayList<Double> pl1ySeries2, int i,
			ArrayList<Pair<Double, Double>> oddData) {
		// if data has been read from Betfair
		if (oddData != null && oddData.size() > 0) {
			pl1ySeries2.add(i, oddData.get(0).getI());
		} else {
			if (i > 0) // keep previous value if it exists
				pl1ySeries2.add(i, pl1ySeries2.get(i - 1));
			else
				// put zero if no previous value
				pl1ySeries2.add(i, (double) 0);
		}
		return pl1ySeries2;
	}

	// switches between the two odds representations
	public void invertAxis() {
		decimalOdds = !decimalOdds;
		if (decimalOdds)
			this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
		else
			this.getAxisSet().getYAxis(0).getTitle()
					.setText(yAxisFractionalTitle);

		showSeries(slider.getSelection(), true);
		// updateDisplay();
	}

	/**
	 * updates the chart with the new given market data
	 */
	public void handleUpdate(final MOddsMarketData newData) {
		// final Composite comp = this.getParent();
		final UpdatableChart chart = this;
		if (chart != null) {
			chart.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					fillData(newData);
					if (!chart.isDisposed()) {
						chart.redraw();
						chart.getParent().update();
					}
					// if (!comp.isDisposed())
					// comp.update();
				}
			});
		}
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
		else if (i == 5) 
			spPl1Selected = !spPl1Selected;
		else if (i == 6) 
			spPl2Selected = !spPl2Selected;
		

		firstSeries.setVisible(pl1Selected);
		secondSeries.setVisible(pl2Selected);
		maPl1Series.setVisible(maPl1Selected);
		maPl2Series.setVisible(maPl2Selected);
		pl1Spread.setVisible(spPl1Selected);
		pl2Spread.setVisible(spPl2Selected);
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

		final MenuItem spPlayer1 = new MenuItem(menu, SWT.CHECK);
		spPlayer1.setText("Back/Lay Spread " + match.getPlayer1());

		final MenuItem spPlayer2 = new MenuItem(menu, SWT.CHECK);
		spPlayer2.setText("Back/Lay Spread " + match.getPlayer2());
		
		final MenuItem stretch = new MenuItem(menu, SWT.CHECK);
		stretch.setText("Stretch");

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
		
		spPlayer1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				UpdatableChart.this.changeSelected(5);
			}
		});
		
		spPlayer2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				UpdatableChart.this.changeSelected(6);
			}
		});
		
		stretch.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                SashForm sf = (SashForm)UpdatableChart.this.getParent().getParent(); 
                if (sf.getMaximizedControl() == null )
                    sf.setMaximizedControl(UpdatableChart.this.getParent());
                else
                    sf.setMaximizedControl(null);
            }
        });
        

		// first player selected by default
		player1.setSelection(true);

		// set the menu
		this.setMenu(menu);
		this.getPlotArea().setMenu(menu);
	}

	public ILineSeries getFirstSeries() {
		return firstSeries;
	}

	public ILineSeries getSecondSeries() {
		return secondSeries;
	}
}
