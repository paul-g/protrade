package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Score;

public class ScorePanel {

    private Match match;

    private Table scoreTable;

    private TableColumn[] columns;

    public ScorePanel(Composite composite, Match match) {
        this.match = match;

        this.scoreTable = new Table(composite, SWT.NONE);
        // scoreTable.setBounds(new Rectangle(10, 10, 270, 90));
        scoreTable.setHeaderVisible(true);

        // table.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
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

        setScores();

        scoreTable.setRedraw(true);

        for (int j = 0; j < i + 3; j++) {
            columns[j].pack();
        }

        scoreTable.redraw();
        scoreTable.getParent().layout();
    }

    public void setScores() {
        Score score = match.getScore();

        int playerOneScores[] = score.getPlayerOneScore();

        TableItem ti = new TableItem(scoreTable, SWT.NONE);
        int c = 1;
        ti.setText(c++, match.getPlayerOne().toString());
        for (int s : playerOneScores) {
            ti.setText(c++, s + "");
        }

        ti.setText(c, score.getPlayerOnePoints() + "");

        int playerTwoScores[] = score.getPlayerTwoScore();

        TableItem ti2 = new TableItem(scoreTable, SWT.NONE);
        c = 1;
        ti2.setText(c++, match.getPlayerTwo().toString());
        for (int s : playerTwoScores) {
            ti2.setText(c++, s + "");
        }

        ti2.setText(c, score.getPlayerTwoPoints() + "");
    }
}
