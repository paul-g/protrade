package org.ic.protrade.ui.widgets.browser;

import java.util.Stack;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.ui.widgets.MatchViewerWidget;
import org.ic.protrade.ui.widgets.WidgetType;

public class BrowserWidget extends MatchViewerWidget {

	private static Logger log = Logger.getLogger(BrowserWidget.class);

	private Browser browser;

	private final Stack<String> historyBack = new Stack<String>();
	private final Stack<String> historyForward = new Stack<String>();

	public BrowserWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(makeLayout());
		BrowserCoolbar bc = new BrowserCoolbar(this, SWT.NONE);
		bc.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1,
				1));

		try {
			browser = new Browser(this, SWT.NONE);
		} catch (SWTError e) {
			log.error("Could not instantiate Browser: " + e.getMessage());
			return;
		}
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	public void setUrl(String url) {
		String oldUrl = browser.getUrl();
		if (oldUrl != null)
			historyBack.push(oldUrl);
		if (url != null)
			browser.setUrl(url);
	}

	public void back() {
		if (!historyBack.isEmpty())
			setUrl(historyForward.push(historyBack.pop()));
	}

	public void forward() {
		if (!historyForward.isEmpty())
			setUrl(historyBack.push(historyForward.pop()));
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
		return WidgetType.BROWSER;
	}
}
