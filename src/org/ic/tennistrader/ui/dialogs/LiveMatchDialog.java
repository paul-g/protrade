package org.ic.tennistrader.ui.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.ui.NavigationPanel;

public class LiveMatchDialog extends Dialog {

	private static Logger log = Logger.getLogger(LiveMatchDialog.class);
	private NavigationPanel navigationPanel;
	private Match selection;

	public LiveMatchDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 1;
		navigationPanel = new NavigationPanel(comp);
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
