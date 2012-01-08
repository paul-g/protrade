package org.ic.tennistrader.listener;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
public class ToolItemListener implements Listener {

	private final Menu menu;
	private final ToolBar tb;
	private final ToolItem ti;
	private final boolean left;

	public ToolItemListener(ToolBar tb, ToolItem ti, Menu menu, boolean left) {
		this.menu = menu;
		this.tb = tb;
		this.ti = ti;
		this.left = left;
	}

	@Override
	public void handleEvent(Event event) {
		Rectangle r = ti.getBounds();
		Point point;
		if (left) point = tb.toDisplay(r.x,r.y + r.height);
		else point = tb.toDisplay(r.x + r.width, r.y + r.height);
		menu.setLocation(point);
		menu.setVisible(true);
	}
}