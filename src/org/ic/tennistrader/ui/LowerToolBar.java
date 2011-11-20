package org.ic.tennistrader.ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;



public class LowerToolBar {
	
	private ToolBar toolbar;
	private static boolean stop = false;
	private static Logger log = Logger.getLogger(LowerToolBar.class);
	
	public LowerToolBar(final MainWindow mainWindow) {

		final Shell shell = mainWindow.getShell();
		// Setting span throughout the columns
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		Display display = shell.getDisplay();

		this.toolbar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolbar.setLayoutData(gridData);

		final ToolItem widgetItem = new ToolItem(toolbar, SWT.POP_UP);
		widgetItem.setToolTipText("Internet Connection");
		final Image off = new Image(display, "images/connection_lost.png");
		final Image on = new Image(display, "images/connection_on.png");
		widgetItem.setImage(on);
		new Thread(new Runnable() {
			public void run() {
				while (!stop) {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
					}
					if (!shell.isDisposed()) {
						toolbar.getDisplay().asyncExec(new Runnable() {
							public void run() {
								if (isInternetReachable()) {
									log.info("Connection - ON");
									widgetItem.setImage(on);
								} else {
									log.info("Connection - OFF");
									widgetItem.setImage(off);
								}
							}
						});
					}
				}
			}
		}).start();
	}
	
	public static void setStop() {
		stop = true;
	}
	
	private boolean isInternetReachable() {
		try {
			// URL to a source
			URL url = new URL("http://www.google.com");
			// Open a connection
			HttpURLConnection urlConnect = (HttpURLConnection) url
					.openConnection();
			// Retrieving data from the source - if there is no connection,
			// throws and exception
			@SuppressWarnings("unused")
			Object objData = urlConnect.getContent();
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
