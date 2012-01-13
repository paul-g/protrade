package org.ic.tennistrader.ui.betting;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.ui.updatable.OddsButton;
import org.ic.tennistrader.utils.Colours;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.model.betting.BetManager;

public class BetPlacingShell extends Dialog {
	private Composite betShell;
	private Text amountText;
	private Text oddsText;
	private Label errorLabel, profitLabel, liabilityLabel;
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
	private Label betProfit;

	private final Match match;
	private PlayerEnum betPlayer;
	private BetTypeEnum betType;
	private String betDetails;
	private Color color;

	public BetPlacingShell(OddsButton button, Match match,
			PlayerEnum betPlayer, BetTypeEnum betType, double initialOdds,
			String betDetails) {
		super(button.getShell());
		this.match = match;
		this.betPlayer = betPlayer;
		this.betType = betType;
		this.betDetails = betDetails;
		this.initialOdds = initialOdds;
		if (betType.equals(BetTypeEnum.B)) {
			this.color = Colours.getBackColor();
		} else {
			this.color = Colours.getLayColor();
		}

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.betShell = (Composite) super.createDialogArea(parent);
		this.getShell().setBackground(color);
		setGridLayout();
		GridData infoGridData = getInfoGridData();
		// GridData gridData = getFirstGridData();
		// GridData gridData2 = getSecondGridData();
		//
		createInfoLabel(betType, betDetails, infoGridData, color);
		createInfoRow(betType, match, betPlayer, initialOdds, infoGridData,
				color);
		Label lastRow = new Label(betShell, SWT.NONE);
		lastRow.setText(betDetails);
		lastRow.setBackground(color);
		lastRow.setLayoutData(getGridData7());
		// createOddsFields(gridData, gridData2);
		// createAmountFields(gridData, gridData2);
		// addTextFieldsListeners(match, betType, betPlayer);
		//
		// createProfitAndLiabilityLabels(betType, infoGridData);
		// createWinnerProfitLabels(match, betType, betPlayer, infoGridData);
		//
		// createErrorLabel(infoGridData);

		return super.createDialogArea(parent);
	}

	private void createInfoRow(BetTypeEnum betType, Match match,
			PlayerEnum betPlayer, double initialOdds, GridData infoGridData,
			Color color) {
		Label name = new Label(betShell, SWT.NONE);
		name.setText(match.getPlayer(betPlayer).getFirstname() + " "
				+ match.getPlayer(betPlayer).getLastname());
		name.setBackground(color);
		name.setLayoutData(infoGridData);
		oddsText = new Text(betShell, SWT.NONE);
		oddsText.setText(initialOdds + "");
		oddsText.setBackground(color);
		amountText = new Text(betShell, SWT.NONE);
		amountText.setText(100 + "");
		amountText.setBackground(color);
		betProfit = new Label(betShell, SWT.NONE);
		betProfit.setText((initialOdds * 100 - 100) + "");
		betProfit.setBackground(color);
		addTextFieldsListeners(match, betType, betPlayer);
	}

	@Override
	protected void okPressed() {

		// Display display = betShell.getDisplay();

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
			BetManager.placeBet(match, betPlayer, betType, odds, amount);
			betShell.dispose();
		} else
			setErrorText(INVALID_PRICE);
		super.okPressed();

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
//				updateProfitAndLiability(betType);
//				updateOverallPossibleProfits(match, betType, betPlayer);
				try {
					System.out.println("zdesy");
					betProfit.setText((Double.parseDouble(amountText.getText()) * Double
							.parseDouble(oddsText.getText())) + "");
//					errorLabel.setVisible(false);
					System.out.println("vishel");
				} catch (NumberFormatException nfe) {
					setErrorText(AMOUNT_NUMBER_EXCEPTION);
					return;
				}
			}
		});
		oddsText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
//				updateProfitAndLiability(betType);
//				updateOverallPossibleProfits(match, betType, betPlayer);
				try {
					double amount = Double.parseDouble(amountText.getText());
					double odds = Double.parseDouble(oddsText.getText());
					betProfit.setText((amount * odds) + "");
//					if (BetManager.isValidPrice(odds))
//						errorLabel.setVisible(false);
//					else
//						setErrorText(INVALID_PRICE);
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
			GridData infoGridData, Color c) {
		Label infoLabel = new Label(betShell, SWT.NONE);
		infoLabel.setLayoutData(infoGridData);
		Label odds = new Label(betShell, SWT.NONE);
		odds.setText("Your" + "\n" + "odds");
		Label stake = new Label(betShell, SWT.NONE);
		stake.setText("Your" + "\n" + "stake");
		Label profit = new Label(betShell, SWT.NONE);
		if (betType.equals(BetTypeEnum.B)) {
			infoLabel.setText("Back (Bet for)");
			profit.setText("Your" + "\n" + "profit");
		} else {
			infoLabel.setText("Lay (Bet against)");
			profit.setText("Your" + "\n" + "liability");
		}
		infoLabel.setBackground(c);
		odds.setBackground(c);
		stake.setBackground(c);
		profit.setBackground(c);
		createErrorLabel(infoGridData);

	}

	protected void setErrorText(String message) {
		errorLabel.setText(message);
		errorLabel.setVisible(true);
	}

	private void createBetShell(Display display) {
		this.betShell = new Shell(display);
		betShell.setSize(350, 600);
		betShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		// betShell.setText("Bet placing");
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
		gridData.horizontalSpan = 7;
		return gridData;
	}

	private GridData getGridData7() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 10;
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
		gridLayout.numColumns = 10;
		// gridLayout.makeColumnsEqualWidth = true;
		betShell.setLayout(gridLayout);
	}

	public void setLocation(int x, int y) {
		betShell.setLocation(x, y);
	}

	public Label getErrorLabel() {
		return errorLabel;
	}

	public void addDisposeListener(DisposeListener listener) {
		this.betShell.addDisposeListener(listener);
	}
}
