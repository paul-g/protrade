package org.ic.protrade.ui.dashboard;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class HelloSWTJFace extends ApplicationWindow {
	public HelloSWTJFace() {
		super(null);
	}

	protected Control createContents(Composite parent) {
		Text helloText = new Text(parent, SWT.CENTER);
		helloText.setText("Hello SWT and JFace!");
		parent.pack();
		return parent;
	}

	public static void main(String[] args) {
		HelloSWTJFace awin = new HelloSWTJFace();
		awin.setBlockOnOpen(true);
		awin.open();
		Display.getCurrent().dispose();
	}
}
