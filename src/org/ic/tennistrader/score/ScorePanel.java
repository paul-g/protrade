package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public class ScorePanel implements UpdatableWidget {

    private Match match;

    private Table scoreTable;

    private TableColumn[] columns;

    private Display display;

    public ScorePanel(Composite composite, Match match) {
        this.match = match;

        this.display = composite.getDisplay();

        this.scoreTable = new Table(composite, SWT.NONE);
        scoreTable.setHeaderVisible(true);

        scoreTable.setLinesVisible(true);
        this.columns = new TableColumn[8];
        columns[0] = new TableColumn(scoreTable, SWT.NONE);
        columns[0].setText("Serving");
        columns[1] = new TableColumn(scoreTable, SWT.NONE);
        columns[1].setText("Player");

        int i = 0;
        for (; i < match.getScore().getMaximumSetsPlayed(); i++) {
            columns[i + 2] = new TableColumn(scoreTable, SWT.NONE);
            columns[i + 2].setText("Set " + (i + 1));
        }

        columns[i + 2] = new TableColumn(scoreTable, SWT.NONE);
        columns[i + 2].setText("Points");

        int c = 1;
        TableItem ti = new TableItem(scoreTable, SWT.NONE);
        ti.setText(c++, match.getPlayerOne().toString());

        c = 1;
        TableItem ti2 = new TableItem(scoreTable, SWT.NONE);
        ti2.setText(c++, match.getPlayerTwo().toString());

        setScores();

        scoreTable.setRedraw(true);

        for (int j = 0; j < i + 3; j++) {
            columns[j].pack();
        }

        scoreTable.redraw();
        scoreTable.getParent().layout();

        match.registerForUpdate(this);
    }

    public void setScores() {
        Score score = match.getScore();

        int playerOneScores[] = score.getPlayerOneScore();
        TableItem ti = scoreTable.getItem(0);
        int c = 2;
        for (int s : playerOneScores) {
            ti.setText(c++, s + "");
        }

        ti.setText(c, score.getPlayerOnePoints() + "");

        int playerTwoScores[] = score.getPlayerTwoScore();
        TableItem ti2 = scoreTable.getItem(1);
        c = 2;
        for (int s : playerTwoScores) {
            ti2.setText(c++, s + "");
        }

        ti2.setText(c, score.getPlayerTwoPoints() + "");
    }

    @Override
    public void handleUpdate(MOddsMarketData newData) {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                setScores();
            }
        });
    }

    @Override
    public void setDisposeListener(Listener listener) {
        // TODO Auto-generated method stub
    }
}