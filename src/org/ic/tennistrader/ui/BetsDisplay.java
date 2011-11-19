package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.domain.Bet;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class BetsDisplay {
    
    private static List<Label> activeBets = new ArrayList<Label>();
    private static Composite composite = null;
    
    public BetsDisplay(Composite composite){
        CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
        CTabItem cti = new CTabItem(tabFolder, SWT.CLOSE);
        cti.setText("Active Bets");
        tabFolder.setSimple(false);
        tabFolder.setMinimizeVisible(true);
        tabFolder.setMaximizeVisible(true);

        Composite comp = new Composite(tabFolder, SWT.NONE);
        BetsDisplay.composite = comp;
        RowLayout rl = new RowLayout();
        rl.type = SWT.VERTICAL;
        comp.setLayout(rl);
        cti.setControl(comp);
        tabFolder.setSelection(cti);
        composite.layout();
    }
    
	public static void addBet(Bet bet) {
		Label betLabel = new Label(composite, SWT.NONE);
		betLabel.setText((bet.getType() == BetTypeEnum.B ? "Back " : "Lay ")
				+ bet.getPlayer().toString() + " for " + bet.getValue().getJ()
				+ "£ at " + bet.getValue().getI() + "");
		activeBets.add(betLabel);
		composite.layout();
	}

}
