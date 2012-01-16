package org.ic.protrade.ui.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.protrade.ui.GraphicsUtils;

public class StandardWidgetResizeListener implements Listener{

    private Composite comp;
    
    public StandardWidgetResizeListener(Composite comp){
        this.comp = comp;
    }
    
    @Override
    public void handleEvent(Event arg0) {
        comp.setBackgroundImage(GraphicsUtils.makeDefaultBackgroundImage(comp));
    }
}
