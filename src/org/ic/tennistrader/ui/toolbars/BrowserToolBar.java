package org.ic.tennistrader.ui.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class BrowserToolBar {
	private final ToolBar toolbar;
	private final Shell shell;
	
	public BrowserToolBar(Composite parent){
		shell = parent.getShell();
		
		GridData gridData = new GridData(GridData.FILL, GridData.END, true,
				false, 1, 1);

		toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);
		
		makeBackMenu();
		makeForwardMenu();
		makeRefreshMenu();
		
	}

	private void makeRefreshMenu() {
		final ToolItem refreshItem = new ToolItem(toolbar, SWT.PUSH);
		refreshItem.setToolTipText("Reload this page");
		refreshItem.setImage(new Image(shell.getDisplay(),"images/toolbar/refresh.png"));
	}

	private void makeForwardMenu() {
		final ToolItem forwardItem = new ToolItem(toolbar, SWT.PUSH);
		forwardItem.setToolTipText("Click to go forward");
		forwardItem.setImage(new Image(shell.getDisplay(),"images/toolbar/go-next.png"));
	}

	private void makeBackMenu() {
		final ToolItem backItem = new ToolItem(toolbar, SWT.PUSH);
		backItem.setToolTipText("Click to go back");
		backItem.setImage(new Image(shell.getDisplay(),"images/toolbar/go-back.png"));
	}

	public ToolBar getToolbar() {
		return toolbar;
	}
	
	
}
