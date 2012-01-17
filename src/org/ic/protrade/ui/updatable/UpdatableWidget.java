package org.ic.protrade.ui.updatable;

import org.eclipse.swt.events.DisposeListener;
import org.ic.protrade.data.market.MOddsMarketData;

public interface UpdatableWidget {
	/*
	 * updates the widget with the new given market data
	 */
	public void handleUpdate(MOddsMarketData newData);

	/*
	 * updates the widget with the new given market data
	 */
	public void handleBettingMarketEndOFSet();

	// adds the given listener when the widget is disposed
	public void setDisposeListener(DisposeListener listener);
}