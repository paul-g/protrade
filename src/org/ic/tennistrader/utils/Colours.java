package org.ic.tennistrader.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Colours {
	private static Color layColor;
	private static Color darkLayColor;
	private static Color lightLayColor;
	private static Color backColor;
	private static Color darkBackColor;
	private static Color lightBackColor;
	private static Color oddsButtonColor;
	private static Color scoreLabelBackgroundColor;
	private static Color themeBasicColor;
	private static Color darkThemeBasicColor;

	public static Color getLayColor() {
		if (layColor == null)
			layColor = new Color(Display.getCurrent(), 238, 210, 238);
		return layColor;
	}
	
	public static Color getDarkLayColor() {
		if (darkLayColor == null)
			darkLayColor = new Color(Display.getCurrent(), 217, 191, 217);
		return darkLayColor;
	}
	
	public static Color getLightLayColor() {
		if (lightLayColor == null)
			lightLayColor = new Color(Display.getCurrent(), 247, 218, 247);
		return lightLayColor;
	}

	public static Color getBackColor() {
		if (backColor == null)
			backColor = new Color(Display.getCurrent(), 198, 226, 255);
		return backColor;
	}
	
	public static Color getDarkBackColor() {
		if (darkBackColor == null)
			darkBackColor = new Color(Display.getCurrent(), 182, 208, 235);
		return darkBackColor;
	}
	
	public static Color getLightBackColor() {
		if (lightBackColor == null)
			lightBackColor = new Color(Display.getCurrent(), 209, 232, 255);
		return lightBackColor;
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

	public static Color getThemeBasicColor() {
		if (themeBasicColor == null)
			themeBasicColor = new Color(Display.getCurrent(), 240, 235, 226);
		return themeBasicColor;
	}
	
	public static Color getDarkThemeBasicColor() {
		if (darkThemeBasicColor == null)
			darkThemeBasicColor = new Color(Display.getCurrent(), 232, 227, 219);
		return darkThemeBasicColor;
	}

}
