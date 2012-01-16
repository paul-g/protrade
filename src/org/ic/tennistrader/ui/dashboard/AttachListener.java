package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

final class AttachListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final WidgetContainer widgetContainer;

	AttachListener(WidgetContainer widgetContainer) {
		this.widgetContainer = widgetContainer;
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		super.widgetSelected(arg0);
		widgetContainer.setDettached(false);
		Shell previous = widgetContainer.getWidget().getShell();
		widgetContainer.setWidget(widgetContainer.getWidget());
		previous.dispose();
	}
}