package org.ic.protrade.ui.betting;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.betting.BetManager;
import org.ic.protrade.ui.updatable.OddsButton;
import org.ic.protrade.utils.Colours;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

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
	private final double initialOdds;
	private Label firstPlayerWinnerSummary;
	private Label secondPlayerWinnerSummary;
	private String firstPlayerWinnerText;
	private String secondPlayerWinnerText;
	private Label betProfit;
	private Label pl1PandL;
	private Label pl2PandL;
	
	
	private final Match match;
	private final PlayerEnum betPlayer;
	private final BetTypeEnum betType;
	private final String betDetails;
	private Color color;
	private OddsButton button;

	public BetPlacingShell(OddsButton button, Match match,
			PlayerEnum betPlayer, BetTypeEnum betType, double initialOdds,
			String betDetails) {
		super(button.getShell());
		this.button = button;
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
		setGridLayout();
		GridData infoGridData = getInfoGridData();

		Font f1 = new Font(Display.getCurrent(), "Times New Roman", 10,
				SWT.NONE);
		Font f2 = new Font(Display.getCurrent(), "Times New Roman", 12,
				SWT.BOLD);
		createInfoLabel(betType, betDetails, infoGridData, color, f1);
		createInfoRow(betType, match, betPlayer, initialOdds, infoGridData,
				color, f2);
		Label lastRow = new Label(betShell, SWT.NONE);
		lastRow.setText(betDetails);
		//lastRow.setBackground(color);
		lastRow.setLayoutData(getGridData10());

		addTextFieldsListeners(match, betType, betPlayer);

		createErrorLabel(getGridData10());
		addPossibleLabelRow(infoGridData, f2);
		addPossibleOutcome1(infoGridData, f2);
		addPossibleOutcome2(infoGridData, f2);
		updateOverallPossibleProfits(match, betType, betPlayer);
		//
		// createProfitAndLiabilityLabels(betType, infoGridData);
		// createWinnerProfitLabels(match, betType, betPlayer, infoGridData);
		//
		this.getShell().setBackground(color);
		return super.createDialogArea(parent);
	}

	private void addPossibleOutcome2(GridData infoGridData, Font f2) {
		secondPlayerWinnerSummary = new Label(betShell, SWT.NONE);
		secondPlayerWinnerSummary.setLayoutData(infoGridData);
		secondPlayerWinnerText = "If " + match.getPlayerTwo()
				+ " wins: ";
		secondPlayerWinnerSummary.setText(secondPlayerWinnerText);
		pl2PandL = new Label(betShell, SWT.CENTER);
		pl2PandL.setLayoutData(getFirstGridData());
	}

	private void addPossibleOutcome1(GridData infoGridData, Font f2) {
		firstPlayerWinnerSummary = new Label(betShell, SWT.NONE);
		firstPlayerWinnerSummary.setLayoutData(infoGridData);
		firstPlayerWinnerText = "If " + match.getPlayerOne()
				+ " wins: ";
		firstPlayerWinnerSummary.setText(firstPlayerWinnerText);
		pl1PandL = new Label(betShell, SWT.CENTER);
		pl1PandL.setLayoutData(getFirstGridData());


	}

	private void addPossibleLabelRow(GridData infoGridData, Font f1) {
		Label possOut = new Label(betShell, SWT.NONE);
		possOut.setText("Possible Outcome");
		possOut.setFont(f1);
		possOut.setLayoutData(infoGridData);
		Label possPandL = new Label(betShell, SWT.CENTER);
		possPandL.setText("Potential P&&L");
		possPandL.setFont(f1);
		possPandL.setLayoutData(getFirstGridData());

	}

	private void createInfoRow(BetTypeEnum betType, Match match,
			PlayerEnum betPlayer, double initialOdds, GridData infoGridData,
			Color color, Font f2) {
		Label name = new Label(betShell, SWT.NONE);
		name.setText(match.getPlayer(betPlayer).getFirstname() + " "
				+ match.getPlayer(betPlayer).getLastname());
		//name.setBackground(color);
		name.setFont(f2);
		name.setLayoutData(infoGridData);
		oddsText = new Text(betShell, SWT.NONE);
		oddsText.setText(initialOdds + "");
		oddsText.setFont(f2);
		oddsText.setBackground(color);
		amountText = new Text(betShell, SWT.NONE);
		amountText.setText(10 + "");
		amountText.setFont(f2);
		// amountText.setBackground(color);
		betProfit = new Label(betShell, SWT.NONE);
		betProfit.setText((initialOdds * 100 - 100) + "");
		//betProfit.setBackground(color);
		betProfit.setFont(f2);
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
			this.button.getDataGrid().updateProfits();
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

		pl1PandL.setText(BetsDisplay.DOUBLE_FORMAT.format(firstPlayerWinnerProfit));
		pl2PandL.setText(BetsDisplay.DOUBLE_FORMAT.format(secondPlayerWinnerProfit));
		Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		Color green = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
		if (firstPlayerWinnerProfit >= 0 ) pl1PandL.setForeground(green); 
		else pl1PandL.setForeground(red);
		if (secondPlayerWinnerProfit >= 0) pl2PandL.setForeground(green);
		else pl2PandL.setForeground(red);
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
				// updateProfitAndLiability(betType);
				// updateOverallPossibleProfits(match, betType, betPlayer);
				try {
					double amount = Double.parseDouble(amountText.getText());
					double odds = Double.parseDouble(oddsText.getText());
					betProfit.setText((amount * (odds - 1)) + "");
					errorLabel.setVisible(false);
					updateOverallPossibleProfits(match, betType, betPlayer);
				} catch (NumberFormatException nfe) {
					setErrorText(AMOUNT_NUMBER_EXCEPTION);
					return;
				}
			}
		});
		oddsText.addListener(SWT.CHANGED, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				// updateProfitAndLiability(betType);
				// updateOverallPossibleProfits(match, betType, betPlayer);
				try {
					double amount = Double.parseDouble(amountText.getText());
					double odds = Double.parseDouble(oddsText.getText());
					betProfit.setText((amount * (odds - 1)) + "");
					if (BetManager.isValidPrice(odds)) {
						errorLabel.setVisible(false);
						updateOverallPossibleProfits(match, betType, betPlayer);
					}
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
			GridData infoGridData, Color c, Font f1) {
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
		infoLabel.setFont(f1);
		odds.setFont(f1);
		stake.setFont(f1);
		profit.setFont(f1);
		//infoLabel.setBackground(c);
		//odds.setBackground(c);
		//stake.setBackground(c);
		//profit.setBackground(c);

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
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 7;
		gridData.grabExcessHorizontalSpace = true;
		return gridData;
	}

	private GridData getGridData10() {
		GridData gridData = new GridData();
		gridData.horizontalSpan = 10;
		gridData.grabExcessHorizontalSpace = true;
		return gridData;
	}

	private GridData getSecondGridData() {
		GridData gridData2 = new GridData(150, 20);
		gridData2.horizontalAlignment = GridData.FILL_HORIZONTAL;
		gridData2.horizontalSpan = 4;
		return gridData2;
	}

	private GridData getFirstGridData() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		return gridData;
	}

	private void setGridLayout() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 10;
		//gridLayout.makeColumnsEqualWidth = true;
		betShell.setLayout(gridLayout);
	}

	public void setLocation(int x, int y) {
		betShell.setLocation(x, y);
	}

	public Label getErrorLabel() {
		return errorLabel;
	}
}
