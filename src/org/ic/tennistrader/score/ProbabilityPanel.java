package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ProbabilityPanel {

    public ProbabilityPanel(Composite composite) {

        final Table table = new Table(composite, SWT.NONE);
        table.setBounds(new Rectangle(10, 110, 370, 90));
        table.setHeaderVisible(true);
        // table.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        table.setLinesVisible(true);
        TableColumn[] column = new TableColumn[5];

        column[0] = new TableColumn(table, SWT.NONE);
        column[0].setText("Probability of winning:");

        column[1] = new TableColumn(table, SWT.NONE);
        column[1].setText("Point");

        column[2] = new TableColumn(table, SWT.NONE);
        column[2].setText("Game");

        column[3] = new TableColumn(table, SWT.NONE);
        column[3].setText("Set");

        column[4] = new TableColumn(table, SWT.NONE);
        column[4].setText("Match");

        // Filling the probabilities table with data
        table.setRedraw(false);

        TableItem item = new TableItem(table, SWT.NONE);
        int c = 0;
        item.setText(c++, "Player 1");
        item.setText(c++, "0");//+(predict.calculate()[0]));
        item.setText(c++, "78%");
        item.setText(c++, "57%");
        item.setText(c++, "63%");

        TableItem item2 = new TableItem(table, SWT.NONE);
        c = 0;
        item2.setText(c++, "Player 2");
        item2.setText(c++, "38%");
        item2.setText(c++, "22%");
        item2.setText(c++, "43%");
        item2.setText(c++, "37%");

        table.setRedraw(true);

        for (int i = 0, n = column.length; i < n; i++) {
            column[i].pack();
        }
    }
}
