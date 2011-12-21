package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.DisplayTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdatableChartTest extends DisplayTest{
	private UpdatableChart chart;
	private String filename;
	private Match match;
	private Shell shell;
	private Slider slider;
	
	@Before
	public void setUp() {
		super.setUp();
	    shell = new Shell(display, SWT.NONE);
		filename = "data/test/fracsoft-reader/tso-fed.csv";
	    match = new HistoricalMatch(filename);
	    slider = new Slider(shell, SWT.BORDER);
		chart = new UpdatableChart(shell, SWT.BORDER, match, slider);
	}	
	
	@After
	public void tearDown() {
		LiveDataFetcher.stopAllThreads();
		super.tearDown();
	}
	
	@Test
    public void invertAxis(){
        chart.fillData(new MOddsMarketData());
        chart.invertAxis();
    }
	
	/*
    @Test
    public void showSeries() {
        chart.fillData(new MOddsMarketData());
        chart.showSeries(0, false);
    }*/
}