package org.ic.tennistrader.ui.chart;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.model.SeriesComputer;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;

public abstract class UpdatableChart extends Chart implements UpdatableWidget {
	private static final Logger log = Logger.getLogger(UpdatableChart.class);
	protected ChartData chartData;
	private ConcurrentHashMap<SeriesProperties, SeriesComputer> computerSeries = new ConcurrentHashMap<SeriesProperties, SeriesComputer>();;
	protected Slider slider;
	protected final int sampleSize = 200;
	private int startingIndex = 0;
	private Composite parent;

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		//computerSeries 
	}

	public void addSeries(SeriesComputer seriesComputer,
			SeriesProperties properties) {
		properties.setChartSeries(this.getSeriesSet().createSeries(
				properties.getChartType(), properties.getName()));
		properties.setSelected(true);
		properties.getChartSeries().setVisible(true);
		computerSeries.put(properties, seriesComputer);
		updateSeriesProperties(properties);
		// TODO add to chart; make menus with reference to properties
		if (chartData != null) {
			properties.getChartSeries().setYSeries(
					seriesComputer.computeValues(properties.getPlayer(),
							chartData, startingIndex));
		}
	}

	public void removeSeries(SeriesProperties properties) {
		properties.setSelected(false);
		properties.getChartSeries().setVisible(false);
		computerSeries.remove(properties);
		// TODO remove from the chart
	}

	public void updateSeriesProperties(SeriesProperties properties) {
		switch (properties.getChartType()) {
		case LINE: {
			ILineSeries lineSeries = (ILineSeries) properties.getChartSeries();
			lineSeries.setVisible(true);
			break;
		}
		case BAR: {
			IBarSeries barSeries = (IBarSeries) properties.getChartSeries();
			barSeries.setVisible(true);
			break;
		}

		}
	}

	public void setMatch(Match match) {
		log.info("Set match " + match);
	}

	public void setChartData(ChartData chartData) {
		this.chartData = chartData;
	}

	public void setSlider(Slider slider) {
		this.slider = slider;
	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		addDisposeListener(listener);
	}

	@Override
	public void handleUpdate(final MOddsMarketData newData) {
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

	protected abstract void updateData(MOddsMarketData data);

	protected void updateDisplay() {
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
	
	protected void makeMenu(ChartMenu chartMenu) {
		Menu menu = new Menu(parent.getShell());
		this.setMenu(menu);
		this.getPlotArea().setMenu(menu);
		for(final SeriesProperties prop : chartMenu.getSeriesItems()) {
			final MenuItem newMenuItem = new MenuItem(menu, SWT.PUSH);
			newMenuItem.setText(prop.getName());
			newMenuItem.setSelection(prop.isSelected());
			newMenuItem.addListener(SWT.Selection, new Listener(){
				@Override
				public void handleEvent(Event arg0) {
					prop.setSelected(!prop.isSelected());
					newMenuItem.setSelection(prop.isSelected());
					if (prop.isSelected())
						addSeries(prop.getComputer(), prop);
					else
						removeSeries(prop);
					updateDisplay();
				}
			});
		}
	}

	/*
	 * public void showSeries(int i, boolean dragged) { int size = (i) <
	 * sampleSize ? (i) : sampleSize; int seriesNr = computerSeries.size(); //
	 * ??? Date showXSeries[] = new Date[size]; ArrayList<double[]> dataArray =
	 * new ArrayList<double[]>(); for (int k = 0; k < seriesNr; k++) {
	 * dataArray.add(k, new double[size]); } int z = i < sampleSize ? 0 : 1; //
	 * i - 1 for overround.. ???
	 * 
	 * if (slider.getMaximum() == slider.getSelection() + 1 || dragged) { //
	 * TODO some pow and k for odds chart int b = (i - sampleSize + 1) * z;
	 * this.startingIndex = b; for (int a = 0; a < size; a++) { int nr = 0;
	 * showXSeries[a] = chartData.getxSeries().get(b);
	 * 
	 * ConcurrentHashMap<ISeries, SeriesComputer> newComputerSeries = new
	 * ConcurrentHashMap<ISeries, SeriesComputer>(); // TODO display for each
	 * series appropriate range of values for (SeriesProperties seriesProp :
	 * computerSeries.keySet()) { SeriesComputer computer =
	 * computerSeries.get(seriesProp); ISeries series =
	 * seriesProp.getChartSeries();
	 * series.setYSeries(computer.computeValues(seriesProp .getPlayer(),
	 * chartData, b)); series.setXDateSeries(showXSeries); } } // adjust()
	 * ???overround updateDisplay(); } }
	 */

}
