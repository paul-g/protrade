package org.ic.tennistrader.ui.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ic.tennistrader.ui.widgets.browser.BrowserCoolbar;

public class BrowserToolBar {
	private final ToolBar toolbar;
	private final Shell shell;
	private final BrowserCoolbar parent;

	public BrowserToolBar(BrowserCoolbar parent) {
		shell = parent.getShell();
		this.parent = parent;

		GridData gridData = new GridData(GridData.FILL, GridData.END, true,
				false, 1, 1);

		toolbar = new ToolBar(parent.getCoolBar(), SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		makeBackMenu();
		makeForwardMenu();
		makeRefreshMenu();

	}

	private void makeRefreshMenu() {
		final ToolItem refreshItem = new ToolItem(toolbar, SWT.PUSH);
		refreshItem.setToolTipText("Reload this page");
		refreshItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/refresh.png"));
	}

	private void makeForwardMenu() {
		final ToolItem forwardItem = new ToolItem(toolbar, SWT.PUSH);
		forwardItem.setToolTipText("Click to go forward");
		forwardItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/go-next.png"));
		forwardItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				parent.getBrowser().forward();
			}
		});
	}

	private void makeBackMenu() {
		final ToolItem backItem = new ToolItem(toolbar, SWT.PUSH);
		backItem.setToolTipText("Click to go back");
		backItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/go-back.png"));

		backItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				parent.getBrowser().back();
			}
		});
	}

	public ToolBar getToolbar() {
		return toolbar;
	}

}
