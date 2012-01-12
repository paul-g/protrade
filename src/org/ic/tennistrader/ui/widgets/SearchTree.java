package org.ic.tennistrader.ui.widgets;

import static org.ic.tennistrader.utils.Pair.pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.ic.tennistrader.authentication.BetfairAuthenticator;
import org.ic.tennistrader.domain.Tournament;
import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;
import org.ic.tennistrader.utils.Pair;

public class SearchTree extends Composite {

	private final Tree tree;
	private final Text searchBox;
	private String prevSearchBoxText = "";
	private final Stack<List<Pair<String, Pair<Integer, Integer>>>> treeStates = new Stack<List<Pair<String, Pair<Integer, Integer>>>>();

	public static void main(String args[]) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());

		// Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");

		BetfairAuthenticator.checkLogin("corina409", "testpass1");

		new SearchTree(shell);

		shell.setMaximized(true);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public SearchTree(Composite parent) {
		super(parent, SWT.None);

		// setLayout(new RowLayout(SWT.VERTICAL));
		setLayout(new GridLayout(1, true));

		this.searchBox = new Text(this, SWT.NONE);

		searchBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent me) {
				String text = searchBox.getText();
				filterTree(text);
				prevSearchBoxText = text;
			}
		});
		searchBox.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				false, false));

		tree = new Tree(this, SWT.NONE);
		tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		// fetchTennisMatches(tree);
		expandAll();
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
				// matchMap.put(child, m);
			}
		}
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
								.add(pair(children[j].getText(), pair(i, j)));
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
				TreeItem ti = new TreeItem(items[p.second().first()], SWT.NONE,
						p.second().second());
				ti.setText(p.first());
			}

		}
		expandAll();
	}

	private void expandAll() {
		for (TreeItem item : tree.getItems()) {
			item.setExpanded(true);
		}
	}

	public TreeItem getSelection() {
		if (tree.getChildren().length > 0)
			return tree.getSelection()[0];
		else
			return null;
	}

	public Tree getTree() {
		return tree;
	}
}
