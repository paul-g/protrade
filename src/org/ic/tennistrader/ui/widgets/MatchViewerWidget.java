package org.ic.tennistrader.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public abstract class MatchViewerWidget extends Composite implements
		UpdatableWidget, SavableWidget {

	protected Match match;

	public MatchViewerWidget(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setBackgroundMode(SWT.INHERIT_DEFAULT);

	}

	public String getTitle() {
		// containers only need a title if they'll be added to tabbedcontainer
		return "";
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public void setMatchAndRegisterForUpdates(Match match) {
		setMatch(match);
		DataManager.registerForMatchUpdate(this, match);
	}

	public void registerForMatchUpdate() throws Exception {
		if (match == null)
			throw new RuntimeException("No match was set for this widget");
		DataManager.registerForMatchUpdate(this, match);
	}
}
