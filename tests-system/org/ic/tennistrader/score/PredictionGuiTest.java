package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.DataManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PredictionGuiTest {

	private String filename;
	private Match match;
	private final Display display = new Display();
	private final Shell shell = new Shell(display, SWT.NONE);

	@Before
	public void setUp() {
		filename = "data/test/fracsoft-reader/tso-fed.csv";
		match = new HistoricalMatch(filename, null);
		shell.setSize(0, 0);
		shell.setLayout(new FillLayout());
		new PredictionGui(shell, SWT.BORDER, match);
	}

	@After
	public void tearDown() {
		DataManager.stopAllThreads();
		while (display.readAndDispatch()) {
			// handle remaining work
		}
		display.dispose();
	}

	@Test
	public void testStartup() {

	}

}