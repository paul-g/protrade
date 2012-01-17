package org.ic.protrade.ui.score;

import org.eclipse.swt.events.DisposeListener;
import org.ic.protrade.data.market.MOddsMarketData;
import org.ic.protrade.data.match.PlayerEnum;
import org.ic.protrade.ui.updatable.UpdatableWidget;

public interface ScorePanel extends UpdatableWidget {

	public abstract void setScores();

	public abstract void setServer(PlayerEnum player);

	public abstract PlayerEnum getServer();

	@Override
	public abstract void handleUpdate(MOddsMarketData newData);

	@Override
	public abstract void setDisposeListener(DisposeListener listener);

}