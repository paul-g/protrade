package src.domain.match;

import org.eclipse.swt.widgets.Composite;

import src.ui.updatable.UpdatableWidget;

public interface Match {    
    public boolean isInPlay();
    public String getName();
    public String getPlayer1();
    public String getPlayer2();
    
    public void registerForUpdate(UpdatableWidget widget, Composite composite);
}
