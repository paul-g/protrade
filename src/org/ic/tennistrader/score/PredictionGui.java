package org.ic.tennistrader.score;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.markets.EventBetfair;
import org.ic.tennistrader.domain.markets.EventMarketBetfair;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.model.betting.BetController;
import org.ic.tennistrader.scrappers.livexscores.ScoreUpdateThread;
import org.ic.tennistrader.scrappers.tennisinsight.StatisticsUpdateThread;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.score.WimbledonScorePanel;
import org.ic.tennistrader.ui.updatable.MarketDataGrid;
import org.ic.tennistrader.ui.widgets.MatchViewerWidget;
import org.ic.tennistrader.ui.widgets.WidgetType;

public class PredictionGui extends MatchViewerWidget {

	private static Logger log = Logger.getLogger(PredictionGui.class);

	private final ScoreUpdateThread scoreUpdateThread;
	private StatisticsUpdateThread statisticsUpdateThread;

	private final StatisticsPanel st;

	public static void main(String args[]) {
		final Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());

		Player playerOne = new Player("Ivo", "Minar");
		Player playerTwo = new Player("Luke", "Saville");

		Match match = new RealMatch("", "", new EventBetfair(
				"Nadal v Del Potro", new ArrayList<EventMarketBetfair>(), 1));
		MOddsMarketData modds = new MOddsMarketData();
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

		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 2;
		this.setLayout(mainLayout);

		@SuppressWarnings("unused")
		MatchViewerWidget sc = new WimbledonScorePanel(this, match);

		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 1,
				2);
		st = new StatisticsPanel(this, match);
		st.setLayoutData(gd);

		addMarketDataGrid(this, match);

		this.statisticsUpdateThread = new StatisticsUpdateThread(match);
		this.statisticsUpdateThread.addListener(st);

		this.scoreUpdateThread = new ScoreUpdateThread(match);
		// Image im = new Image(this.getDisplay(), "images/scoreboard.png" );
		// this.setBackgroundImage(im);
	}

	public void start() {
		//String statsString = getStatsString("data/test/tennisinsight-tso-fed.dat");
		//new StatisticsParser(statsString, match).parseAndSetStatistics();
		//st.handleEvent(new Event());
		statisticsUpdateThread.start();
		//st.handleEvent(new Event());
		if (match.isInPlay()) {
			log.info("Live match: starting score update thread");
			// only start score fetching for live matches
			try{
				scoreUpdateThread.start();
			} catch (Exception e){
				match.setScore(new Score());
			}
		}
	}

	/**
	 * Adds the market data grid with back and lay values and amounts
	 * 
	 * @param comp
	 * @param ti
	 */
	private void addMarketDataGrid(Composite comp, Match match) {
		MarketDataGrid grid = new MarketDataGrid(comp,
				SWT.NONE, match);
		grid.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true, 1, 1));
		BetController betController = new BetController(Arrays.asList(grid
				.getP1BackButtons()), Arrays.asList(grid.getP1LayButtons()),
				Arrays.asList(grid.getP2BackButtons()), Arrays.asList(grid
						.getP2LayButtons()), match);
		grid.setBetController(betController);
		DataManager.registerForMatchUpdate(grid, match);
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

			while (scanner.hasNext()) {
				test += scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			// log.error(e.getMessage());
		}

		return test;
	}

	@Override
	public void handleUpdate(MOddsMarketData newData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public WidgetType getWidgetType() {
		return WidgetType.PREDICTION_GUI;
	}
}