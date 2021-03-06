package org.ic.protrade.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class GraphicsUtils {
    
    public static Image makeGradientBackgroundImage(Composite comp, int red1,
            int green1, int blue1, int red2, int green2, int blue2) {

        final Display display = comp.getDisplay();
        final Color foregroundColor = new org.eclipse.swt.graphics.Color(
                display, red1, green1, blue1);
        final Color backgroundColor = new org.eclipse.swt.graphics.Color(
                display, red2, green2, blue2);
        
        return makeGradientBackgroundImage(comp, foregroundColor, backgroundColor);
    }
    
    public static Image makeGradientBackgroundImage(Composite comp, Color foregroundColor, Color backgroundColor){

        final Display display = comp.getDisplay();
        
        final Rectangle rect = comp.getClientArea();
        
        Image image = new Image(display, rect.width, rect.height);
        
        GC gc = new GC(image);
        gc.setForeground(foregroundColor);
        gc.setBackground(backgroundColor);
        gc.fillGradientRectangle(rect.x, rect.y, rect.width, rect.height / 2,
                true);

        gc.setForeground(backgroundColor);
        gc.setBackground(foregroundColor);
        gc.fillGradientRectangle(rect.x, rect.height / 2, rect.width,
                rect.height, true);
        gc.dispose();

        return image;
    }
    
    public static Image makeDefaultBackgroundImage(Composite comp){
        final Color foregroundColor = new org.eclipse.swt.graphics.Color(
                comp.getDisplay(), 143, 143, 143);
        final Color backgroundColor = new org.eclipse.swt.graphics.Color(
                comp.getDisplay(), 168, 168, 168);
        return makeGradientBackgroundImage(comp, foregroundColor, backgroundColor);
    }

}
