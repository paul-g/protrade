package org.ic.tennistrader.ui.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class BetsTable {
	private Shell shell;
	private Table table;
	private DashboardToolBar dtb;
	
	public BetsTable(int x,int y, DashboardToolBar dtb) {
		shell = new Shell();
		shell.setText("Bets Table");
		shell.setBounds(x, y, 410, 200);
		shell.setLayout(new FillLayout());
		table = new Table(shell, SWT.NONE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn[] column = new TableColumn[7];
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
		column[5].setText("Profit");
		column[6] = new TableColumn(table, SWT.CENTER);
		column[6].setText("Liability");
		TableItem item = new TableItem(table, SWT.NONE);
		int n = column.length;
		for (int i = 0; i < n; i++) {
			item.setText(i, "");
			column[i].pack();
		}
		shell.open();
		dtb.setBetsTable(null);
	}
	
	public void setFocus() {
		shell.forceActive();
	}
}
