package org.ic.protrade.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Slider;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.domain.ChartData;
import org.ic.protrade.service.DataManager;
import org.ic.protrade.ui.DisplayTest;
import org.ic.protrade.ui.chart.OddsChart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdatableChartTest extends DisplayTest {
	private OddsChart chart;
	private String filename;
	private Match match;
	private Slider slider;
	private ChartData chartData;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		filename = "data/test/fracsoft-reader/tso-fed.csv";
		match = new HistoricalMatch(filename, null);
		slider = new Slider(shell, SWT.BORDER);
		chartData = new ChartData(null);
		chart = new OddsChart(shell, SWT.BORDER, match, slider, chartData);
	}

	@Override
	@After
	public void tearDown() {
		DataManager.stopAllThreads();
		super.tearDown();
	}

	@Test
	public void invertAxis() {
		chart.invertAxis();
		chartData.updateData(new MOddsMarketData());
		chart.invertAxis();
	}

	/*
	 * @Test public void showSeries() { chart.fillData(new MOddsMarketData());
	 * chart.showSeries(0, false); }
	 */
}