package org.ic.protrade.ui.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.swtchart.ILineSeries.PlotSymbolType;

public class LineProp {
	private Color color;
	private boolean step;
	private boolean antialias;
	private boolean area;
	private int barPadding;
	private int yAxisId;
	private PlotSymbolType symbolType;

	LineProp() {
		this.color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		this.step = true;
		this.antialias = true;
		this.area = false;
		this.barPadding = 100;
		this.yAxisId = 0;
		this.symbolType = PlotSymbolType.NONE;
	}

	public Color getColor() {
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

	public void setBarPadding(int barPadding) {
		this.barPadding = barPadding;
	}

	public int getBarPadding() {
		return barPadding;
	}

	public void setyAxisId(int yAxisId) {
		this.yAxisId = yAxisId;
	}

	public int getyAxisId() {
		return yAxisId;
	}

	public void setSymbolType(PlotSymbolType symbolType) {
		this.symbolType = symbolType;
	}

	public PlotSymbolType getSymbolType() {
		return symbolType;
	}
}
