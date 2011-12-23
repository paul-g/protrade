package org.ic.tennistrader.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
	public static Color layColor;
	public static Color backColor;
	public static Color oddsButtonColor;
	// The application's current background colour is 238, 238, 224
	
	public static void setColors(Display display) {
		layColor = new org.eclipse.swt.graphics.Color(display, 238, 210, 238);
		backColor = new org.eclipse.swt.graphics.Color(display, 198, 226, 255);
		oddsButtonColor = new org.eclipse.swt.graphics.Color(display, 240, 240,
				240);
	}
}
