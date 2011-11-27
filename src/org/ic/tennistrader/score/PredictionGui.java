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
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.domain.match.Statistics;

public class PredictionGui extends StandardWidgetContainer{

    private static Logger log = Logger.getLogger(Main.class);

    private TableColumn[] columns;

    private ScoreUpdateThread scoreUpdateThread;

    private StatisticsUpdateThread statisticsUpdateThread;
        
    /**
     * For running the prediction gui separately
     */
    public static void main(String args[]) {
        final Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());

        Player playerOne = new Player("Roger", "Federer");
        Player playerTwo = new Player("Jo-Wilfried", "Tsonga");

        Match match = new RealMatch("","", new EventBetfair("Sousa v Souza", new ArrayList<EventMarketBetfair>(), 1));
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

        ScorePanel sc = new ScorePanel(this, match);
        
        StatisticsPanel st = new StatisticsPanel(parent, match);
    	
        this.statisticsUpdateThread = new StatisticsUpdateThread(match, st);

        this.scoreUpdateThread = new ScoreUpdateThread(match);

        parent.getDisplay().timerExec(5000, new Runnable() {
            @Override
            public void run() {
            	statisticsUpdateThread.checkStatisticsUpdate();
                if (!statisticsUpdateThread.isStatisticsPopulated())
                    if (!parent.isDisposed())
                        parent.getDisplay().timerExec(5000, this);
            }
        });

       if (match.isInPlay()) {
            // only start score fetching for live matches
             scoreUpdateThread.start();
        }
       
        statisticsUpdateThread.start();
        
        Score score = scoreUpdateThread.getScore();
        Statistics playerOneStats = statisticsUpdateThread.getPlayerOneStats();
        Statistics playerTwoStats = statisticsUpdateThread.getPlayerOneStats();
    	PlayerEnum server = sc.getServer();	
        
        //PredictionCalculator predict = new PredictionCalculator(score, playerOneStats, playerTwoStats, server);
        //predict.calculate();
        ProbabilityPanel probabilityPanel = new ProbabilityPanel(this);
    }

    @Override
    public String getTitle() {
        return "Prediction";
    }   
}