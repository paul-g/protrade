package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.score.PredictionGui;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PredictionGuiTest {
    
    private String filename;
    private Match match;
    private Display display = new Display();
    private Shell shell = new Shell(display, SWT.NONE);
    
    @Before
    public void setUp() {
        filename = "data/test/fracsoft-reader/tso-fed.csv";
        match = new HistoricalMatch(filename);
        new PredictionGui(shell, SWT.BORDER, match);
    }   
    
    @After
    public void tearDown() {
        LiveDataFetcher.stopAllThreads();
        while (display.readAndDispatch()){
            // handle remaining work
        }
        display.dispose();
    }
    

    @Test
    public void testStartup() {
        
    }
	
}