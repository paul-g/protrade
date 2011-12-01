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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;



public class LowerToolBar{
	
	private ToolBar toolbar;
	private ProgressBar usage;
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

		/* Internet availability */
		final ToolItem widgetItem = new ToolItem(toolbar, SWT.POP_UP);
		widgetItem.setToolTipText("Internet Connection");
		final Image off = new Image(display, "images/connection_lost.png");
		final Image on = new Image(display, "images/connection_on.png");
		widgetItem.setImage(on);
		
		/* Memory usage bar */
		usage = new ProgressBar(shell, SWT.SMOOTH);
    	Label name = new Label(shell, SWT.NULL);
    	name.setText("Memory Usage");
    	name.setAlignment(SWT.RIGHT);
    	name.setBounds(10,10,80,20);
    	usage.setBounds(90, 10, 200, 20);
		shell.open();
		
		createAndStartNetworkCheckThread(shell, widgetItem, off, on);
		createUsageBarCheck(shell, usage);
	}

	/** Method invoking the Internet check thread */
    private void createAndStartNetworkCheckThread(final Shell shell,
            final ToolItem widgetItem, final Image off, final Image on) {
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

    
    /** Method invoking the memory usage check */
    private void createUsageBarCheck (final Shell shell, final ProgressBar usage) {
    	new Thread(new Runnable() {
			public void run() {
				while (!stop) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
					if (!shell.isDisposed()) {
						toolbar.getDisplay().asyncExec(new Runnable() {
							public void run() {
								double max = (double) Runtime.getRuntime().maxMemory();
								double fraction = max - (double) Runtime.getRuntime().freeMemory();
								double selection = (fraction / max) * 100;
								int selection_int = (int) (selection);
						    	usage.setSelection(selection_int);
							}
						});
					}
				}
			}
		}).start();
    }
	
	/** Thread stopping value */
	public static void setStop() {
		stop = true;
	}
	
	/** Method checking the Internet availability */
	public boolean isInternetReachable() {
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
