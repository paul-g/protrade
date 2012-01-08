package org.ic.tennistrader.ui.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class VideoPlayerToolBar {
	private final ToolBar toolbar;
	private final Shell shell;

	public VideoPlayerToolBar(Composite parent) {
		shell = parent.getShell();

		GridData gridData = new GridData(GridData.FILL, GridData.END, true,
				false, 1, 1);

		toolbar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		makeStopMenu();
		makeBackwardMenu();
		makePlayMenu();
		makeForwardMenu();
	}

	private void makeForwardMenu() {
		final ToolItem forwardItem = new ToolItem(toolbar, SWT.PUSH);
		forwardItem.setToolTipText("Next");
		forwardItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/seek-forward.png"));		
	}

	private void makePlayMenu() {
		final ToolItem playItem = new ToolItem(toolbar, SWT.PUSH);
		playItem.setToolTipText("Play");
		playItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/start.png"));
	}

	private void makeBackwardMenu() {
		final ToolItem playItem = new ToolItem(toolbar, SWT.PUSH);
		playItem.setToolTipText("Previous");
		playItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/seek-backward.png"));
	}

	private void makeStopMenu() {
		final ToolItem stopItem = new ToolItem(toolbar, SWT.PUSH);
		stopItem.setToolTipText("Stop");
		stopItem.setImage(new Image(shell.getDisplay(),
				"images/toolbar/stop.png"));
	}

	
	public ToolBar getToolbar() {
		return toolbar;
	}

}
