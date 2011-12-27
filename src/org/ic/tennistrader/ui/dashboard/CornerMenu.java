package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

class CornerMenu extends Composite {
	
	CornerMenu(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		Button close = new Button(this, SWT.PUSH);
		close.setText("X");
		Button menu = new Button(this, SWT.PUSH);
		menu.setText(">");
		setSize(20, 20);
		setVisible(false);
	}
	
}