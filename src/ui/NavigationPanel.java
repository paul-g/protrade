package src.ui;

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import src.Pair;

public class NavigationPanel {

  private final CTabFolder folder;
  private Tree tree;
  private final Text searchBox;
  private Stack<List<Pair<String, Pair<Integer, Integer>>>> treeStates = new Stack<List<Pair<String, Pair<Integer, Integer>>>>();

  private List<Listener> listeners;

  private String prevSearchBoxText = "";

  public NavigationPanel(Shell shell) {
    this.folder = new CTabFolder(shell,  SWT.RESIZE | SWT.BORDER);
    folder.setSimple(false);

    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.horizontalSpan = 1;
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace   = true;
    
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
    TreeItem ty= new TreeItem(tree.getItems()[0], SWT.NONE, 1);
    ty.setText("Test item");
    listeners = new ArrayList<Listener>();
    
    GridData tgridData = new GridData();
    tgridData.horizontalAlignment = GridData.FILL;
    tgridData.verticalAlignment = GridData.FILL;
    tgridData.grabExcessHorizontalSpace = true;
    tgridData.grabExcessVerticalSpace   = true;
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
            removedPairs.add(new Pair<String, Pair<Integer,Integer>>(children[j].getText(), new Pair<Integer, Integer>(i,j)));
            children[j].dispose();
          }
        }
      }
      treeStates.push(removedPairs);
    } else {
      // char removed - go back to previous tree state
      List<Pair<String, Pair<Integer, Integer>>> previousState = treeStates.pop();
      for (Pair<String, Pair<Integer, Integer>> p : previousState){
        TreeItem ti = new TreeItem(items[p.getJ().getI()], SWT.NONE, p.getJ().getJ());
        ti.setText(p.getI());
      }
      
    }
  }

  private static void loadTennisMatches(Tree tree) {
    // Wimbledon
    TreeItem wimb = new TreeItem(tree, SWT.NONE);
    wimb.setText("Wimbledon - In progress");
    TreeItem feddjo = new TreeItem(wimb, SWT.NONE);
    feddjo.setText("Federer vs Djokovic - 60 60 60");
    TreeItem nadmur = new TreeItem(wimb, SWT.NONE);
    nadmur.setText("Nadal vs Murray - 75 62 31");

    // Piatra-Neamt
    TreeItem pno = new TreeItem(tree, SWT.NONE);
    pno.setText("Piatra-Neamt Open - Pending");
    TreeItem paco = new TreeItem(pno, SWT.NONE);
    paco.setText("Paul vs Corina - Pending");
    TreeItem fena = new TreeItem(pno, SWT.NONE);
    fena.setText("Ferrer vs Nalbandian - Pending");
  }
  
}