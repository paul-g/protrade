package src.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class DisplayPanel implements Listener{
  
  final CTabFolder folder;

  public DisplayPanel(Composite parent){
    folder = new CTabFolder(parent, SWT.RESIZE | SWT.BORDER);
    folder.setSimple(false);
    folder.setMinimizeVisible(true);
    folder.setMaximizeVisible(true);
    
    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.horizontalSpan = 2;
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace   = true;
    
    folder.setLayoutData(gridData);
  }

  public void handleEvent(Event event) {
    TreeItem ti = (TreeItem) event.item;
    
    CTabItem[] items = folder.getItems();
    int pos = -1;
    
    for (int i=0;pos==-1 && i<items.length;i++) 
      if (items[i].getText().equals(ti.getText())) 
         pos = i;

    if (pos==-1) {
      CTabItem item = new CTabItem(folder, SWT.CLOSE);
      item.setText(ti.getText());
      Text text = new Text(folder, SWT.NONE);
      text.setText("Tab content for " + ti.getText());
      item.setControl(text);
      folder.setSelection(item);
      ChartDrawer chart_draw = new ChartDrawer(folder,ti.getText());
      ChartDrawer.showChart();
      item.setControl(ChartDrawer.chart);
    } else 
     folder.setSelection(pos);
    
  }
}
