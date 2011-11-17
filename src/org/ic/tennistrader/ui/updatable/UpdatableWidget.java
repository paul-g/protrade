package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.MOddsMarketData;

public interface UpdatableWidget {
	/*
	 * updates the widget with the new given market data
	 */
	public void handleUpdate(MOddsMarketData newData);
	
	// adds the given listener when the widget is disposed
	public void adddisposeListener(Listener listener);
}
