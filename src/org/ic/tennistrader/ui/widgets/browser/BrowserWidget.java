package org.ic.tennistrader.ui.widgets.browser;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.ui.widgets.MatchViewerWidget;
import org.ic.tennistrader.ui.widgets.WidgetType;

public class BrowserWidget extends MatchViewerWidget {

	private static Logger log = Logger.getLogger(BrowserWidget.class);

	public BrowserWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(makeLayout());
		BrowserCoolbar bc = new BrowserCoolbar(this, SWT.NONE);
		bc.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1,
				1));

		final Browser browser;
		try {
			browser = new Browser(this, SWT.NONE);
		} catch (SWTError e) {
			log.error("Could not instantiate Browser: " + e.getMessage());
			return;
		}
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.setUrl("http://www.atpworldtour.com/Rankings/Singles.aspx");
	}

	private Layout makeLayout() {
		return new GridLayout(1, true);
	}

	@Override
	public void handleUpdate(MOddsMarketData newData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public WidgetType getWidgetType() {
		// TODO Auto-generated method stub
		return null;
	}
}
