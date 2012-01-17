package org.ic.protrade.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.ic.protrade.data.match.HistoricalMatch;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.service.DataManager;
import org.ic.protrade.ui.DisplayTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdatableMarketDataGridTest extends DisplayTest {
	private String filename;
	private Match match;
	private Shell shell;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		shell = new Shell(display, SWT.NONE);
		filename = "data/test/fracsoft-reader/tso-fed.csv";
		match = new HistoricalMatch(filename, null);
		new MarketDataGrid(shell, SWT.NONE, match);
	}

	@Override
	@After
	public void tearDown() {
		DataManager.stopAllThreads();
		super.tearDown();
	}

	@Test
	public void testStartup() {
		// init was successfull
	}
}