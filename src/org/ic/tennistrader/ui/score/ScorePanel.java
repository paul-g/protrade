package org.ic.tennistrader.ui.score;

import org.eclipse.swt.events.DisposeListener;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public interface ScorePanel extends UpdatableWidget{

    public abstract void setScores();

    public abstract void setServer(PlayerEnum player);

    public abstract PlayerEnum getServer();

    public abstract void handleUpdate(MOddsMarketData newData);

    public abstract void setDisposeListener(DisposeListener listener);

}