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
    
    private PredictionUpdateThread predictionUpdateThread;
        
    /**
     * For running the prediction gui separately
     */
    public static void main(String args[]) {
        final Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());

        Player playerOne = new Player("Roger", "Federer");
        Player playerTwo = new Player("Jo-Wilfried", "Tsonga");

        Match match = new RealMatch("","", new EventBetfair("Federer v Tsonga", new ArrayList<EventMarketBetfair>(), 1));

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
        ProbabilityPanel probabilityPanel = new ProbabilityPanel(this, match);
        StatisticsPanel st = new StatisticsPanel(parent, match);
        
    	
        this.statisticsUpdateThread = new StatisticsUpdateThread(match, st);

        this.scoreUpdateThread = new ScoreUpdateThread(match, sc);
        
        this.predictionUpdateThread = new PredictionUpdateThread(match, probabilityPanel);

        parent.getDisplay().timerExec(5000, new Runnable() {
            @Override
            public void run() {
            	statisticsUpdateThread.checkStatisticsUpdate();
                if (!statisticsUpdateThread.isStatisticsPopulated()){
                    if (!parent.isDisposed())
                        parent.getDisplay().timerExec(5000, this);
                }
                else 
                {
                	
                }
            }
        });

        if (match.isInPlay()) {
            // only start score fetching for live matches
            parent.getDisplay().timerExec(5000, new Runnable() {

                @Override
                public void run() {
                    scoreUpdateThread.handleUpdate();
                    if (!parent.isDisposed())
                        parent.getDisplay().timerExec(5000, this);
                }
            });

            scoreUpdateThread.start();
        }
        statisticsUpdateThread.start();
        
        
        
        parent.getDisplay().timerExec(5000, new Runnable() {
        	
        	private volatile boolean stop = false;
        	//private 
        	
            @Override
            public void run() {
            	
                if (!stop && !statisticsUpdateThread.isStatisticsPopulated()){
                    if (!parent.isDisposed())
                        parent.getDisplay().timerExec(5000, this);
                }
                Score score = scoreUpdateThread.getScore();
                PlayerEnum server = PlayerEnum.PLAYER1;
                Statistics playerOneStats = statisticsUpdateThread.getPlayerOneStats();
                Statistics playerTwoStats = statisticsUpdateThread.getPlayerTwoStats();
                
                PredictionCalculator predict = new PredictionCalculator(score, playerOneStats, playerTwoStats, server);
                predict.calculate();
                predictionUpdateThread.updateTable(predict);
                System.out.println(playerOneStats.getFirstServePercent());
                System.out.println(playerTwoStats.getFirstServePercent());
                System.out.println("CALCULATING ONCE");
                requestStop();
             
            }
            
            public void requestStop() {
                stop = true;
              }
        });
        
       /* while (!statisticsUpdateThread.isStatisticsPopulated()){
        	try{
        	synchronized (statisticsUpdateThread) {
        		statisticsUpdateThread.wait(500);
            }
        	}catch(Exception e){}
        }*/
    	
    }

    @Override
    public String getTitle() {
        return "Prediction";
    }   
}