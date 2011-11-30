package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public class MatchDataView extends StandardWidgetContainer implements UpdatableWidget {

	Label marketStatus;
	
    public MatchDataView(Composite arg0, int arg1, final Match match) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub

        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        this.setLayout(rowLayout);

        Label name = new Label(this, SWT.BORDER);
        name.setText("Match : " + match.getName());

        Label status = new Label(this, SWT.BORDER);
        String st = (match.isInPlay() ? "In Progress" : "Not In Progress");
        status.setText("Status: " + st);

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
                LiveDataFetcher.setPlaybackSpeed(match, Integer
                        .parseInt(selectionItems[combo.getSelectionIndex()]));
            }
        });

        this.pack();
    }
    
    private void changeStatus(String status) {
		if (status != null){
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

}
