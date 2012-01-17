package org.ic.protrade.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.service.DataManager;
import org.ic.protrade.ui.widgets.MatchViewerWidget;
import org.ic.protrade.ui.widgets.WidgetType;

public class MatchDataView extends MatchViewerWidget implements UpdatableWidget {

	private Label marketStatus;
	private Label nameLabel;
	private Label statusLabel;

	public MatchDataView(Composite parent, int style) {
		super(parent, style);

		String name = "No match selected";
		String status = "No match selected";

		init(match, name, status);
	}

	public MatchDataView(Composite parent, int style, final Match match) {
		super(parent, style);
		this.match = match;

		String name = match.getName();
		String status = match.getStatus();

		init(match, name, status);
	}

	private void init(final Match match, String name, String status) {
		setLayout(new RowLayout(SWT.VERTICAL));

		nameLabel = new Label(this, SWT.BORDER);
		nameLabel.setText("Match : " + name);

		statusLabel = new Label(this, SWT.BORDER);
		statusLabel.setText("Status: " + status);

		marketStatus = new Label(this, SWT.BORDER);
		marketStatus.setText("");

		(new Label(this, SWT.BORDER)).setText("Speed: ");
		final Combo combo = new Combo(this, SWT.READ_ONLY);
		final String[] selectionItems = new String[] { "1", "2", "5", "10" };
		combo.setItems(selectionItems);
		combo.select(0);
		Rectangle clientArea = this.getClientArea();
		combo.setBounds(clientArea.x, clientArea.y, 200, 200);

		combo.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				if (match != null)
					DataManager.setPlaybackSpeed(match,
							Integer.parseInt(selectionItems[combo
									.getSelectionIndex()]));
			}
		});

		pack();
	}

	private void changeStatus(String status) {
		if (status != null) {
			marketStatus.setText(status);
			marketStatus.pack();
		}
	}

	@Override
	public void handleUpdate(final MOddsMarketData newData) {
		this.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				changeStatus(newData.getMatchStatus());
			}
		});
	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		this.addDisposeListener(listener);
	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub
	}

	@Override
	public WidgetType getWidgetType() {
		return WidgetType.MATCH_VIEW;
	}

	@Override
	public void setMatch(Match match) {
		this.match = match;
		nameLabel.setText(match.getName());
		nameLabel.pack();
		statusLabel.setText(match.getStatus());
		statusLabel.pack();
		layout();
	}
}
