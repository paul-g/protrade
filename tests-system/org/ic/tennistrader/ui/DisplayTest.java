package org.ic.tennistrader.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public abstract class DisplayTest {
	protected Display display;
	protected Shell comp;
	
	@Before
	public void setUp() {
		display = new Display();
		comp = new Shell(display);
	}
	
	@After  
    public void tearDown() {
        while (display.readAndDispatch()){
            // handle remaining work
        }
        display.dispose();
    }
}
