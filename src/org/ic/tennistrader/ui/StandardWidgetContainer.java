package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class StandardWidgetContainer extends Composite {

    public StandardWidgetContainer(Composite arg0, int arg1) {
        super(arg0, arg1);
        this.setBackgroundMode(SWT.INHERIT_DEFAULT);

    }
    
    public String getTitle(){
        // containers only need a title if they'll be added to tabbedcontainer
        return "";
    }
}
