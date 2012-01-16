package org.ic.protrade.ui.widgets;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

public class StandardTabbedWidgetRightClickListener implements Listener{

       private Menu menu;
       private CTabFolder folder;

       public StandardTabbedWidgetRightClickListener(Menu menu, CTabFolder folder) {
           this.menu = menu;
           this.folder = folder;
       }

       @Override
       public void handleEvent(Event event) {
           Point click = new Point(event.x, event.y);
           Point point = folder.getDisplay().map(null, folder, click);
           CTabItem item = folder.getItem(point);
           if (item != null) {
               //selected = item;
               menu.setLocation(click);
               menu.setVisible(true);
           }
       }
}