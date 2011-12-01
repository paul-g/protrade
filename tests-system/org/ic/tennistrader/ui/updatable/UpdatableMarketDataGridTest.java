package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdatableMarketDataGridTest {

    private UpdatableMarketDataGrid grid;
    private String filename;
    private Match match;
    private Display display = new Display();
    private Shell shell = new Shell(display, SWT.NONE);
    
    @Before
    public void setUp() {
        filename = "data/test/fracsoft-reader/tso-fed.csv";
        match = new HistoricalMatch(filename);
        grid = new UpdatableMarketDataGrid(shell, SWT.NONE, match);
    }   
    
    @After
    public void tearDown() {
        LiveDataFetcher.stopAllThreads();
        display.dispose();
    }
    
    @Test
    public void testStartup() {
        // init was successfull
    }
}