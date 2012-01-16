package org.ic.protrade.ui.score;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.match.HistoricalMatch;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.Player;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.domain.match.RealMatch;
import org.ic.protrade.domain.match.Score;
import org.ic.protrade.score.PredictionCalculator;
import org.ic.protrade.scrappers.livexscores.ScoreUpdateThread;
import org.ic.protrade.ui.betting.BetsDisplay;
import org.ic.protrade.ui.widgets.MatchViewerWidget;
import org.ic.protrade.ui.widgets.WidgetType;

public class WimbledonScorePanel extends MatchViewerWidget implements
		ScorePanel, Listener {
	private Table scoreTable;
	private PlayerEnum server;

	private Image im2;

	private static final String[] colLabelText = { "1", "2", "3", "4", "", "",
			"", "Sts", "", "Gms", "", "Pts", "" };
	private final int nCols = colLabelText.length;

	private final List<Label> colLabels = new ArrayList<Label>();

	private final Map<PlayerEnum, Map<Integer, Label>> labelMap = new HashMap<PlayerEnum, Map<Integer, Label>>();

	private static final Logger log = Logger
			.getLogger(WimbledonScorePanel.class);

	private final static int[] SETS = { 1, 2, 3, 4 };

	private final static int NAME = 5;

	private final static int PRED_MATCH = 6;
	private final static int SERVER = 7;

	private final static int SET = 8;
	private final static int PRED_SET = 9;
	private final static int GAME = 10;
	private final static int PRED_GAME = 11;
	private final static int POINT = 12;
	private final static int PRED_POINT = 13;

	private static final String PRED_SIZER = makeSizer(0);
	private static final String SIZER = makeSizer(0);

	private static final int DEFAULT_NAME_SIZE = 30;
	private static final int DEFAULT_EXTRA_SPACE = 3;
	private int nameSize = DEFAULT_NAME_SIZE;

	public WimbledonScorePanel(Composite parent) {
		super(parent, SWT.NONE);
		init(40, 40);
	}

	public static void main(String args[]) {
		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new FillLayout());
		Match m = new HistoricalMatch("data/fracsoft/fracsof1.csv", null);

		m.setPlayer1(new Player("Denis", "Kudla"));
		m.setPlayer2(new Player("Eduardo", "Schwank"));

		WimbledonScorePanel wsp = new WimbledonScorePanel(shell, m);
		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();
	}

	public WimbledonScorePanel(Composite parent, Match match) {
		super(parent, SWT.NONE);
		this.match = match;
		Display display = getDisplay();
		Image im = new Image(display, "images/green.jpg");
		this.im2 = new Image(display, "images/scoreboard1.png");
		this.setBackgroundImage(im);

		int name1Len = match.getPlayer(PlayerEnum.PLAYER1).toString().length();
		int name2Len = match.getPlayer(PlayerEnum.PLAYER2).toString().length();

		init(name1Len, name2Len);
		scoreThreadStart();
	}

	private void init(int name1Len, int name2Len) {
		Display display = getDisplay();
		Image im = new Image(display, "images/scoreboard.png");
		this.im2 = new Image(display, "images/scoreboard1.png");
		setBackgroundImage(im);

		nameSize = (name1Len > name2Len ? name1Len : name2Len);

		if (nameSize < DEFAULT_NAME_SIZE)
			nameSize = DEFAULT_NAME_SIZE;

		GridLayout gl = new GridLayout();
		gl.numColumns = nCols;
		setLayout(gl);

		for (int i = 0; i < nCols; i++)
			colLabels.add(makeColLabel(colLabelText[i]));

		initScoreLabels(PlayerEnum.PLAYER1);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.CENTER;

		dummyLabel(4);

		Label l = new Label(this, SWT.NONE);
		l.setText("vs");
		l.setFont(new Font(display, "Times", 10, SWT.NONE));
		l.setLayoutData(gd);
		l.setForeground(display.getSystemColor(SWT.COLOR_WHITE));

		dummyLabel(nCols - 5);

		initScoreLabels(PlayerEnum.PLAYER2);

		// setScores();
		// TODO: remove this
		// setDummyValues();

	}

	@SuppressWarnings("unused")
	private void setDummyValues() {
		Map<Integer, Label> pl1Map = labelMap.get(PlayerEnum.PLAYER1);
		pl1Map.get(1).setText("3");
		pl1Map.get(2).setText("7");
		Image ballSmall = getBallImage();

		Map<Integer, Label> pl2Map = labelMap.get(PlayerEnum.PLAYER2);
		pl2Map.get(1).setText("6");
		pl2Map.get(2).setText("6");
		pl2Map.get(SERVER).setImage(ballSmall);
	}

	private Image getBallImage() {
		Image ball = new Image(Display.getCurrent(), "images/ball.png");
		Image ballSmall = new Image(Display.getCurrent(), ball.getImageData()
				.scaledTo(20, 20));
		ball.dispose();
		return ballSmall;
	}

	private static String makeSizer(int n) {
		String s = new String();
		for (int i = 0; i < n + DEFAULT_EXTRA_SPACE; i++)
			s += " ";
		return s;
	}

	private void dummyLabel(int n) {
		for (int i = 0; i < n; i++)
			dummyLabel();
	}

	private void dummyLabel() {
		new Label(this, SWT.NONE);
	}

	private void initScoreLabels(PlayerEnum player) {
		Display display = getDisplay();
		Map<Integer, Label> map = new HashMap<Integer, Label>();

		String playername;
		if (match != null)
			playername = match.getPlayer(player).toString();
		else
			playername = player.toString();

		for (int i = 0; i < 4; i++)
			map.put(SETS[i], makeScoreLabelWithText(this, " 0 "));

		map.put(NAME,
				makeScoreLabelWithText(this, playername
						+ makeSizer(nameSize - playername.length())));
		map.put(SERVER, makeScoreLabelWithText(this, "    "));
		map.put(PRED_MATCH,
				makeScoreLabelWithText(this, "0%" + PRED_SIZER,
						makeFont(display),
						display.getSystemColor(SWT.COLOR_GREEN)));

		map.put(SET, makeScoreLabelWithText(this, " 0 " + SIZER));
		map.put(PRED_SET,
				makeScoreLabelWithText(this, "0%" + PRED_SIZER,
						makeFont(display),
						display.getSystemColor(SWT.COLOR_GREEN)));

		map.put(GAME, makeScoreLabelWithText(this, " 0 " + SIZER));
		map.put(PRED_GAME,
				makeScoreLabelWithText(this, "0%" + PRED_SIZER,
						makeFont(display),
						display.getSystemColor(SWT.COLOR_GREEN)));

		map.put(POINT, makeScoreLabelWithText(this, " 0 " + SIZER));
		map.put(PRED_POINT,
				makeScoreLabelWithText(this, "0%" + PRED_SIZER,
						makeFont(display),
						display.getSystemColor(SWT.COLOR_GREEN)));

		labelMap.put(player, map);
	}

	private Font makeFont(Display display) {
		return new Font(display, "Times", (int) (12 * displayRatio()), SWT.NONE);
	}

	private double displayRatio() {
		double res = 1;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension s = toolkit.getScreenSize();
		if (s.width == 1024 && s.height == 768)
			res = 0.8;
		return res;
	}

	private Label makeScoreLabelWithText(Composite comp, String string) {
		Display display = getDisplay();
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		Font f = new Font(display, "Times", (int) (12 * displayRatio()),
				SWT.BOLD);
		Label label = new Label(comp, SWT.NONE);
		label.setBackgroundImage(im2);
		// Tlabel.setBackground(Colours.getScoreLabelBackgroundColor());
		label.setFont(f);
		label.setLayoutData(gd);
		label.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
		label.setText(string);
		return label;
	}

	private Label makeScoreLabelWithText(Composite comp, String string, Font f,
			Color foreground) {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		Label label = new Label(comp, SWT.NONE);
		label.setBackgroundImage(im2);
		// label.setBackground(Colours.getScoreLabelBackgroundColor());
		label.setFont(f);
		label.setLayoutData(gd);
		label.setForeground(foreground);
		label.setText(string);
		return label;
	}

	private Label makeColLabel(String string) {
		Display display = getDisplay();
		Label label = new Label(this, SWT.NONE);
		label.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		label.setText(string);
		label.setFont(new Font(display, "Times", 10, SWT.NONE));
		return label;
	}

	public void scoreThreadStart() {
		final ScoreUpdateThread scoreUpdateThread = new ScoreUpdateThread(match);
		log.info("Check is match is live for score fetching");
		// if (match.isInPlay()) {
		log.info("Live match: starting score update thread");
		// only start score fetching for live matches

		if (match.isInPlay()) {
			try {
				scoreUpdateThread.addListener(this);
				scoreUpdateThread.start();
			} catch (Exception e) {
				// match.setScore(new Score());
			}
		}
	}

	private String prevScore = null;

	@Override
	public void setScores() {
		String score = match.getScore().toString();
		if (!score.equals(prevScore)) {
			log.info("Score changed.");
			setPlayerScore(match.getScore(), PlayerEnum.PLAYER1);
			setPlayerScore(match.getScore(), PlayerEnum.PLAYER2);
			updatePrediction();
		}
		prevScore = score;
	}

	private void updatePrediction() {
		final Display display = getDisplay();
		log.info("Updating predicted odds");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				final double[] result = new PredictionCalculator(match)
						.calculate(match);
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						setPlayerPrediction(
								PredictionCalculator.getP1Data(result),
								PlayerEnum.PLAYER1);
						setPlayerPrediction(
								PredictionCalculator.getP2Data(result),
								PlayerEnum.PLAYER2);

					}
				});
			}
		});

		t.start();
		// newData.setplayer1PredictedOdds(result[8]);
		// newData.setplayer2PredictedOdds(result[9]);
		log.info("Updated predicted odds");
	}

	private void setPlayerScore(Score score, PlayerEnum player) {
		Map<Integer, Label> labels = labelMap.get(player);
		for (int i = 1; i <= score.getSetsPlayed(); i++)
			labels.get(i).setText(
					Integer.toString(score.getSetScore(i)
							.getPlayerGames(player)));
		labels.get(SET).setText(score.getPlayerSets(player) + "");
		labels.get(GAME).setText(
				score.getPlayerScores(player)[score.getSetsPlayed()] + "");
		labels.get(POINT).setText(score.getPlayerPoints(player) + "");
		if (player.equals(score.getServer()))
			labels.get(SERVER).setImage(getBallImage());
		else
			labels.get(SERVER).setImage(im2);
	}

	private void setPlayerPrediction(double[] result, PlayerEnum player) {
		Map<Integer, Label> labels = labelMap.get(player);
		labels.get(PRED_POINT).setText(
				BetsDisplay.DOUBLE_FORMAT.format(result[0] * 100) + "%");
		labels.get(PRED_GAME).setText(
				BetsDisplay.DOUBLE_FORMAT.format(result[1] * 100) + "%");
		labels.get(PRED_SET).setText(
				BetsDisplay.DOUBLE_FORMAT.format(result[2] * 100) + "%");
		labels.get(PRED_MATCH).setText(
				BetsDisplay.DOUBLE_FORMAT.format(result[3] * 100) + "%");
	}

	@Override
	public void setServer(PlayerEnum player) {
		if (player == PlayerEnum.PLAYER1)
			scoreTable.getItem(0).setText(0, "S");
		else
			scoreTable.getItem(1).setText(0, "S");

		server = player;
	}

	@Override
	public PlayerEnum getServer() {
		return this.server;
	}

	@Override
	public void handleUpdate(final MOddsMarketData newData) {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				setScores();
			}
		});
	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		this.addDisposeListener(listener);
	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub

	}

	@Override
	public WidgetType getWidgetType() {
		return WidgetType.SCORE_PANEL;
	}

	@Override
	public void setMatch(Match match) {
		this.match = match;
		Label pl1Name = labelMap.get(PlayerEnum.PLAYER1).get(NAME);
		pl1Name.setText(match.getPlayerOne().toString());

		Label pl2Name = labelMap.get(PlayerEnum.PLAYER2).get(NAME);
		pl2Name.setText(match.getPlayerTwo().toString());

		if (match instanceof RealMatch) {
			// sleep to allow other threads to start faster
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			scoreThreadStart();
		}

	}

	@Override
	public void handleEvent(Event arg0) {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				setScores();
			}
		});
	}
}