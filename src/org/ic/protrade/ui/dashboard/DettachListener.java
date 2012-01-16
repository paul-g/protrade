package org.ic.protrade.ui.dashboard;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

final class DettachListener extends SelectionAdapter {

	private static Logger log = Logger.getLogger(DettachListener.class);

	/**
	 * 
	 */
	private final WidgetContainer widgetContainer;

	/**
	 * @param widgetContainer
	 */
	DettachListener(WidgetContainer widgetContainer) {
		this.widgetContainer = widgetContainer;
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		super.widgetSelected(arg0);
		log.info("Dettached widget in separate shell");
		widgetContainer.setDettached(true);
		final Shell shell = new Shell(this.widgetContainer.getDisplay());
		shell.setLayout(new FillLayout());

		this.widgetContainer.getWidget().setParent(shell);
		shell.pack();
		shell.open();

		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem mi = new MenuItem(menuBar, SWT.CASCADE);
		mi.setText("Options");

		Menu menu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem attach = new MenuItem(menu, SWT.PUSH);
		attach.setText("Attach");
		attach.addSelectionListener(new AttachListener(widgetContainer));
		mi.setMenu(menu);

		shell.setMenuBar(menuBar);

		Display display = this.widgetContainer.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		log.info("Disposed dettached shell");
	}
}