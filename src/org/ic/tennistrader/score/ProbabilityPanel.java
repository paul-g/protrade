package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public class ProbabilityPanel extends StandardWidgetContainer implements
        UpdatableWidget {

    private Table table;
    private Match match;
    private final Display display;

    public ProbabilityPanel(Composite parent, Match match) {
        super(parent, SWT.NONE);
        this.match = match;
        this.display = parent.getDisplay();

        this.setLayout(new GridLayout());

        table = new Table(this, SWT.NONE);
        // table.setLayout(new FillLayout());
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn[] column = new TableColumn[5];
        column[0] = new TableColumn(table, SWT.NONE);
        column[0].setText("Prob/win:");
        column[0].setResizable(false);

        column[1] = new TableColumn(table, SWT.NONE);
        column[1].setText("Point   ");
        column[1].setResizable(false);

        column[2] = new TableColumn(table, SWT.NONE);
        column[2].setText("Game   ");
        column[2].setResizable(false);

        column[3] = new TableColumn(table, SWT.NONE);
        column[3].setText("Set       ");
        column[3].setResizable(false);

        column[4] = new TableColumn(table, SWT.NONE);
        column[4].setText("Match");
        column[4].setResizable(false);

        // Filling the probabilities table with data
        table.setRedraw(false);

        TableItem item = new TableItem(table, SWT.NONE);
        int c = 0;
        item.setText(c++, match.getPlayerOne().getLastname());
        item.setText(c++, "-");
        item.setText(c++, "-");
        item.setText(c++, "-");
        item.setText(c++, "-");

        TableItem item2 = new TableItem(table, SWT.NONE);
        c = 0;
        item2.setText(c++, match.getPlayerTwo().getLastname());
        item2.setText(c++, "-");
        item2.setText(c++, "-");
        item2.setText(c++, "-");
        item2.setText(c++, "-");

        table.setRedraw(true);

        for (int i = 0, n = column.length; i < n; i++) {
            column[i].pack();
        }

        table.getParent().layout();

        DataManager.registerForMatchUpdate(this, match);

    }

    public void updateTable() {

        double[] result = PredictionCalculator.calculate(this.match);
        // double[] result = {0,0,0,0,0};

        Table table = this.table;
        // Filling the probabilities table with data
        table.setRedraw(false);

        TableItem item = table.getItem(0);
        int c = 0;
        item.setText(c++, match.getPlayerOne().getLastname());
        item.setText(c++, Double.toString(result[0]));
        item.setText(c++, Double.toString(result[2]));
        item.setText(c++, Double.toString(result[4]));
        item.setText(c++, Double.toString(result[6]));

        TableItem item2 = table.getItem(1);
        c = 0;
        item2.setText(c++, match.getPlayerTwo().getLastname());
        item2.setText(c++, Double.toString(result[1]));
        item2.setText(c++, Double.toString(result[3]));
        item2.setText(c++, Double.toString(result[5]));
        item2.setText(c++, Double.toString(result[7]));

        table.setRedraw(true);

    }

    @Override
    public void handleUpdate(MOddsMarketData newData) {
        if (match.getPlayerOne().getStatistics() != null)
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    updateTable();
                }
            });
    }

    @Override
    public void setDisposeListener(DisposeListener listener) {
        this.addDisposeListener(listener);
    }
}
