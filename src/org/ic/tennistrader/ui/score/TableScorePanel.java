package org.ic.tennistrader.ui.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.StandardWidgetContainer;

public class TableScorePanel extends StandardWidgetContainer implements
        ScorePanel {
    private Match match;
    private Table scoreTable;
    private PlayerEnum server;
    private TableColumn[] columns;
    private Display display;

    public TableScorePanel(Composite parent, Match match) {
        super(parent, SWT.NONE);
        this.match = match;
        this.display = parent.getDisplay();

        this.setLayout(new FillLayout());

        this.scoreTable = new Table(this, SWT.NONE);
        //scoreTable.setHeaderVisible(true);
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
        ti.setText(c++, match.getPlayerOne().getLastname());

        c = 1;
        TableItem ti2 = new TableItem(scoreTable, SWT.NONE);
        ti2.setText(c++, match.getPlayerTwo().getLastname());

        setScores();

        scoreTable.setRedraw(true);

        for (int j = 0; j < i + 3; j++) {
            columns[j].pack();
        }

        scoreTable.redraw();
        scoreTable.getParent().layout();

        DataManager.registerForMatchUpdate(this, match);
    }

    /* (non-Javadoc)
     * @see org.ic.tennistrader.score.ScorePanel#setScores()
     */
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

    /* (non-Javadoc)
     * @see org.ic.tennistrader.score.ScorePanel#setServer(org.ic.tennistrader.domain.match.PlayerEnum)
     */
    public void setServer(PlayerEnum player) {
        if (player == PlayerEnum.PLAYER1)
            scoreTable.getItem(0).setText(0, "S");
        else
            scoreTable.getItem(1).setText(0, "S");

        server = player;
    }

    /* (non-Javadoc)
     * @see org.ic.tennistrader.score.ScorePanel#getServer()
     */
    public PlayerEnum getServer() {
        return this.server;
    }

    /* (non-Javadoc)
     * @see org.ic.tennistrader.score.ScorePanel#handleUpdate(org.ic.tennistrader.domain.MOddsMarketData)
     */
    @Override
    public void handleUpdate(MOddsMarketData newData) {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                setScores();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.ic.tennistrader.score.ScorePanel#setDisposeListener(org.eclipse.swt.events.DisposeListener)
     */
    @Override
    public void setDisposeListener(DisposeListener listener) {
        this.addDisposeListener(listener);
    }

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub
		
	}
}