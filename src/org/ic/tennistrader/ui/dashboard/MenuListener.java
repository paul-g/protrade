package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;

class MenuListener extends MouseTrackAdapter {

	private final WidgetContainer widgetContainer;
	private CornerMenu cornerMenu;

	MenuListener(WidgetContainer widgetContainer, CornerMenu cornerMenu) {
		this.widgetContainer = widgetContainer;
		this.cornerMenu = cornerMenu;
	}

	@Override
	public void mouseExit(MouseEvent arg0) {
		widgetContainer.setCursor(widgetContainer.dCursor);
	}

	@Override
	public void mouseEnter(MouseEvent arg0) {
		// cornerMenu.setVisible(true);
	}
}