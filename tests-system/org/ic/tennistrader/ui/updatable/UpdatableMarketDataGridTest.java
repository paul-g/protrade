package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.DisplayTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdatableMarketDataGridTest extends DisplayTest{
    private UpdatableMarketDataGrid grid;
    private String filename;
    private Match match;
    private Shell shell;
    
    @Before
    public void setUp() {
    	super.setUp();
    	shell = new Shell(display, SWT.NONE);
        filename = "data/test/fracsoft-reader/tso-fed.csv";
        match = new HistoricalMatch(filename);
        grid = new UpdatableMarketDataGrid(shell, SWT.NONE, match);
    }   
    
    @After
    public void tearDown() {
        LiveDataFetcher.stopAllThreads();
        super.tearDown();
    }
    
    @Test
    public void testStartup() {
        // init was successfull
    }
}