package org.ic.protrade.ui.toolbars;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.protrade.domain.Bet;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.model.betting.BetManager;

public class BetsTable {
	private Shell shell;
	private Table table;
	private TableColumn[] column = new TableColumn[9];
	
	public BetsTable(int x,int y, final DashboardToolBar dtb) {
		shell = new Shell();
		shell.setText("Bets Table");
		shell.setBounds(x, y, 600, 200);
		shell.setLayout(new FillLayout());
		table = new Table(shell, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		column[0] = new TableColumn(table, SWT.CENTER);
		column[0].setText("Match");
		column[1] = new TableColumn(table, SWT.CENTER);
		column[1].setText("Player");
		column[2] = new TableColumn(table, SWT.CENTER);
		column[2].setText("Bet Type");
		column[3] = new TableColumn(table, SWT.CENTER);
		column[3].setText("Odds");
		column[4] = new TableColumn(table, SWT.CENTER);
		column[4].setText("Amount");
		column[5] = new TableColumn(table, SWT.CENTER);
		column[5].setText("Unmatched");
		column[6] = new TableColumn(table, SWT.CENTER);
		column[6].setText("Possible Payoff");
		column[7] = new TableColumn(table, SWT.CENTER);
		column[7].setText("Liability");
		column[8] = new TableColumn(table, SWT.CENTER);
		column[8].setText("Successful");
		BetManager.setBetsTable(this);
		refresh();
		shell.open();
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dtb.setBetsTable(null);
				BetManager.setBetsTable(null);
			}
		});
	}
	
	public void setFocus() {
		shell.forceActive();
	}
	
	public void refresh() {
		shell.getDisplay().asyncExec(new Runnable(){
			@Override
			public void run() {
				table.removeAll();
				for (Bet b : BetManager.getMatchedBetTab()) {
					add(b);
				}
				for (Bet b : BetManager.getUnmatchedBetTab()) {
					add(b);
				}
				for (int i = 0; i < column.length; i++) column[i].pack();
				shell.redraw();
				shell.pack();
				shell.open();
			} 
		});
	}
	
	public void add( Bet bet ) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, bet.getMatch().getName());
		item.setText(1, bet.getPlayer().equals(PlayerEnum.PLAYER1) ? bet
				.getMatch().getPlayerOne().toString() : bet.getMatch()
				.getPlayerTwo().toString());
		item.setText(2, bet.getType().toString());
		item.setText(3, bet.getOdds() + "");
		item.setText(4, bet.getAmount() + "");
		item.setText(5, toS(bet.getUnmatchedValue()));
		item.setText(6, toS(bet.getPossibleProfit()));
		item.setText(7, toS(bet.getPossibleLiability()));
		if (bet.getProfit() == 0)
			item.setText(8, "N/A");
		else if (bet.getProfit() > 0)
			item.setText(8, "Yes");
		else
			item.setText(8, "No");
	}
	
	private String toS (double d) {
		return Double.valueOf(new DecimalFormat("#.##").format(d))+"";
	}
}
