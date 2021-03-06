package org.ic.protrade.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ic.protrade.ui.richlist.RichListView;

public abstract class RichListDialog {

	protected Shell dialog;

	public RichListDialog() {
		Display d = Display.getCurrent();
		dialog = new Shell(d, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		dialog.setLayout(new FillLayout());

		ScrolledComposite sc = new ScrolledComposite(dialog, SWT.H_SCROLL
				| SWT.V_SCROLL);

		RichListView r = new RichListView(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, true);
		r.setLayout(gridLayout);
		addElements(r);
		// dialog.setSize(400, 400);
		r.pack();

		Rectangle viewBounds = r.getBounds();

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setContent(r);
		sc.setMinSize(viewBounds.width, viewBounds.height);

		dialog.setSize(viewBounds.width,
				Display.getCurrent().getClientArea().height / 2);

		dialog.open();
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