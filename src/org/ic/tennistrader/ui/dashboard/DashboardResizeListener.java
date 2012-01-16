package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

final class DashboardResizeListener implements Listener {
	/**
	 * 
	 */
	private final Dashboard dashboard;

	/**
	 * @param dashboard
	 */
	DashboardResizeListener(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	@Override
	public void handleEvent(Event arg0) {
		Rectangle area = this.dashboard.getClientArea();

		Dashboard.log.info("Previous client area " + this.dashboard.clientArea + " "
				+ " new client area " + this.dashboard.getClientArea());

		if (this.dashboard.clientArea.width != 0 && this.dashboard.clientArea.height != 0
				&& area.width != 0 && area.height != 0) {
			double heightRatio = (double) this.dashboard.clientArea.height
					/ this.dashboard.getClientArea().height;
			double widthRatio = (double) this.dashboard.clientArea.width
					/ this.dashboard.getClientArea().width;
			this.dashboard.scaleWidgets(widthRatio, heightRatio);
			this.dashboard.layoutWidgets();
		}

		this.dashboard.clientArea = area;
	}
}