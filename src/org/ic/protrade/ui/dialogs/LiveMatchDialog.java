package org.ic.protrade.ui.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.ui.NavigationPanel;

public class LiveMatchDialog extends Dialog {

	private static Logger log = Logger.getLogger(LiveMatchDialog.class);
	private NavigationPanel navigationPanel;
	private Match selection;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		new LiveMatchDialog(shell).open();
	}

	public LiveMatchDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 1;
		navigationPanel = new NavigationPanel(comp);
		GridData gd = new GridData();
		gd.widthHint = 500;
		gd.heightHint = 500;
		comp.setLayoutData(gd);
		// this.getShell().setSize(400, 500);
		return comp;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// createButton(parent, RESET_ID, "Reset All", false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		selection = navigationPanel.getSelectedMatch();
		super.buttonPressed(buttonId);
	}

	public Match getSelectedMatch() {
		return selection;
	}
}
