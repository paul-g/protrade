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
import org.ic.tennistrader.exceptions.OddsButtonNotFoundException;
import org.ic.tennistrader.ui.updatable.OddsButton;
import org.ic.tennistrader.utils.Colours;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class BetPlacingShell {	
	private Shell betShell;
	private Text amountText;
	private Text oddsText;
	private Label errorLabel, profitLabel, liabilityLabel;
	private static Logger log = Logger.getLogger(BetPlacingShell.class);
	private static final String amountNumberException = "Please ensure the amount is a valid number.";
	private static final String oddsNumberException = "Please ensure the odds are a valid number.";
	private static final String profitText = "Your possible profit: ";
	private static final String liabilityText = "Your total liability: ";
	
	public BetPlacingShell(final OddsButton oddsButton, final BetController betController) {
		createBetShell(oddsButton);
		
		setGridLayout();        
		GridData infoGridData = getInfoGridData();
		GridData gridData = getFirstGridData();        
        GridData gridData2 = getSecondGridData();
        
		createInfoLabel(oddsButton, betController, infoGridData);		        
		createOddsFields(oddsButton.getOdds().getText(), gridData, gridData2);
        createAmountFields(gridData, gridData2);
        addTextFieldsListeners(betController, oddsButton);
        
        createSubmitButton(oddsButton, betController);        
        createCancelButton();
		
        createProfitAndLiabilityLabels(oddsButton, betController, infoGridData);
        
        createErrorLabel(infoGridData);
        
        //betShell.pack();
		betShell.open();
	}


	private void addTextFieldsListeners(final BetController betController,
			final OddsButton oddsButton) {
		amountText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				updateProfitAndLiability(oddsButton, betController);
				try {
					Double.parseDouble(amountText.getText());
					errorLabel.setVisible(false);
				} catch (NumberFormatException nfe) {
					setErrorText(amountNumberException);
					return;
				}
			}
		});
		oddsText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				updateProfitAndLiability(oddsButton, betController);
				try {
					Double.parseDouble(oddsText.getText());
					errorLabel.setVisible(false);
				} catch (NumberFormatException nfe) {
					setErrorText(oddsNumberException);
					return;
				}
			}			
		});
	}


	private void createProfitAndLiabilityLabels(OddsButton oddsButton, BetController betController, GridData infoGridData) {
		profitLabel = new Label(betShell, SWT.NONE);    	
    	profitLabel.setLayoutData(infoGridData);
    	
    	liabilityLabel = new Label(betShell, SWT.NONE);    	
    	liabilityLabel.setLayoutData(infoGridData);		
    	
    	updateProfitAndLiability(oddsButton, betController);
	}


	private void updateProfitAndLiability(OddsButton oddsButton,
			BetController betController) {
		profitLabel.setText(profitText
				+ BetsDisplay.DOUBLE_FORMAT.format(getPossibleProfit(
						oddsButton, betController)));
		liabilityLabel.setText(liabilityText
				+ BetsDisplay.DOUBLE_FORMAT.format(getPossibleLiability(
						oddsButton, betController)));
	}


	private double getPossibleLiability(OddsButton oddsButton,
			BetController betController) {
		Double amount, odds;
		try {
			amount = Double.parseDouble(amountText.getText());
			odds = Double.parseDouble(oddsText.getText());
			if (betController.getBetType(oddsButton).equals(BetTypeEnum.B))
				return amount;
			else
				return (odds - 1) * amount;
		} catch (Exception e) {			
		}
		return 0.0;
	}


	private double getPossibleProfit(OddsButton oddsButton, BetController betController) {
		Double amount, odds;
		try {
			amount = Double.parseDouble(amountText.getText());
			odds = Double.parseDouble(oddsText.getText());
			if (betController.getBetType(oddsButton).equals(BetTypeEnum.B))
				return (odds - 1) * amount;
			else
				return amount;
		} catch (Exception e) {			
		}
		return 0.0;
	}


	private void createErrorLabel(GridData infoGridData) {
    	errorLabel = new Label(betShell, SWT.NONE);
    	errorLabel.setText(amountNumberException);
    	errorLabel.setVisible(false);
    	errorLabel.setLayoutData(infoGridData);
	}

	private void createInfoLabel(final OddsButton oddsButton,
            final BetController betController, GridData infoGridData) {
        try {
            Label infoLabel = new Label(betShell, SWT.NONE);
            infoLabel.setText(betController.getBettingDetails(oddsButton));
            if (betController.getBetType(oddsButton).equals(BetTypeEnum.B)){
            	infoLabel.setBackground(Colours.backColor);
            } else {
            	infoLabel.setBackground(Colours.layColor);
            }
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
		try {
			if (betController.getBetType(oddsButton).equals(BetTypeEnum.B)) {
				submitButton.setBackground(Colours.backColor);
			} else {
				submitButton.setBackground(Colours.layColor);
			}
		} catch (OddsButtonNotFoundException e) {
		}
		submitButton.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Double amount, odds;
				try {
				amount = Double.parseDouble(amountText
                   		.getText());
				} catch (NumberFormatException nfe) {
					setErrorText(amountNumberException);
					return;
				}
				try {
				odds =  Double.parseDouble(oddsText.getText());
				} catch (NumberFormatException nfe) {
					setErrorText(oddsNumberException);
					return;
				}
                betController.addBet(oddsButton, amount, odds);
                betShell.dispose();
			}
		});		
	}

	protected void setErrorText(String message) {
		errorLabel.setText(message);
		errorLabel.setVisible(true);
	}

	private void createBetShell(OddsButton oddsButton) {
		this.betShell = new Shell(oddsButton.getComp().getDisplay());
		betShell.setSize(350, 600);
		betShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		betShell.setText("Bet placing");
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
		GridData gridData2 = new GridData();
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
        //gridLayout.makeColumnsEqualWidth = true;
        betShell.setLayout(gridLayout);
	}
	
	public void setLocation(int x, int y){
		betShell.setLocation(x, y);
	}
}
