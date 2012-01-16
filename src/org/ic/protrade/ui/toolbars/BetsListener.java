package org.ic.protrade.ui.toolbars;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolItem;

public class BetsListener implements Listener {
	private int x,y;
	private DashboardToolBar toolbar;
	
	public BetsListener(ToolItem ti, DashboardToolBar toolbar) {
		Rectangle r = ti.getBounds();
		x = r.x;
		y = (int) (r.y + r.height * 2.5);
		this.toolbar = toolbar;
	}

	@Override
	public void handleEvent(Event event) {
		if (toolbar.getBetsTable() == null) {
			toolbar.setBetsTable(new BetsTable(x,y,toolbar));
		} else {
			toolbar.getBetsTable().setFocus();
		}
	}
}