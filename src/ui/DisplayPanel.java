package src.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

public class DisplayPanel implements Listener{
  
  final CTabFolder tabFolder;

  public DisplayPanel(Shell shell){
    tabFolder = new CTabFolder(shell, SWT.BORDER);
    Rectangle clientArea = shell.getClientArea();
    tabFolder.setSimple(false);
    tabFolder.setLocation(clientArea.x, clientArea.y);
    tabFolder.pack();
  }

  public void handleEvent(Event event) {
    TreeItem ti = (TreeItem) event.item;
    
    CTabItem[] items = tabFolder.getItems();
    int pos = -1;
    
    for (int i=0;pos==-1 && i<items.length;i++) 
      if (items[i].getText().equals(ti.getText())) 
         pos = i;

    if (pos==-1) {
      CTabItem item = new CTabItem(tabFolder, SWT.CLOSE);
      item.setText(ti.getText());
      tabFolder.setSelection(item);
    } else 
     tabFolder.setSelection(pos);
  }
}
