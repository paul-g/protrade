package org.ic.tennistrader.ui.chart;

import org.eclipse.swt.SWT;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.model.chart_computers.SeriesComputer;
import org.ic.tennistrader.ui.chart.ChartSettings.ResultSet;
import org.swtchart.IBarSeries;
import org.swtchart.IErrorBar;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.IErrorBar.ErrorBarType;
import org.swtchart.ISeries.SeriesType;

public class SeriesProperties {
	private SeriesType chartType;
	private MarketSeriesType marketType;
	private PlayerEnum player;
	private String name;
	private String playerName;
	private ISeries chartSeries;
	private IErrorBar errorBar;
	private boolean visibleErrorBar;
	private boolean selected;
	private SeriesComputer computer;
	private LineProp lineProp;

	public SeriesProperties(SeriesType chartType, MarketSeriesType marketType,
			PlayerEnum player, String name, String playerName, SeriesComputer computer, LineProp lineProp) {
		this.chartType = chartType;
		this.marketType = marketType;
		this.name = name;
		this.player = player;
		this.selected = false;
		this.computer = computer;
		this.lineProp = lineProp;
		this.chartSeries = null;
		this.errorBar = null;
		this.visibleErrorBar = false;
		this.playerName = playerName;
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

	public String getFullName() {
		String fullName = this.name;
		if (this.chartType.equals(SeriesType.LINE) && !this.marketType.equals(MarketSeriesType.OVERROUND))
			fullName += " " + this.playerName;
		else if (this.chartType.equals(SeriesType.BAR) && this.marketType.equals(MarketSeriesType.VOLUME))
			fullName += " " + this.playerName;
		return fullName;
	}

	public void setChartSeries(ISeries chartSeries) {
		this.chartSeries = chartSeries;
		this.errorBar = this.chartSeries.getYErrorBar();
		this.errorBar.setType(ErrorBarType.BOTH);
		this.errorBar.setVisible(this.visibleErrorBar);
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

	public LineProp getLineProp() {
		return lineProp;
	}

	public void setLineProp(ResultSet resultSet) {
		this.lineProp.setColor(resultSet.getColor());
		this.lineProp.setAntialias(resultSet.getAntialias() == SWT.ON);
		this.lineProp.setArea(resultSet.getArea());
		this.lineProp.setStep(resultSet.getStep());
		this.lineProp.setSymbolType(resultSet.getSymbolType());
		updateSeriesProperties();
	}

	public void updateSeriesProperties() {
		if (this.chartSeries != null) {
			if (this.chartType.equals(SeriesType.LINE)) {
				ILineSeries lineSeries = (ILineSeries) chartSeries;
				lineSeries.setLineColor(lineProp.getColor());
				lineSeries.setAntialias(lineProp.isAntialias() ? SWT.ON
						: SWT.OFF);
				lineSeries.enableStep(lineProp.isStep());
				lineSeries.enableArea(lineProp.isArea());
				lineSeries.setSymbolType(lineProp.getSymbolType());
			} else if (this.chartType.equals(SeriesType.BAR)) {
				IBarSeries barSeries = (IBarSeries) this.chartSeries;
				barSeries.setBarPadding(lineProp.getBarPadding());
				barSeries.setYAxisId(lineProp.getyAxisId());
			}
		}
	}

	public void setErrorBar(IErrorBar errorBar) {
		this.errorBar = errorBar;
	}

	public IErrorBar getErrorBar() {
		return errorBar;
	}

	public void setVisibleErrorBar(boolean visibleErrorBar) {
		this.visibleErrorBar = visibleErrorBar;
		if (this.errorBar != null)
			this.errorBar.setVisible(this.visibleErrorBar);
	}

	public boolean isVisibleErrorBar() {
		return visibleErrorBar;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
