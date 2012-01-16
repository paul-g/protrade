package org.ic.protrade.ui.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class ProgressDialog extends Dialog implements MatchLoadListener {

	private static final Logger log = Logger.getLogger(ProgressDialog.class);

	public ProgressDialog(Shell parentShell) {
		super(parentShell);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		log.info("Showing long running operation dialog");
		Composite comp = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;
		Label l = new Label(comp, SWT.NONE);
		l.setText("Loading match data...");
		ProgressBar pb = new ProgressBar(comp, SWT.INDETERMINATE);
		return comp;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
	}

	@Override
	public void handleMatchLoadComplete() {
		/*
		 * getShell().getDisplay().asyncExec(new Runnable() {
		 * 
		 * @Override public void run() { log.info("Closing dialog"); close(); }
		 * });
		 */
	}
}
