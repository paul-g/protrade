package org.ic.tennistrader.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class UiSample {

	final static int SIZE = 6;

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);

		GridLayout layout = new GridLayout(2, false);

		shell.setLayout(layout);

		// shell.setLayout(new FillLayout());

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);

		final SimpleWidget[] widgets = new SimpleWidget[SIZE];

		for (int i = 0; i < SIZE; i++) {
			final SimpleWidget widget = new SimpleWidget(shell, SWT.BORDER);
			widget.setLayoutData(gd);
			widgets[i] = widget;
		}
		
		
		Menu widgetMenu = new Menu(shell, SWT.POP_UP);
		
		MenuItem mi = new MenuItem(widgetMenu, SWT.PUSH);
		mi.setText("Span");
		
		mi.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Pressed ");
				widgets[3].dispose();
				widgets[2].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				//widgets[2].setBounds(50, 50, 200, 200);
				//widgets[2].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
				shell.layout();
			}
		});
		
		widgets[2].setMenu(widgetMenu);

		shell.setSize(400, 400);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}
}