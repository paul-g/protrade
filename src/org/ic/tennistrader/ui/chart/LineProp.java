package org.ic.tennistrader.ui.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class LineProp {
	private Color color;
	private boolean step;
	private boolean antialias;
	private boolean area;

	LineProp() {
		this.color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		this.step = true;
		this.antialias = true;
		this.area = false;
	}

	public Color getColor() {
		System.out.println(color);
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isStep() {
		return step;
	}

	public void setStep(boolean step) {
		this.step = step;
	}

	public boolean isAntialias() {
		return antialias;
	}

	public void setAntialias(boolean antialias) {
		this.antialias = antialias;
	}

	public boolean isArea() {
		return area;
	}

	public void setArea(boolean area) {
		this.area = area;
	}
}
