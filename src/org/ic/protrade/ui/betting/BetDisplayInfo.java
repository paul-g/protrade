package org.ic.protrade.ui.betting;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.protrade.data.match.Player;
import org.ic.protrade.domain.Bet;

public class BetDisplayInfo {
	private Composite parent;
	private Label firstPlayerWinnerSummary;
	private Label secondPlayerWinnerSummary;
	private HashMap<Bet, Label> betLabels; 
	private String firstPlayerWinnerText;
	private String secondPlayerWinnerText;
	private double firstPlayerWinnerProfit;
	private double secondPlayerWinnerProfit;
	
	public BetDisplayInfo(Composite composite, Player firstPlayer, Player secondPlayer) {
		this.parent = composite;
		this.betLabels = new HashMap<Bet, Label>();
		initPlayerWinnerSummaryLabels(firstPlayer.toString(), secondPlayer.toString());		
	}

	private void initPlayerWinnerSummaryLabels(String firstPlayerName, String secondPlayerName) {
		firstPlayerWinnerSummary = new Label(parent, SWT.NONE);
		firstPlayerWinnerText = "If " + firstPlayerName + " wins your profit is ";
		firstPlayerWinnerProfit = 0;
		secondPlayerWinnerSummary = new Label(parent, SWT.NONE);
		secondPlayerWinnerText = "If " + secondPlayerName + " wins your profit is ";
		secondPlayerWinnerProfit = 0;
		setPlayerLabels();
	}
	
	private void setPlayerLabels() {
		setFirstPlayerLabel();
		setSecondPlayerLabel();
		parent.layout();
	}

	private void setFirstPlayerLabel() {
		firstPlayerWinnerSummary.setText(firstPlayerWinnerText
				+ BetsDisplay.DOUBLE_FORMAT.format(firstPlayerWinnerProfit));
	}
	
	private void setSecondPlayerLabel() {
		secondPlayerWinnerSummary.setText(secondPlayerWinnerText
				+ BetsDisplay.DOUBLE_FORMAT.format(secondPlayerWinnerProfit));
	}
	
	public void setPlayerWinnerProfits(double firstPlayerWinnerProfit, double secondPlayerWinnerProfit) {
		setFirstPlayerWinnerProfit(firstPlayerWinnerProfit);
		setSecondPlayerWinnerProfit(secondPlayerWinnerProfit);
		setPlayerLabels();
	}

	public double getFirstPlayerWinnerProfit() {
		return firstPlayerWinnerProfit;
	}

	private void setFirstPlayerWinnerProfit(double firstPlayerWinnerProfit) {
		this.firstPlayerWinnerProfit = firstPlayerWinnerProfit;
	}

	public double getSecondPlayerWinnerProfit() {
		return secondPlayerWinnerProfit;
	}

	private void setSecondPlayerWinnerProfit(double secondPlayerWinnerProfit) {
		this.secondPlayerWinnerProfit = secondPlayerWinnerProfit;
	}

	public HashMap<Bet, Label> getBets() {
		return betLabels;
	}
	
	public void addBet(Bet bet, Label betLabel) {
		Label newBet = new Label(parent, SWT.NONE);
		newBet.setText(betLabel.getText());
		this.betLabels.put(bet, newBet);
		parent.layout();
	}

    public void updateBetLabel(Bet bet, Label betLabel) {
        if (betLabels.containsKey(bet)) {
            Label oldLabel = betLabels.get(bet);
            oldLabel.setText(betLabel.getText());
            parent.layout();
        }        
    }
}
