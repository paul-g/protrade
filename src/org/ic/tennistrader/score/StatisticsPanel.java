package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;


public class StatisticsPanel implements Listener {

    private Match match;

    private Tree tree;
    
    private final Display display;

    public StatisticsPanel(Composite composite, Match match) {
        this.match = match;
        this.display = composite.getDisplay();
        composite.setLayout(new FillLayout());
        this.tree = new Tree(composite, SWT.MULTI | SWT.FULL_SELECTION
                | SWT.CENTER);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        TreeColumn[] tcolumn = new TreeColumn[3];
        tcolumn[0] = new TreeColumn(tree, SWT.LEFT);
        tcolumn[0].setText(match.getPlayerOne().toString());
        tcolumn[0].setWidth(220);
        tcolumn[0].setResizable(false);

        tcolumn[1] = new TreeColumn(tree, SWT.CENTER);
        tcolumn[1].setText("VS");
        tcolumn[1].setWidth(150);
        tcolumn[1].setResizable(false);

        tcolumn[2] = new TreeColumn(tree, SWT.RIGHT);
        tcolumn[2].setText(match.getPlayerTwo().toString());
        tcolumn[2].setWidth(200);
        tcolumn[2].setResizable(false);
    }

    public Tree getTree() {
        return this.tree;
    }

    @Override
    public void handleEvent(Event arg0) {
        System.out.println("Added stats");
        display.asyncExec(new Runnable(){
            @Override
            public void run() {
                final TreeItem basics = new TreeItem(tree, SWT.MULTI | SWT.CENTER);
                basics.setText(1, "Basics");
                basics
                        .setForeground(tree.getDisplay().getSystemColor(
                                SWT.COLOR_WHITE));
                basics.setBackground(tree.getDisplay().getSystemColor(
                        SWT.COLOR_DARK_GREEN));
                basics.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));
                
                Player pl1 = match.getPlayerOne();
                Player pl2 = match.getPlayerTwo();
                
                makeTreeLine(basics, "Country", pl1.getCountry(), pl2.getCountry());
                makeTreeLine(basics, "DoB", pl1.getDob(), pl2.getDob());
                makeTreeLine(basics, "Height", pl1.getHeight(), pl2.getHeight());
                makeTreeLine(basics, "W-L", pl1.getWonLost(), pl2.getWonLost());
                makeTreeLine(basics, "Plays", pl1.getPlays(), pl2.getPlays());
                makeTreeLine(basics, "Rank", pl1.getRank(), pl2.getRank());
            }
        });
  
    }
    
    private void makeTreeLine(TreeItem parent, String description, String playerOneValue, String playerTwoValue){
        TreeItem item = new TreeItem(parent, SWT.CENTER);
        item.setText(0,playerOneValue);
        item.setText(1, description);
        item.setBackground(1, tree.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item.setText(2, playerTwoValue);
    }
    
    

}
