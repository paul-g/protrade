package org.ic.tennistrader.ui.updatable;

import org.ic.tennistrader.domain.MOddsMarketData;

public interface UpdatableWidget {
	/*
	 * updates the widget with the new given market data
	 */
	public void handleUpdate(MOddsMarketData newData);
}