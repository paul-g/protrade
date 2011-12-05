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
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.exceptions.OddsButtonNotFoundException;
import org.ic.tennistrader.ui.updatable.OddsButton;
import org.ic.tennistrader.utils.Colours;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.model.BetManager;

public class BetPlacingShell {	
	private Shell betShell;
	private Text amountText;
	private Text oddsText;
	private Label errorLabel, profitLabel, liabilityLabel;
	private static Logger log = Logger.getLogger(BetPlacingShell.class);
	private static final String AMOUNT_NUMBER_EXCEPTION = "Please ensure the amount is a valid number.";
	private static final String ODDS_NUMBER_EXCEPTION = "Please ensure the odds are a valid number.";
	private static final String INVALID_PRICE = "Please ensure the odds are a valid price value.";
	private static final String PROFIT_TEXT = "Your possible profit: ";
	private static final String LIABILITY_TEXT = "Your possible liability: ";
	private static final double INITIAL_AMOUNT = 10;
	private double initialOdds;	
	private Label firstPlayerWinnerSummary;
	private Label secondPlayerWinnerSummary;
	private String firstPlayerWinnerText;
	private String secondPlayerWinnerText;
	
	public BetPlacingShell(final OddsButton oddsButton, final BetController betController) {
		createBetShell(oddsButton);
		
		setGridLayout();        
		GridData infoGridData = getInfoGridData();
		GridData gridData = getFirstGridData();        
        GridData gridData2 = getSecondGridData();
        
        initialOdds = Double.parseDouble(oddsButton.getOdds().getText());
        
		createInfoLabel(oddsButton, betController, infoGridData);		        
		createOddsFields(gridData, gridData2);
        createAmountFields(gridData, gridData2);
        addTextFieldsListeners(betController, oddsButton);
        
        createSubmitButton(oddsButton, betController);        
        createCancelButton();
		
        createProfitAndLiabilityLabels(oddsButton, betController, infoGridData);
        createWinnerProfitLabels(oddsButton, betController, infoGridData);
        
        createErrorLabel(infoGridData);
        
        //betShell.pack();
		betShell.open();
	}


	private void createWinnerProfitLabels(OddsButton oddsButton, BetController betController,
			GridData infoGridData) {
		firstPlayerWinnerSummary = new Label(betShell, SWT.NONE);
		firstPlayerWinnerSummary.setLayoutData(infoGridData);
		firstPlayerWinnerText = "If " + betController.getMatch().getPlayerOne()
				+ " wins, overall: ";

		secondPlayerWinnerSummary = new Label(betShell, SWT.NONE);
		secondPlayerWinnerSummary.setLayoutData(infoGridData);
		secondPlayerWinnerText = "If "
				+ betController.getMatch().getPlayerTwo() + " wins, overall: ";

		updateOverallPossibleProfits(oddsButton, betController);
	}

	private void updateOverallPossibleProfits(OddsButton oddsButton, BetController betController) {
		Match match = betController.getMatch();
		double firstPlayerWinnerProfit = BetManager
				.getFirstPlayerWinnerProfit(match);
		double secondPlayerWinnerProfit = BetManager
				.getSecondPlayerWinnerProfit(match);
		firstPlayerWinnerProfit += getPlayerWinnerProfit(oddsButton, betController, PlayerEnum.PLAYER1);
		secondPlayerWinnerProfit += getPlayerWinnerProfit(oddsButton, betController, PlayerEnum.PLAYER2);

		firstPlayerWinnerSummary.setText(firstPlayerWinnerText
				+ BetsDisplay.DOUBLE_FORMAT.format(firstPlayerWinnerProfit));
		secondPlayerWinnerSummary.setText(secondPlayerWinnerText
				+ BetsDisplay.DOUBLE_FORMAT.format(secondPlayerWinnerProfit));
	}


	private double getPlayerWinnerProfit(OddsButton oddsButton,
			BetController betController, PlayerEnum winner) {
		try {
			if ((betController.getBetType(oddsButton).equals(BetTypeEnum.B) && betController
					.getBetPlayer(oddsButton).equals(winner))
					|| (betController.getBetType(oddsButton).equals(
							BetTypeEnum.L) && !betController.getBetPlayer(
							oddsButton).equals(winner)))
				return getPossibleProfit(oddsButton, betController);
			else
				return (-1) * getPossibleLiability(oddsButton, betController);
		} catch (OddsButtonNotFoundException e) {
			return 0;
		}
	}

	private void addTextFieldsListeners(final BetController betController,
			final OddsButton oddsButton) {
		amountText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				updateProfitAndLiability(oddsButton, betController);
				updateOverallPossibleProfits(oddsButton, betController);
				try {
					Double.parseDouble(amountText.getText());					
					errorLabel.setVisible(false);
				} catch (NumberFormatException nfe) {
					setErrorText(AMOUNT_NUMBER_EXCEPTION);
					return;
				}
			}
		});
		oddsText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				updateProfitAndLiability(oddsButton, betController);
				updateOverallPossibleProfits(oddsButton, betController);
				try {
					double odds = Double.parseDouble(oddsText.getText());
					if (BetManager.isValidPrice(odds))
						errorLabel.setVisible(false);
					else
						setErrorText(INVALID_PRICE);
				} catch (NumberFormatException nfe) {
					setErrorText(ODDS_NUMBER_EXCEPTION);
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
		profitLabel.setText(PROFIT_TEXT
				+ BetsDisplay.DOUBLE_FORMAT.format(getPossibleProfit(
						oddsButton, betController)));
		liabilityLabel.setText(LIABILITY_TEXT
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
    	errorLabel.setText(AMOUNT_NUMBER_EXCEPTION);
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
		final Button submitButton = new Button(betShell, SWT.NONE);
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
					setErrorText(AMOUNT_NUMBER_EXCEPTION);
					return;
				}
				try {
				odds =  Double.parseDouble(oddsText.getText());
				} catch (NumberFormatException nfe) {
					setErrorText(ODDS_NUMBER_EXCEPTION);
					return;
				}
				if (BetManager.isValidPrice(odds)) {
					betController.addBet(oddsButton, amount, odds);
					betShell.dispose();
				} else
					setErrorText(INVALID_PRICE);
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
        amountText.setText("" + INITIAL_AMOUNT);
        amountText.setLayoutData(gridData2);        
	}

	private void createOddsFields(GridData gridData,
			GridData gridData2) {
		Label oddsLabel = new Label(betShell, SWT.NONE);
		oddsLabel.setText("Odds: ");
		oddsLabel.setLayoutData(gridData);

        oddsText = new Text(betShell, SWT.NONE);
        oddsText.setText("" + initialOdds);
        oddsText.setLayoutData(gridData2);        
	}

	private GridData getInfoGridData() {
		GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData.horizontalSpan = 6;
		return gridData;
	}

	private GridData getSecondGridData() {
		GridData gridData2 = new GridData(150, 20);
        gridData2.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData2.horizontalSpan = 4;
		return gridData2;
	}

	private GridData getFirstGridData() {
		GridData gridData = new GridData(75, 20);
        gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData.horizontalSpan = 2;
		return gridData;
	}

	private void setGridLayout() {
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        //gridLayout.makeColumnsEqualWidth = true;
        betShell.setLayout(gridLayout);
	}
	
	public void setLocation(int x, int y){
		betShell.setLocation(x, y);
	}
}
