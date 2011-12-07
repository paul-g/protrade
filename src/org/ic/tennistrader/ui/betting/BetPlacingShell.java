package org.ic.tennistrader.ui.betting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.utils.Colours;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.model.BetManager;

public class BetPlacingShell {
	private Shell betShell;
	private Text amountText;
	private Text oddsText;
	private Label errorLabel, profitLabel, liabilityLabel;
	public Label getErrorLabel() {
		return errorLabel;
	}

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

	public BetPlacingShell(Display display, Match match, PlayerEnum betPlayer,
			BetTypeEnum betType, double initialOdds, String betDetails) {
		createBetShell(display);

		setGridLayout();
		GridData infoGridData = getInfoGridData();
		GridData gridData = getFirstGridData();
		GridData gridData2 = getSecondGridData();

		this.initialOdds = initialOdds;

		createInfoLabel(betType, betDetails, infoGridData);
		createOddsFields(gridData, gridData2);
		createAmountFields(gridData, gridData2);
		addTextFieldsListeners(match, betType, betPlayer);

		createSubmitButton(match, betPlayer, betType);
		createCancelButton();

		createProfitAndLiabilityLabels(betType, infoGridData);
		createWinnerProfitLabels(match, betType, betPlayer, infoGridData);

		createErrorLabel(infoGridData);

		// betShell.pack();
		betShell.open();
	}

	private void createWinnerProfitLabels(Match match, BetTypeEnum betType,
			PlayerEnum betPlayer, GridData infoGridData) {
		firstPlayerWinnerSummary = new Label(betShell, SWT.NONE);
		firstPlayerWinnerSummary.setLayoutData(infoGridData);
		firstPlayerWinnerText = "If " + match.getPlayerOne()
				+ " wins, overall: ";

		secondPlayerWinnerSummary = new Label(betShell, SWT.NONE);
		secondPlayerWinnerSummary.setLayoutData(infoGridData);
		secondPlayerWinnerText = "If " + match.getPlayerTwo()
				+ " wins, overall: ";

		updateOverallPossibleProfits(match, betType, betPlayer);
	}

	private void updateOverallPossibleProfits(Match match, BetTypeEnum betType,
			PlayerEnum betPlayer) {
		double firstPlayerWinnerProfit = BetManager
				.getFirstPlayerWinnerProfit(match);
		double secondPlayerWinnerProfit = BetManager
				.getSecondPlayerWinnerProfit(match);
		firstPlayerWinnerProfit += getPlayerWinnerProfit(betType, betPlayer,
				PlayerEnum.PLAYER1);
		secondPlayerWinnerProfit += getPlayerWinnerProfit(betType, betPlayer,
				PlayerEnum.PLAYER2);

		firstPlayerWinnerSummary.setText(firstPlayerWinnerText
				+ BetsDisplay.DOUBLE_FORMAT.format(firstPlayerWinnerProfit));
		secondPlayerWinnerSummary.setText(secondPlayerWinnerText
				+ BetsDisplay.DOUBLE_FORMAT.format(secondPlayerWinnerProfit));
	}

	private double getPlayerWinnerProfit(BetTypeEnum betType,
			PlayerEnum betPlayer, PlayerEnum winner) {
		if ((betType.equals(BetTypeEnum.B) && betPlayer.equals(winner))
				|| (betType.equals(BetTypeEnum.L) && !betPlayer.equals(winner)))
			return getPossibleProfit(betType);
		else
			return (-1) * getPossibleLiability(betType);
	}

	private void addTextFieldsListeners(final Match match,
			final BetTypeEnum betType, final PlayerEnum betPlayer) {
		amountText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				updateProfitAndLiability(betType);
				updateOverallPossibleProfits(match, betType, betPlayer);
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
				updateProfitAndLiability(betType);
				updateOverallPossibleProfits(match, betType, betPlayer);
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

	private void createProfitAndLiabilityLabels(BetTypeEnum betType,
			GridData infoGridData) {
		profitLabel = new Label(betShell, SWT.NONE);
		profitLabel.setLayoutData(infoGridData);

		liabilityLabel = new Label(betShell, SWT.NONE);
		liabilityLabel.setLayoutData(infoGridData);

		updateProfitAndLiability(betType);
	}

	private void updateProfitAndLiability(BetTypeEnum betType) {
		profitLabel.setText(PROFIT_TEXT
				+ BetsDisplay.DOUBLE_FORMAT.format(getPossibleProfit(betType)));
		liabilityLabel.setText(LIABILITY_TEXT
				+ BetsDisplay.DOUBLE_FORMAT
						.format(getPossibleLiability(betType)));
	}

	private double getPossibleLiability(BetTypeEnum betType) {
		Double amount, odds;
		try {
			amount = Double.parseDouble(amountText.getText());
			odds = Double.parseDouble(oddsText.getText());
			if (betType.equals(BetTypeEnum.B))
				return amount;
			else
				return (odds - 1) * amount;
		} catch (Exception e) {
		}
		return 0.0;
	}

	private double getPossibleProfit(BetTypeEnum betType) {
		Double amount, odds;
		try {
			amount = Double.parseDouble(amountText.getText());
			odds = Double.parseDouble(oddsText.getText());
			if (betType.equals(BetTypeEnum.B))
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

	private void createInfoLabel(BetTypeEnum betType, String betDetails,
			GridData infoGridData) {
		Label infoLabel = new Label(betShell, SWT.NONE);
		infoLabel.setText(betDetails);
		if (betType.equals(BetTypeEnum.B)) {
			infoLabel.setBackground(Colours.backColor);
		} else {
			infoLabel.setBackground(Colours.layColor);
		}
		infoLabel.setLayoutData(infoGridData);
	}

	private void createCancelButton() {
		Button cancelButton = new Button(betShell, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				betShell.dispose();
			}
		});
	}

	private void createSubmitButton(final Match match,
			final PlayerEnum betPlayer, final BetTypeEnum betType) {
		final Button submitButton = new Button(betShell, SWT.NONE);
		submitButton.setText("Place bet");
		if (betType.equals(BetTypeEnum.B)) {
			submitButton.setBackground(Colours.backColor);
		} else {
			submitButton.setBackground(Colours.layColor);
		}
		submitButton.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Double amount, odds;
				try {
					amount = Double.parseDouble(amountText.getText());
				} catch (NumberFormatException nfe) {
					setErrorText(AMOUNT_NUMBER_EXCEPTION);
					return;
				}
				try {
					odds = Double.parseDouble(oddsText.getText());
				} catch (NumberFormatException nfe) {
					setErrorText(ODDS_NUMBER_EXCEPTION);
					return;
				}
				if (BetManager.isValidPrice(odds)) {
					BetManager
							.placeBet(match, betPlayer, betType, odds, amount);
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

	private void createBetShell(Display display) {
		this.betShell = new Shell(display);
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

	private void createOddsFields(GridData gridData, GridData gridData2) {
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
		// gridLayout.makeColumnsEqualWidth = true;
		betShell.setLayout(gridLayout);
	}

	public void setLocation(int x, int y) {
		betShell.setLocation(x, y);
	}
}
