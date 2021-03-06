package org.ic.protrade.ui.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public interface MainWindow {

	public abstract Shell show();

	public abstract void run(Display display);

	public abstract void notifyLoadEvent(String name);
	
	public abstract void addLoadListener(Listener l);
	
	public abstract Shell getShell();

	public abstract void dispose();
}