package org.ic.tennistrader.ui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.ic.tennistrader.controller.BetController;
import org.ic.tennistrader.exceptions.MaximumBetAmountExceededException;
import org.ic.tennistrader.exceptions.OddsButtonNotFoundException;
import org.ic.tennistrader.ui.updatable.OddsButton;

public class BetShell {	
	private Shell betShell;
	//private BetController betController;
	//private OddsButton oddsButton;
	private Text amountText;
	private Text oddsText;
	private static Logger log = Logger.getLogger(BetShell.class);
	
	public BetShell(final OddsButton oddsButton, final BetController betController) {
		//this.betController = betController;
		//this.oddsButton = oddsButton;
		createBetShell(oddsButton);
		
		setGridLayout();        
		GridData infoGridData = getInfoGridData();
		GridData gridData = getFirstGridData();        
        GridData gridData2 = getSecondGridData();
        
		createInfoLabel(oddsButton, betController, infoGridData);		        
		createOddsFields(oddsButton.getOdds().getText(), gridData, gridData2);
        createAmountFields(gridData, gridData2);
        
        createSubmitButton(oddsButton, betController);        
        createCancelButton();
		
        betShell.pack();
		betShell.open();
	}

    private void createInfoLabel(final OddsButton oddsButton,
            final BetController betController, GridData infoGridData) {
        try {
            Label infoLabel = new Label(betShell, SWT.NONE);
            infoLabel.setText(betController.getBettingDetails(oddsButton));
            infoLabel.setLayoutData(infoGridData);
        } catch (OddsButtonNotFoundException e) {
            log.error(e.getMessage());
        }
    }
	
	private void createCancelButton() {
		Button cancelButton = new Button(betShell, SWT.NONE);
        cancelButton.setText("Cancel");
        cancelButton.addListener(SWT.MouseUp, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				betShell.dispose();
			}        	
        });
	}

	private void createSubmitButton(final OddsButton oddsButton,
			final BetController betController) {
		Button submitButton = new Button(betShell, SWT.NONE);
		submitButton.setText("Place bet");
		submitButton.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				try {
                    betController.addBet(oddsButton, Double.parseDouble(amountText
                    		.getText()), Double.parseDouble(oddsText.getText()));
                    betShell.dispose();
                } catch (MaximumBetAmountExceededException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
			}
		});
	}

	private void createBetShell(OddsButton oddsButton) {
		this.betShell = new Shell(oddsButton.getComp().getDisplay());
		betShell.setSize(350, 600);
		betShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	private void createAmountFields(GridData gridData, GridData gridData2) {
		Label amountLabel = new Label(betShell, SWT.NONE);
        amountLabel.setText("Amount (£): ");
        amountLabel.setLayoutData(gridData);
        
        amountText = new Text(betShell, SWT.NONE);
        amountText.setText("10");
        amountText.setLayoutData(gridData2);
	}

	private void createOddsFields(String odds, GridData gridData,
			GridData gridData2) {
		Label oddsLabel = new Label(betShell, SWT.NONE);
		oddsLabel.setText("Odds: ");
		oddsLabel.setLayoutData(gridData);

        oddsText = new Text(betShell, SWT.NONE);
        oddsText.setText(odds);
        oddsText.setLayoutData(gridData2);
	}

	private GridData getInfoGridData() {
		GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData.horizontalSpan = 5;
		return gridData;
	}

	private GridData getSecondGridData() {
		GridData gridData2 = new GridData(150, 16);
        gridData2.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData2.horizontalSpan = 4;
		return gridData2;
	}

	private GridData getFirstGridData() {
		GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData.horizontalSpan = 1;
		return gridData;
	}

	private void setGridLayout() {
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        gridLayout.marginTop = 0;
        betShell.setLayout(gridLayout);
	}
	
	public void setLocation(int x, int y){
		betShell.setLocation(x, y);
	}

}
