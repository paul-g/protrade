package org.ic.tennistrader.ui.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.swtchart.ILineSeries;

public class LineProp {
	private Color color;
	private boolean step;
	private boolean antialias;
	private boolean area;

	LineProp(ILineSeries line) {
		this.color = line.getLineColor();
		System.out.println(this.color);
		this.step = line.isStepEnabled();
		if (line.getAntialias() == SWT.ON)
			this.antialias = true;
		else
			this.antialias = false;
		this.area = line.isAreaEnabled();
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
