package org.ic.tennistrader.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
	private static Color layColor;
	private static Color backColor;
	private static Color oddsButtonColor;
	private static Color scoreLabelBackgroundColor;

	public static Color getLayColor() {
		if (layColor == null)
			layColor = new Color(Display.getCurrent(), 238, 210, 238);
		return layColor;
	}

	public static Color getBackColor() {
		if (backColor == null)
			backColor = new Color(Display.getCurrent(), 198, 226, 255);
		return backColor;
	}

	public static Color getOddsButtonColor() {
		if (oddsButtonColor == null)
			oddsButtonColor = new Color(Display.getCurrent(), 240, 240, 240);
		return oddsButtonColor;
	}

	public static Color getScoreLabelBackgroundColor() {
		if (scoreLabelBackgroundColor == null)
			scoreLabelBackgroundColor = new Color(Display.getCurrent(), 19, 79,
					9);
		return scoreLabelBackgroundColor;
	}

}
