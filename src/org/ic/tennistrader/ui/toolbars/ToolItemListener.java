package org.ic.tennistrader.ui.toolbars;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

class ToolItemListener implements Listener {

	private final Menu menu;
	private final ToolBar tb;
	private final ToolItem ti;

	public ToolItemListener(ToolBar tb, ToolItem ti, Menu menu) {
		this.menu = menu;
		this.tb = tb;
		this.ti = ti;
	}

	@Override
	public void handleEvent(Event event) {
		Rectangle r = ti.getBounds();
		Point point = tb.toDisplay(r.x + r.width, r.y + r.height);
		menu.setLocation(point);
		menu.setVisible(true);
	}
}