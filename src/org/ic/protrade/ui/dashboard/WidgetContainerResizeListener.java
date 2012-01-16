package org.ic.protrade.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class WidgetContainerResizeListener implements Listener {

	private final WidgetContainer widgetContainer;
	private Point origin;
	private Location initialLocation;

	WidgetContainerResizeListener(WidgetContainer widgetContainer) {
		this.widgetContainer = widgetContainer;
	}

	@Override
	public void handleEvent(Event e) {

		int x = e.x;
		int y = e.y;

		if (!widgetContainer.resizeEnabled())
			return;

		switch (e.type) {
		case SWT.MouseDown:
			if (e.button == 1) {
				origin = widgetContainer.getDisplay().map(widgetContainer,
						null, x, y);
				initialLocation = this.widgetContainer.getLocation(x, y);
			}
			break;
		case SWT.MouseUp:
			if (e.button == 1)
				origin = null;
			break;
		case SWT.MouseMove:
			if ((e.stateMask & SWT.BUTTON3) != SWT.BUTTON3)
				this.widgetContainer.setCursor(widgetContainer
						.getCursorForLocation(x, y));
			if (origin != null && (e.stateMask & SWT.BUTTON1) == SWT.BUTTON1) {
				Point eP = widgetContainer.getDisplay().map(widgetContainer,
						null, e.x, e.y);
				int dx = (eP.x - origin.x);
				int dy = (eP.y - origin.y);
				origin = eP;
				widgetContainer.getDashboard().handleResize(widgetContainer,
						dx, dy, initialLocation);
			}
			break;
		}
	}
}