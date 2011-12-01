package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.domain.BetDisplayInfo;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class BetsDisplay extends StandardTabbedWidgetContainer{    
	private static BetsDisplay betsDisplay;
    private static List<Label> activeBets = new ArrayList<Label>();
    private static Composite composite = null;
    private static HashMap<Match, BetDisplayInfo> matchBets = new HashMap<Match, BetDisplayInfo>();
    private static HashMap<Bet, Label> unmatchedBetsLabels = new HashMap<Bet, Label>();
    
    public BetsDisplay(Composite parent, int style){
        super(parent, style);
        CTabItem cti = new CTabItem(folder, SWT.CLOSE);
        cti.setText("Active Bets");
        folder.setSimple(false);
        folder.setMinimizeVisible(true);
        folder.setMaximizeVisible(true);

        Composite comp = new StandardWidgetContainer(folder, SWT.NONE);
        BetsDisplay.composite = comp;
        RowLayout rl = new RowLayout();
        rl.type = SWT.VERTICAL;
        comp.setLayout(rl);
        cti.setControl(comp);
        folder.setSelection(cti);
        betsDisplay = this;
        
        parent.layout();
    }

    public static void addBet(Bet bet) {
        Label betLabel = new Label(composite, SWT.NONE);
        setBetLabelText(bet, betLabel);
        if (bet.getUnmatchedValue() > 0)
            unmatchedBetsLabels.put(bet, betLabel);
        activeBets.add(betLabel);
        updateMatchTab(bet, betLabel);
        composite.layout();
    }

	private static void updateMatchTab(Bet bet, Label betLabel) {
		betsDisplay.updateTab(bet, betLabel);
	}

	private void updateTab(Bet bet, Label betLabel) {
		if (matchBets.containsKey(bet.getMatch())) {
			BetDisplayInfo betDisplayInfo = matchBets.get(bet.getMatch());
			addBetDisplay(bet, betLabel, betDisplayInfo);
			folder.setSelection(getTabPosition(bet.getMatch().getName()));
		} else {
			CTabItem tabItem = addTab(bet.getMatch().getName());
			Composite control = new Composite(folder, SWT.NONE);
			control.setLayout(new FillLayout());
			RowLayout rl = new RowLayout();
			rl.type = SWT.VERTICAL;
			control.setLayout(rl);
			BetDisplayInfo betDisplayInfo = new BetDisplayInfo(control, bet
					.getMatch().getPlayerOne(), bet.getMatch().getPlayerTwo());
			addBetDisplay(bet, betLabel, betDisplayInfo);
			matchBets.put(bet.getMatch(), betDisplayInfo);
			tabItem.setControl(control);
			folder.setSelection(tabItem);
		}
	}

	private void addBetDisplay(Bet bet, Label betLabel,
			BetDisplayInfo betDisplayInfo) {
		betDisplayInfo.setPlayerWinnerProfits(betDisplayInfo
				.getFirstPlayerWinnerProfit()
				+ bet.getFirstPlayerWinnerProfit(), betDisplayInfo
				.getSecondPlayerWinnerProfit()
				+ bet.getSecondPlayerWinnerProfit());
		betDisplayInfo.addBet(bet, betLabel);
	}

	public static void addSettledBet(final Bet bet) {
		composite.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				Label betLabel = new Label(composite, SWT.NONE);
				betLabel.setText((bet.getType() == BetTypeEnum.B ? "Back " : "Lay ")
						+ bet.getPlayer().toString() + " for " + bet.getAmount()
						+ "£ at " + bet.getOdds() + " was " + ((bet.getProfit() > 0) ? "" : "not ")
						+ " successful and your " + (bet.getProfit() > 0 ? "profit " : "loss ")
						+ " is: " + (bet.getProfit() > 0 ? bet.getProfit() : ((-1) * bet.getProfit()) )
						+ "");
				activeBets.add(betLabel);
				BetDisplayInfo betDisplayInfo = matchBets.get(bet.getMatch());
				betDisplayInfo.addBet(bet, betLabel);
				composite.layout();
			}			
		});		
	}

    public static void updateUnmatchedBet(final Bet bet) {
        if (unmatchedBetsLabels.containsKey(bet)) {
            final Label betLabel = unmatchedBetsLabels.get(bet);
            composite.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    setBetLabelText(bet, betLabel);
                    betsDisplay.updateTabLabel(bet, betLabel);
                    composite.layout();
                }
            });
            if (bet.getUnmatchedValue() == 0)
                unmatchedBetsLabels.remove(bet);
        }
    }

    protected void updateTabLabel(Bet bet, Label betLabel) {
        if (matchBets.containsKey(bet.getMatch())) {
            BetDisplayInfo betDisplayInfo = matchBets.get(bet.getMatch());
            betDisplayInfo.updateBetLabel(bet, betLabel);
        } 
    }

    private static void setBetLabelText(Bet bet, Label betLabel) {
        String betLabelText = bet.getDescription(); 
        if (bet.getUnmatchedValue() > 0) {            
            betLabelText += ". Yet unmatched value: " + bet.getUnmatchedValue() + "";
        }
        betLabel.setText(betLabelText);
    }
}
