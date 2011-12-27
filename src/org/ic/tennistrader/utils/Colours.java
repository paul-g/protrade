package org.ic.tennistrader.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
	private static Color layColor;
	private static Color backColor;
	private static Color oddsButtonColor;
	
	public static Color getLayColor(Display display) {
		if (layColor == null)
			layColor = new org.eclipse.swt.graphics.Color(display, 238, 210, 238);		
		return layColor;
	}

	public static Color getBackColor(Display display) {
		if (backColor == null)
			backColor = new org.eclipse.swt.graphics.Color(display, 198, 226, 255);
		return backColor;
	}

	public static Color getOddsButtonColor(Display display) {
		if (oddsButtonColor == null)
			oddsButtonColor = new org.eclipse.swt.graphics.Color(display, 240, 240,
					240);
		return oddsButtonColor;
	}
	
}
