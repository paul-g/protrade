package src.domain;

public interface UpdatableWidget {
	/*
	 * updates the widget with the new given market data
	 */
	public void handleUpdate(MOddsMarketData newData);
}
