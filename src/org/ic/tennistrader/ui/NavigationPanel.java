package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.ic.tennistrader.authentication.BetfairAuthenticator;
import org.ic.tennistrader.domain.Tournament;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.listener.MatchSelectionListener;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;
import org.ic.tennistrader.ui.widgets.SearchTree;

public class NavigationPanel {

	private final CTabFolder folder;

	private final List<MatchSelectionListener> listeners = new ArrayList<MatchSelectionListener>();

	private final HashMap<TreeItem, RealMatch> matchMap = new HashMap<TreeItem, RealMatch>();

	private static SearchTree searchTree;

	private static final Logger log = Logger.getLogger(NavigationPanel.class);

	public NavigationPanel(Composite shell) {
		this.folder = new CTabFolder(shell, SWT.RESIZE | SWT.BORDER);
		folder.setSimple(false);
		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);

		GridData gridData = makeLayoutData();
		folder.setLayoutData(gridData);

		makeLiveMatchesItem();

		makeRecordedMatchesItem();
	}

	private void makeRecordedMatchesItem() {
		CTabItem navigation = new CTabItem(folder, SWT.CLOSE);
		navigation.setText("Recorded Matches");

	}

	private void makeLiveMatchesItem() {
		CTabItem navigation = new CTabItem(folder, SWT.CLOSE);
		navigation.setText("Live Matches");

		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayout(new FillLayout());
		BetfairAuthenticator.checkLogin("corina409", "testpass1");
		searchTree = new SearchTree(composite);

		fetchTennisMatches(searchTree.getTree());
		navigation.setControl(composite);

		searchTree.getTree().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem ti = (TreeItem) event.item;
				Match match = getMatch(ti);
				log.info("Match selection fired " + match);
				if (match != null)
					for (MatchSelectionListener msl : listeners)
						msl.handleMatchSelection(match);
			}
		});
	}

	private GridData makeLayoutData() {
		return new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1);
	}

	public void addListener(MatchSelectionListener listener) {
		listeners.add(listener);
	}

	// only fetches matches from Betfair
	private void fetchTennisMatches(Tree tree) {
		List<Tournament> tours = BetfairConnectionHandler.getTournamentsData();
		for (Tournament t : tours) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(t.toString());

			for (RealMatch m : t.getMatches()) {
				TreeItem child = new TreeItem(item, SWT.NONE);
				child.setText(m.toString());
				matchMap.put(child, m);
			}
		}
	}

	public CTabFolder getFolder() {
		return this.folder;
	}

	public void addTab(String text) {
		CTabItem cti = new CTabItem(folder, SWT.CLOSE);
		cti.setText(text);
		folder.setSelection(cti);
	}

	public Match getMatch(TreeItem treeItem) {
		return matchMap.get(treeItem);
	}

	public static TreeItem getSelection() {
		return searchTree.getSelection();
	}

	public Match getSelectedMatch() {
		return getMatch(getSelection());
	}

	public boolean isTabPresent(String title) {
		return getTab(title) != null;
	}

	public CTabItem getTab(String title) {
		CTabItem item = null;
		CTabItem[] ctis = folder.getItems();
		for (int i = 0; item == null && i < ctis.length; i++) {
			if (ctis[i].getText().equals(title))
				item = ctis[i];
		}

		return item;
	}
}