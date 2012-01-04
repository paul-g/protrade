package org.ic.tennistrader.ui.chart;

import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;

public class SeriesProperties {
	private SeriesType chartType;
	private MarketSeriesType marketType;
	private String name;
	private ISeries chartSeries;

	public SeriesProperties(SeriesType chartType, MarketSeriesType marketType, String name) {
		this.chartType = chartType;
		this.marketType = marketType;
		this.name = name;
	}
	

	public SeriesType getChartType() {
		return chartType;
	}

	public MarketSeriesType getMarketType() {
		return marketType;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setChartSeries(ISeries chartSeries) {
		this.chartSeries = chartSeries;
	}


	public ISeries getChartSeries() {
		return chartSeries;
	}
}
