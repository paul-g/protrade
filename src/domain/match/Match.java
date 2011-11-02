package src.domain.match;

import org.eclipse.swt.widgets.Composite;

import src.ui.updatable.UpdatableWidget;

public interface Match {    
    public boolean isInPlay();
    public String getName();
    public void registerForUpdate(UpdatableWidget widget, Composite composite);
}
