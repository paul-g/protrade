package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class WidgetPlaceholder extends Composite {

	private final Button b;
	private final WidgetContainer widgetContainer;

	WidgetPlaceholder(Composite parent, int style,
			WidgetContainer widgetContainer) {
		super(parent, style);
		this.widgetContainer = widgetContainer;
		setLayout(new FillLayout());
		b = new Button(this, SWT.PUSH);
		final WidgetContainer wc = widgetContainer;
		b.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {

				AddWidgetDialog adw = new AddWidgetDialog(
						WidgetPlaceholder.this);

				Composite selection = adw.getSelection();
				if (selection != null) {
					wc.setWidget(adw.getSelection());
					dispose();
				}
			}

		});
	}
}
