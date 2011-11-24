package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.ic.tennistrader.domain.match.Match;

public class StatisticsPanel {
	private Match match;
	
	private Tree tree;
	
	public StatisticsPanel(Composite composite, Match match) {
		this.match = match;
        composite.setLayout(new FillLayout());
        this.tree = new Tree(composite, SWT.MULTI | SWT.FULL_SELECTION
                | SWT.CENTER);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        TreeColumn[] tcolumn = new TreeColumn[3];
        tcolumn[0] = new TreeColumn(tree, SWT.LEFT);
        tcolumn[0].setText("Player 1");
        tcolumn[0].setWidth(220);
        tcolumn[0].setResizable(false);

        tcolumn[1] = new TreeColumn(tree, SWT.CENTER);
        tcolumn[1].setText("VS");
        tcolumn[1].setWidth(150);
        tcolumn[1].setResizable(false);

        tcolumn[2] = new TreeColumn(tree, SWT.RIGHT);
        tcolumn[2].setText("Player 2");
        tcolumn[2].setWidth(200);
        tcolumn[2].setResizable(false);

        //return tree;
    }
	
	public Tree getTree()
	{
		return this.tree;
	}
}
