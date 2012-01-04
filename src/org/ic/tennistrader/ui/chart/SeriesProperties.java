package org.ic.tennistrader.ui.chart;

import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.model.SeriesComputer;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;

public class SeriesProperties {
	private SeriesType chartType;
	private MarketSeriesType marketType;
	private PlayerEnum player;
	private String name;
	private ISeries chartSeries;
	private boolean selected;
	private SeriesComputer computer;

	public SeriesProperties(SeriesType chartType, MarketSeriesType marketType,
			PlayerEnum player, String name, SeriesComputer computer) {
		this.chartType = chartType;
		this.marketType = marketType;
		this.name = name;
		this.player = player;
		this.selected = false;
		this.computer = computer;
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

	public PlayerEnum getPlayer() {
		return player;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public SeriesComputer getComputer() {
		return computer;
	}
}
