package org.ic.tennistrader.score;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.ui.widgets.MatchViewerWidget;
import org.ic.tennistrader.ui.widgets.WidgetType;

public class StatisticsPanel extends MatchViewerWidget implements Listener {

	private Tree tree;
	private TreeColumn playerOneColumn;
	private TreeColumn playerTwoColumn;

	public StatisticsPanel(Composite parent) {
		super(parent, SWT.NONE);
		init(parent, "Player 1", "Player 2");
	}

	public StatisticsPanel(Composite parent, Match match) {
		super(parent, SWT.NONE);
		this.match = match;
		String pl1Lastname = match.getPlayerOne().getLastname();
		String pl2Lastname = match.getPlayerTwo().getLastname();
		init(parent, pl1Lastname, pl2Lastname);
	}

	private void init(Composite parent, String pl1Lastname, String pl2Lastname) {
		setLayout(new FillLayout());

		this.tree = new Tree(this, SWT.NONE);
		tree.setHeaderVisible(true);

		playerOneColumn = new TreeColumn(tree, SWT.LEFT);
		playerOneColumn.setText(pl1Lastname);
		playerOneColumn.setWidth(140);
		playerOneColumn.setResizable(false);

		TreeColumn midColumn = new TreeColumn(tree, SWT.CENTER);
		midColumn.setText("VS");
		midColumn.setWidth(140);
		midColumn.setResizable(false);

		playerTwoColumn = new TreeColumn(tree, SWT.RIGHT);
		playerTwoColumn.setText(pl2Lastname);
		playerTwoColumn.setWidth(140);
		playerTwoColumn.setResizable(false);
	}

	public Tree getTree() {
		return this.tree;
	}

	@Override
	public void handleEvent(Event arg0) {
		System.out.println("Added stats");
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				SiteParser parser = new SiteParser();
				tree.getColumn(0).setImage(
						parser.getPlayerImage(match.getPlayerOne()));
				tree.getColumn(2).setImage(
						parser.getPlayerImage(match.getPlayerTwo()));
				tree.setItemCount(0);
				final TreeItem basics = new TreeItem(tree, SWT.MULTI
						| SWT.CENTER);
				basics.setText(1, "Basics");
				basics.setForeground(tree.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
				basics.setBackground(tree.getDisplay().getSystemColor(
						SWT.COLOR_DARK_GREEN));
				basics.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

				Player pl1 = match.getPlayerOne();
				Player pl2 = match.getPlayerTwo();

				makeTreeLine(basics, "Country", pl1.getCountry(),
						pl2.getCountry());
				makeTreeLine(basics, "DoB", pl1.getDob(), pl2.getDob());
				makeTreeLine(basics, "Height", pl1.getHeight(), pl2.getHeight());
				makeTreeLine(basics, "W-L", pl1.getWonLost(), pl2.getWonLost());
				makeTreeLine(basics, "Plays", pl1.getPlays(), pl2.getPlays());
				makeTreeLine(basics, "Rank", pl1.getRank(), pl2.getRank());

				TreeItem matchTree = new TreeItem(tree, SWT.CENTER);
				matchTree.setText(1, "Match Stats");
				matchTree.setForeground(tree.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
				matchTree.setBackground(tree.getDisplay().getSystemColor(
						SWT.COLOR_DARK_GREEN));
				matchTree.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

				String[] playerOneWonLost = match.getPlayerOneWonLost();
				String[] playerTwoWonLost = match.getPlayerTwoWonLost();

				String[] matchTreeTitles = { "Match W/L %", "Set W/L %",
						"Game W/L %", "Points W/L %", "Tiebreaks W/L %",
						"Tiebreaks/Set" };
				for (int i = 0; i < playerOneWonLost.length; i++) {
					makeTreeLine(matchTree, matchTreeTitles[i],
							playerOneWonLost[i], playerTwoWonLost[i]);
				}

				TreeItem serves = new TreeItem(tree, SWT.CENTER);
				serves.setText(1, "Serve Stats");
				serves.setForeground(tree.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
				serves.setBackground(tree.getDisplay().getSystemColor(
						SWT.COLOR_DARK_GREEN));
				serves.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

				Map<String, String[][]> statisticsMap = match
						.getStatisticsMap();

				for (String s : statisticsMap.keySet()) {

					String[][] values = statisticsMap.get(s);
					TreeItem item = new TreeItem(serves, SWT.CENTER);
					item.setText(1, s);
					for (int i = 0; i < values.length; i++) {
						makeTreeLine(item, values[i][1], values[i][0],
								values[i][2]);
					}
				}

			}
		});

	}

	private void makeTreeLine(TreeItem parent, String description,
			String playerOneValue, String playerTwoValue) {
		TreeItem item = new TreeItem(parent, SWT.CENTER);
		item.setText(0, playerOneValue);
		item.setText(1, description);
		item.setBackground(1, tree.getDisplay()
				.getSystemColor(SWT.COLOR_YELLOW));
		item.setText(2, playerTwoValue);
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
		return WidgetType.STATISTICS_PANEL;
	}

	@Override
	public void setMatch(Match match) {
		this.match = match;
		playerOneColumn.setText(match.getPlayerOne().getLastname());
		playerTwoColumn.setText(match.getPlayerTwo().getLastname());
		StatisticsUpdateThread thread = new StatisticsUpdateThread(match);
		thread.addListener(this);
		thread.start();
	}
}
