package org.ic.tennistrader.score;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.Main;
import org.ic.tennistrader.controller.BetController;
import org.ic.tennistrader.domain.EventBetfair;
import org.ic.tennistrader.domain.EventMarketBetfair;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;

public class PredictionGui extends StandardWidgetContainer {

    private static Logger log = Logger.getLogger(Main.class);

    private ScoreUpdateThread scoreUpdateThread;

    private StatisticsUpdateThread statisticsUpdateThread;
    
    private Match match;
    
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
        this.match = match;

        RowLayout mainLayout = new RowLayout();
        mainLayout.type = SWT.HORIZONTAL;
        //mainLayout.pack = true;
        mainLayout.fill = true;
        this.setLayout(mainLayout);
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;
        Composite panels = new Composite(this, SWT.NONE);
        panels.setLayout(gridLayout);
        
        @SuppressWarnings("unused")
        ScorePanel sc = new ScorePanel(panels, match);

        @SuppressWarnings("unused")
        ProbabilityPanel probabilityPanel = new ProbabilityPanel(panels, match);
        
        addMarketDataGrid(panels, match);
        
        StatisticsPanel st = new StatisticsPanel(this, match);
        
        this.statisticsUpdateThread = new StatisticsUpdateThread(match);
        this.statisticsUpdateThread.addListener(st);

        this.scoreUpdateThread = new ScoreUpdateThread(match);

    }
    
    public void start(){
        statisticsUpdateThread.start();
        if (match.isInPlay()) {
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
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace=true;
        gridData.horizontalAlignment = GridData.FILL;
        grid.setLayoutData(gridData);
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
}