package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class BetsDisplay extends StandardTabbedWidgetContainer{
    
    private static List<Label> activeBets = new ArrayList<Label>();
    private static Composite composite = null;
    
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
        parent.layout();
    }
    
	public static void addBet(Bet bet) {
		Label betLabel = new Label(composite, SWT.NONE);
		betLabel.setText((bet.getType() == BetTypeEnum.B ? "Back " : "Lay ")
				+ bet.getPlayer().toString() + " for " + bet.getAmount()
				+ "£ at " + bet.getOdds() + "");
		activeBets.add(betLabel);
		composite.layout();
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
				composite.layout();
			}			
		});		
	}
}
