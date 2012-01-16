package org.ic.tennistrader.ui.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.model.chart_computers.SeriesComputer;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;

public abstract class UpdatableChart extends Chart implements UpdatableWidget {
	private static final Logger log = Logger.getLogger(UpdatableChart.class); 
	protected ChartData chartData;
	private ConcurrentHashMap<SeriesProperties, SeriesComputer> computerSeries = new ConcurrentHashMap<SeriesProperties, SeriesComputer>();;
	protected List<SeriesProperties> initialSeries;
	protected Slider slider;
	protected final int sampleSize = 200;
	private int startingIndex = 0;
	private Composite parent;
	private Menu menu;
	protected ChartMenu chartMenu;
	protected String player1Name;
	protected String player2Name;
	protected boolean inverted = false;
	protected String yAxisTitle;
	protected String yAxisInvertedTitle;

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		initialSeries = new ArrayList<SeriesProperties>();
		//computerSeries 
	}
	
	protected void addInitialSeries() {
		for (SeriesProperties seriesProp : initialSeries){
			addSeries(seriesProp.getComputer(), seriesProp);
		}
	}

	public void addSeries(SeriesComputer seriesComputer,
			SeriesProperties properties) {
		properties.setChartSeries(this.getSeriesSet().createSeries(
				properties.getChartType(), properties.getFullName()));
		properties.setSelected(true);
		properties.getChartSeries().setVisible(true);
		properties.updateSeriesProperties();
		computerSeries.put(properties, seriesComputer);
		updateSeriesProperties(properties);
		// TODO add to chart; make menus with reference to properties
		if (chartData != null) {
			
			computeValues(properties);
			//properties.getChartSeries().setXDateSeries(new Date[]);
		}
	}

	private void computeValues(SeriesProperties properties) {
		SeriesComputer seriesComputer = properties.getComputer();
		properties.getChartSeries().setYSeries(
				seriesComputer.computeValues(properties.getPlayer(),
						chartData, startingIndex, inverted));
		if (properties.getMarketType().equals(MarketSeriesType.BACK_ODDS)) {
			properties.getErrorBar().setPlusErrors(
					seriesComputer.computePlusErrors(properties.getPlayer(),
							chartData, startingIndex, inverted));
			properties.getErrorBar().setMinusErrors(
					seriesComputer.computeMinusErrors(properties.getPlayer(),
							chartData, startingIndex, inverted));
		}
	}

	public void removeSeries(SeriesProperties properties) {
		properties.setSelected(false);
		properties.getChartSeries().setVisible(false);
		this.getSeriesSet().deleteSeries(properties.getFullName());
		computerSeries.remove(properties);
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
		this.player1Name = match.getPlayerOne().toString();
		this.player2Name = match.getPlayerTwo().toString();
		for (SeriesProperties seriesProp : chartMenu.getSeriesItems()) {
			seriesProp.setPlayerName(seriesProp.getPlayer().equals(
					PlayerEnum.PLAYER1) ? player1Name : player2Name);
		}
		makeMenu();
		
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
		updateData(newData);
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				
				/*
				for (SeriesProperties prop : computerSeries.keySet()) {
					if (chartData != null) {
						prop.getChartSeries().setYSeries(
								computerSeries.get(prop).addValue(
										prop.getPlayer(), chartData,
										newData.getPl1LastMatchedPrice(),
										newData.getPl2LastMatchedPrice()));
						prop.getChartSeries().setXSeries(...);
					}
				}
				updateDisplay();
				*/
			}
		});
	}

	protected abstract void updateData(MOddsMarketData data);

	protected void updateDisplay() {
		final Composite parent = getParent();
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!isDisposed()) {
					UpdatableChart.this.getAxisSet().getYAxis(0).adjustRange();
					if (inverted)
						UpdatableChart.this.getAxisSet().getYAxis(0).getTitle()
								.setText(yAxisInvertedTitle);
					else
						UpdatableChart.this.getAxisSet().getYAxis(0).getTitle()
								.setText(yAxisTitle);
					redraw();
				}
				if (parent != null && !parent.isDisposed())
					parent.update();
			}
		});
	}
	
	protected void makeMenu() {
		addInitialSeries();
		menu = new Menu(parent.getShell(), SWT.POP_UP);
		this.setMenu(menu);
		this.getPlotArea().setMenu(menu);
		for(final SeriesProperties prop : chartMenu.getSeriesItems()) {
			createMenuItem(menu, prop);
		}
		
		MenuItem settingsItem = new MenuItem(menu, SWT.BUTTON1);
		settingsItem.setText("Settings...");
		settingsItem.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				//String namePl1 = "Player1";
				//String namePl2 = "Player2";
				//Composite settingsPanel = new Composite(form, SWT.BORDER);
				ChartSettings csd = new ChartSettings(UpdatableChart.this.getParent().getShell(),
						UpdatableChart.this);
				csd.open();
			}
		});
		
		MenuItem invertItem = new MenuItem(menu, SWT.BUTTON1);
		invertItem.setText("Invert axis");
		invertItem.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				UpdatableChart.this.invertAxis();
			}			
		});
	}
	
	protected abstract void invertAxis();
	/*
	private void updateMenu() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		for (MenuItem mi : menuItems) mi.dispose();
	}
	*/
	
	private void createMenuItem(Menu menu, final SeriesProperties prop) {
		final MenuItem newMenuItem = new MenuItem(menu, SWT.CHECK);
		newMenuItem.setText(prop.getFullName());
		newMenuItem.setSelection(prop.isSelected());
		newMenuItem.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if (newMenuItem.getSelection())
					addSeries(prop.getComputer(), prop);
				else
					removeSeries(prop);
				updateDisplay();
			}
		});
		if(prop.getMarketType().equals(MarketSeriesType.BACK_ODDS)) {
			addSpreadMenuItem(menu, prop, newMenuItem);
		}
	}

	private void addSpreadMenuItem(Menu menu, final SeriesProperties prop,
			final MenuItem newMenuItem) {
		final MenuItem spreadMenuItem = new MenuItem(menu, SWT.CHECK);
		spreadMenuItem.setText("Spread " + prop.getFullName());
		spreadMenuItem.setSelection(false);
		spreadMenuItem.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				if (newMenuItem.getSelection() && spreadMenuItem.getSelection())
					addErrorSeries(prop.getComputer(), prop);
				else if (newMenuItem.getSelection())
					removeErrorSeries(prop);
				updateDisplay();
			}
		});
	}

	protected void addErrorSeries(SeriesComputer computer, SeriesProperties prop) {
		prop.setVisibleErrorBar(true);
	}
	
	protected void removeErrorSeries(SeriesProperties prop) {
		prop.setVisibleErrorBar(false);
	}

	

	public void setChartMenu(ChartMenu chartMenu) {
		this.chartMenu = chartMenu;
	}

	public ChartMenu getChartMenu() {
		return chartMenu;
	}
	
	public int getSampleSize() {
		return this.sampleSize;
	}

	
	public void showSeries(int i, final boolean dragged) {
		int size = (i) < sampleSize ? (i) : sampleSize;
		int seriesNr = computerSeries.size();
		// ???
		Date showXSeries[] = new Date[size];
		ArrayList<double[]> dataArray = new ArrayList<double[]>();
		for (int k = 0; k < seriesNr; k++) {
			dataArray.add(k, new double[size]);
		}
		int z = i < sampleSize ? 0 : 1;
		// i - 1 for overround.. ???

		// TODO some pow and k for odds chart
		int b = (i - sampleSize + 1) * z;
		this.startingIndex = b;
		for (int a = 0; a < size; a++) {
			showXSeries[a] = chartData.getxSeries().get(b + a);

			// TODO display for each series appropriate range of values

		}

		for (SeriesProperties seriesProp : computerSeries.keySet()) {
			/*
			 * seriesProp.getChartSeries().setYSeries(computer.computeValues(seriesProp
			 * .getPlayer(), chartData, b));
			 */
			this.startingIndex = b;
			computeValues(seriesProp);
			seriesProp.getChartSeries().setXDateSeries(showXSeries);
		}

		this.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (slider.getMaximum() == slider.getSelection() + 1 || dragged) {
					// adjust() ???overround
					updateDisplay();
				}
			}			
		});
		
	}
	 
	public String getPlayer1Name() {
		return player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}
}
