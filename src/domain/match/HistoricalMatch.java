package src.domain.match;

import org.eclipse.swt.widgets.Composite;

import src.service.LiveDataFetcher;
import src.ui.updatable.UpdatableWidget;

public class HistoricalMatch implements Match {
    
    String filename;
    
    public HistoricalMatch(String filename){
        this.filename = filename;
    }

    @Override
    public boolean isInPlay() {
        return false;
    }

    @Override
    public String getName() {
        return "History";
    }

    @Override
    public void registerForUpdate(UpdatableWidget widget, Composite composite) {
        LiveDataFetcher.registerFromFile(widget, this, filename, composite);
    }
}
