package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class WidgetContainerMoveListener implements Listener {

	private Point origin;
	private WidgetContainer wc;

	WidgetContainerMoveListener(WidgetContainer wc) {
		this.wc = wc;
	}

	@Override
	public void handleEvent(Event e) {
		int x = e.x;
		int y = e.y;
		switch (e.type) {
		case SWT.MouseDown:
			if (e.button == 3) {
				wc.setCursor(wc.getDisplay()
						.getSystemCursor(SWT.CURSOR_SIZEALL));
				origin = new Point(x, y);
			}
			break;
		case SWT.MouseUp:
			if (origin != null) {
				int dx = e.x - origin.x;
				int dy = e.y - origin.y;
				wc.getDashboard().move(wc, dx, dy);
				wc.setCursor(wc.getDisplay()
						.getSystemCursor(SWT.CURSOR_ARROW));
			}
			origin = null;
			break;
		}
	}
}
