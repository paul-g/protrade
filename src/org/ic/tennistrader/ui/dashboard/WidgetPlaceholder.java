package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.ui.dialogs.AddWidgetDialog;
import org.ic.tennistrader.ui.widgets.SavableWidget;
import org.ic.tennistrader.ui.widgets.WidgetType;

public class WidgetPlaceholder extends Composite implements SavableWidget {

	private final Button b;
	private final WidgetContainer widgetContainer;

	WidgetPlaceholder(Composite parent, int style,
			WidgetContainer widgetContainer) {
		super(parent, style);
		this.widgetContainer = widgetContainer;
		setLayout(new FillLayout());
		b = new Button(this, SWT.PUSH);
		Image image = new Image(Display.getCurrent(), "images/plus.png");
		final WidgetContainer wc = widgetContainer;
		b.setImage(image);
		b.setText("Add widget");
		b.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				AddWidgetDialog adw = new AddWidgetDialog();
				adw.setWidgetPlaceholder(WidgetPlaceholder.this);
				adw.show();
				Composite selection = adw.getSelection();
				if (selection != null) {
					wc.setWidget(adw.getSelection());
					dispose();
				}
			}
		});
		image.dispose();
	}

	public Dashboard getDashboard() {
		return widgetContainer.getDashboard();
	}

	@Override
	public WidgetType getName() {
		return WidgetType.PLACEHOLDER;
	}
}
