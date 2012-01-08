package org.ic.tennistrader.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.ui.richlist.RichListView;

public abstract class RichListDialog {

	protected Shell dialog;

	public RichListDialog() {
		Display d = Display.getCurrent();
		dialog = new Shell(d, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setLayout(new FillLayout());

		RichListView r = new RichListView(dialog, SWT.NONE);

		addElements(r);

		dialog.open();
		dialog.pack();
	}

	protected abstract void addElements(RichListView r);

	public void setText(String text) {
		dialog.setText(text);
	}

	public void show() {
		Display d = Display.getCurrent();
		while (!dialog.isDisposed()) {
			if (d.readAndDispatch())
				d.sleep();
		}
	}

	protected Control makeElementControl(Listener l) {
		Button button = new Button(dialog, SWT.PUSH | SWT.FLAT);
		Image image = new Image(Display.getCurrent(), "images/plus.png");
		button.setImage(image);
		button.setText("Add Now");
		button.addListener(SWT.Selection, l);
		image.dispose();
		return button;
	}

}