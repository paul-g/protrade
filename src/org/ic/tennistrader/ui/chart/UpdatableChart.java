package org.ic.tennistrader.ui.chart;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.model.SeriesComputer;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;

public abstract class UpdatableChart extends Chart implements UpdatableWidget {
	private static final Logger log = Logger.getLogger(UpdatableChart.class);
	protected ChartData chartData;
	private ConcurrentHashMap<SeriesComputer, ISeries>  computerSeries;
	protected Slider slider;
	protected final int sampleSize = 200;

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
		computerSeries = new ConcurrentHashMap<SeriesComputer, ISeries>();
	}

	public void addSeries(SeriesComputer seriesComputer,
			SeriesProperties properties) {
		properties.setChartSeries(this.getSeriesSet().createSeries(
			properties.getChartType(), properties.getName()));
		computerSeries.put(seriesComputer, properties.getChartSeries());
		setSeriesProperties(properties);

	}
	
	
	
	private void setSeriesProperties(SeriesProperties properties) {
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
}
