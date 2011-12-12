package org.ic.tennistrader.score;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.controller.BetController;
import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.EventMarketBetfair;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.ui.score.WimbledonScorePanel;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;

public class PredictionGui extends StandardWidgetContainer {

    private static Logger log = Logger.getLogger(PredictionGui.class);

    private ScoreUpdateThread scoreUpdateThread;

    private StatisticsUpdateThread statisticsUpdateThread;
    
    private Match match;

    private StatisticsPanel st;
    
    public static void main(String args[]) {
        final Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());

        Player playerOne = new Player("Rafael", "Nadal");
        Player playerTwo = new Player("Del Potro", "Juan Martin");

        Match match = new RealMatch("","", new EventBetfair("Nadal v Del Potro", new ArrayList<EventMarketBetfair>(), 1));
        MOddsMarketData modds =new MOddsMarketData();
        modds.setDelay(5);
        match.addMarketData(modds);
        match.setPlayer1(playerOne);
        match.setPlayer2(playerTwo);

        new PredictionGui(shell, SWT.BORDER, match).start();

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        
        display.dispose();
    }
    
    public PredictionGui(final Composite parent, int style, Match match) {
        super(parent, style);
     log.info("Started prediction GUI");
        this.match = match;

        GridLayout mainLayout  = new GridLayout();
        mainLayout.numColumns = 2;
        this.setLayout(mainLayout);
        
        @SuppressWarnings("unused")
        WimbledonScorePanel sc = new WimbledonScorePanel(this, match);

        GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 1 , 2);
        st = new StatisticsPanel(this, match);
        st.setLayoutData(gd);

        addMarketDataGrid(this, match);
        
        //this.statisticsUpdateThread = new StatisticsUpdateThread(match);
        //this.statisticsUpdateThread.addListener(st);

        this.scoreUpdateThread = new ScoreUpdateThread(match);
        //Image im = new Image(this.getDisplay(), "images/scoreboard.png" );
        //this.setBackgroundImage(im);
    }
    
    public void start(){
        String statsString = getStatsString("data/test/tennisinsight-tso-fed.dat");
        new StatisticsParser(statsString, match).parseAndSetStatistics();
        st.handleEvent(new Event());
        //statisticsUpdateThread.start();
        if (match.isInPlay()) {
         log.info("Live match: starting score update thread");
            // only start score fetching for live matches
            scoreUpdateThread.start();
        }
    }
    
    /**
* Adds the market data grid with back and lay values and amounts
*
* @param comp
* @param ti
*/
    private void addMarketDataGrid(Composite comp, Match match) {
        UpdatableMarketDataGrid grid = new UpdatableMarketDataGrid(comp,
                SWT.NONE, match);
        grid.setLayoutData( new GridData(GridData.FILL, GridData.FILL, true, true, 1 , 1));
        BetController betController = new BetController(Arrays.asList(grid
                .getP1BackButtons()), Arrays.asList(grid.getP1LayButtons()),
                Arrays.asList(grid.getP2BackButtons()), Arrays.asList(grid
                        .getP2LayButtons()), match);
        grid.setBetController(betController);
        LiveDataFetcher.registerForMatchUpdate(grid, match);
        // BetManager.registerGrid(match, table);
    }
    


    @Override
    public String getTitle() {
        return "Prediction";
    }
    
    private static String getStatsString(String filename) {

        Scanner scanner;
        String test = "";

        try {
            scanner = new Scanner(new FileInputStream(filename));
        
            while (scanner.hasNext()){
            test += scanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            // log.error(e.getMessage());
        }

        return test;
    }   
}