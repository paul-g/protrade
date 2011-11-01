package src.ui.updatable;

import src.domain.MOddsMarketData;

public interface UpdatableWidget {
	/*
	 * updates the widget with the new given market data
	 */
	public void handleUpdate(MOddsMarketData newData);
}
