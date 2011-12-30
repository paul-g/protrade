package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.DisplayTest;
import org.ic.tennistrader.ui.chart.UpdatableChart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdatableChartTest extends DisplayTest{
	private UpdatableChart chart;
	private String filename;
	private Match match;
	private Slider slider;
	private ChartData chartData;
	
	@Before
	public void setUp() {
		super.setUp();
		filename = "data/test/fracsoft-reader/tso-fed.csv";
	    match = new HistoricalMatch(filename);
	    slider = new Slider(shell, SWT.BORDER);
	    chartData =  new ChartData(null);
		chart = new UpdatableChart(shell, SWT.BORDER, match, slider, chartData);
	}	
	
	@After
	public void tearDown() {
		LiveDataFetcher.stopAllThreads();
		super.tearDown();
	}
	
	@Test
    public void invertAxis(){
		chart.invertAxis();
		chartData.updateData(new MOddsMarketData());
        chart.invertAxis();
    }
	
	/*
    @Test
    public void showSeries() {
        chart.fillData(new MOddsMarketData());
        chart.showSeries(0, false);
    }*/
}