package src.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class NavigationPanel {
  
  private final Tree tree;
  private List<Listener> listeners;
  
  public NavigationPanel(Shell shell){
    this.tree = new Tree(shell, SWT.NONE);
    listeners = new ArrayList<Listener>();
    loadTennisMatches(tree);
    
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

  private static void loadTennisMatches(Tree tree) {
    // Wimbledon
    TreeItem wimb = new TreeItem(tree, SWT.NONE);
    wimb.setText("Wimbledon ");
    TreeItem feddjo = new TreeItem(wimb, SWT.NONE);
    feddjo.setText("Federer vs Djokovic ");
    TreeItem nadmur = new TreeItem(wimb, SWT.NONE);
    nadmur.setText("Nadal vs Murray ");

    // Piatra-Neamt
    TreeItem pno = new TreeItem(tree, SWT.NONE);
    pno.setText("Piatra-Neamt Open");
    TreeItem paco = new TreeItem(pno, SWT.NONE);
    paco.setText("Paul vs Corina");
    TreeItem fena = new TreeItem(pno, SWT.NONE);
    fena.setText("Ferrer vs Nalbandian ");
  }
}
