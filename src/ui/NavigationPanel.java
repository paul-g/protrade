package src.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import src.Pair;
import src.domain.EventMarketBetfair;
import src.domain.MarketBetfair;
import src.domain.EventBetfair;
import src.domain.Match;
import src.domain.Tournament;
import src.service.BetfairConnectionHandler;

public class NavigationPanel {

	private final CTabFolder folder;
	private Tree tree;
	private final Text searchBox;
	private Stack<List<Pair<String, Pair<Integer, Integer>>>> treeStates = new Stack<List<Pair<String, Pair<Integer, Integer>>>>();

	private List<Listener> listeners;

	private String prevSearchBoxText = "";

	private static HashMap<TreeItem, Match> matchMap = new HashMap<TreeItem, Match>();

	public NavigationPanel(Composite shell) {
		this.folder = new CTabFolder(shell, SWT.RESIZE | SWT.BORDER);
		folder.setSimple(false);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		folder.setLayoutData(gridData);

		CTabItem navigation = new CTabItem(folder, SWT.CLOSE);
		navigation.setText("Match Navigator");

		Composite composite = new Composite(folder, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		this.searchBox = new Text(composite, SWT.NONE);

		GridData sgridData = new GridData();
		sgridData.horizontalAlignment = GridData.FILL;
		sgridData.verticalAlignment = GridData.FILL;
		sgridData.grabExcessHorizontalSpace = true;
		searchBox.setLayoutData(sgridData);

		folder.setLayoutData(gridData);
		this.tree = new Tree(composite, SWT.NONE);
		loadTennisMatches(tree);
		listeners = new ArrayList<Listener>();

		GridData tgridData = new GridData();
		tgridData.horizontalAlignment = GridData.FILL;
		tgridData.verticalAlignment = GridData.FILL;
		tgridData.grabExcessHorizontalSpace = true;
		tgridData.grabExcessVerticalSpace = true;
		tree.setLayoutData(tgridData);

		folder.setLayoutData(gridData);

		navigation.setControl(composite);

		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);

		searchBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				String text = searchBox.getText();
				filterTree(text);
				prevSearchBoxText = text;
			}

		});

		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				for (Listener l : listeners)
					l.handleEvent(event);
			}
		});

	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	private void filterTree(String text) {
		TreeItem[] items = tree.getItems();
		if (text.length() > prevSearchBoxText.length()) {
			// char added to text - go to next by filtering entries
			List<Pair<String, Pair<Integer, Integer>>> removedPairs = new ArrayList<Pair<String, Pair<Integer, Integer>>>();
			for (int i = 0; i < items.length; i++) {
				TreeItem children[] = items[i].getItems();
				for (int j = 0; j < children.length; j++) {
					String txt = children[j].getText();
					if (!txt.contains(text)) {
						removedPairs
								.add(new Pair<String, Pair<Integer, Integer>>(
										children[j].getText(),
										new Pair<Integer, Integer>(i, j)));
						children[j].dispose();
					}
				}
			}
			treeStates.push(removedPairs);
		} else {
			// char removed - go back to previous tree state
			List<Pair<String, Pair<Integer, Integer>>> previousState = treeStates
					.pop();
			for (Pair<String, Pair<Integer, Integer>> p : previousState) {
				TreeItem ti = new TreeItem(items[p.getJ().getI()], SWT.NONE, p
						.getJ().getJ());
				ti.setText(p.getI());
			}

		}
	}

	private void loadTennisMatches(Tree tree) {
		List<Tournament> tours = BetfairConnectionHandler.getTournamentsData();
		for (Tournament t : tours) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(t.toString());
			matchMap.put(item, new Match("pl1", "Pl 2", null)); // to avoid NullPointerException

			for (Match m : t.getMatches()) {
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

	public static Match getMatch(TreeItem treeItem) {
		return matchMap.get(treeItem);
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