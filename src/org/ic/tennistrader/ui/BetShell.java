package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BetShell {
	private Shell betShell;
	
	public BetShell(Display display, String odds) {
		this.betShell = new Shell(display);
		betShell.setSize(350, 600);
		betShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        gridLayout.marginTop = 0;
        betShell.setLayout(gridLayout);
        
		GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData.horizontalSpan = 1;
        
        GridData gridData2 = new GridData(150, 16);
        gridData2.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData2.horizontalSpan = 4;
        
		Label oddsLabel = new Label(betShell, SWT.NONE);
		oddsLabel.setText("Odds: ");
		oddsLabel.setLayoutData(gridData);

        final Text oddsText = new Text(betShell, SWT.NONE);
        oddsText.setText(odds);
        oddsText.setLayoutData(gridData2);

        Label amountLabel = new Label(betShell, SWT.NONE);
        amountLabel.setText("Amount (£): ");
        amountLabel.setLayoutData(gridData);
        
        final Text amountText = new Text(betShell, SWT.NONE);
        amountText.setText("10");
        amountText.setLayoutData(gridData2);
		
		betShell.open();
	}

}
