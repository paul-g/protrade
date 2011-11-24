package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class StandardWidgetContainer extends Composite {

    public StandardWidgetContainer(Composite arg0, int arg1) {
        super(arg0, arg1);
        this.setBackgroundMode(SWT.INHERIT_DEFAULT);

        this.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                StandardWidgetContainer.this.setBackgroundImage(GraphicsUtils
                        .makeDefaultBackgroundImage(StandardWidgetContainer.this));
            }

        });
    }
}
