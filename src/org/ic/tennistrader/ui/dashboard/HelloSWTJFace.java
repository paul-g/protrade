package org.ic.tennistrader.ui.dashboard;
import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

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
