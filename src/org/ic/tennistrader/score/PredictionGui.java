package org.ic.tennistrader.score;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.ic.tennistrader.Main;
import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.EventMarketBetfair;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.ui.StandardWidgetContainer;

public class PredictionGui extends StandardWidgetContainer {

    private static Logger log = Logger.getLogger(Main.class);

    private TableColumn[] columns;

    private ScoreUpdateThread scoreUpdateThread;

    private StatisticsUpdateThread statisticsUpdateThread;
    
    public static void main(String args[]) {
        final Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());

        Player playerOne = new Player("Jo-Wilfried", "Tsonga");
        Player playerTwo = new Player("Roger", "Federer");

        Match match = new RealMatch("","", new EventBetfair("Federer v Tsonga", new ArrayList<EventMarketBetfair>(), 1));
        match.setPlayer1(playerOne);
        match.setPlayer2(playerTwo);

        new PredictionGui(shell, SWT.BORDER, match);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        
        display.dispose();
    }
    
    public PredictionGui(final Composite parent, int style, Match match) {
        super(parent, style);

        this.setLayout(new GridLayout());

        ProbabilityPanel probabilityPanel = new ProbabilityPanel(this, match);

        ScorePanel sc = new ScorePanel(this, match);

        StatisticsPanel st = new StatisticsPanel(this, match);
        this.statisticsUpdateThread = new StatisticsUpdateThread(match,st);
        this.statisticsUpdateThread.addListener(st);

        this.scoreUpdateThread = new ScoreUpdateThread(match);

        if (match.isInPlay()) {
            // only start score fetching for live matches
            scoreUpdateThread.start();
        }

        statisticsUpdateThread.start();
    }

    @Override
    public String getTitle() {
        return "Prediction";
    }
}