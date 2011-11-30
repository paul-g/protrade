package org.ic.tennistrader.ui.updatable;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.ic.tennistrader.domain.match.Match;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;

public class OverroundChart extends Chart{
	private ILineSeries backOverround;
	private ILineSeries layOverround;
	private IBarSeries pl1Volume;
	private IBarSeries pl2Volume;
	private boolean isBackOverround;
	private boolean isLayOverround;
	
	public OverroundChart(Composite parent, int style, GridData gridData, Match match) {
		super(parent, style);
		this.setLayoutData(gridData);
		this.getTitle().setVisible(false);
		createSeries(match);
		setBackOverround(true);
		this.getLegend().setPosition(SWT.BOTTOM);
		this.getAxisSet().getYAxis(0).getTitle().setText("Overround");
		this.getAxisSet().getXAxis(0).getTitle().setVisible(false);
	}



	private void createSeries(Match match) {
		ISeriesSet seriesSet = this.getSeriesSet();
		backOverround = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "back overround");
		Color color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		backOverround.setLineColor(color);
		layOverround = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "lay overround");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		layOverround.setLineColor(color);
		pl1Volume = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, match.getPlayerOne() +  " Volume");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		pl1Volume.setBarColor(color);
		pl2Volume = (IBarSeries) seriesSet.createSeries(SeriesType.BAR, match.getPlayerTwo() + " Volume");
		color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		pl2Volume.setBarColor(color);
		layOverround.setVisible(false);
		pl1Volume.setVisible(false);
		pl2Volume.setVisible(false);
	}
	
	public void visibility(boolean pl1, boolean pl2){
		if (isBackOverround || isLayOverround){
			this.getAxisSet().getYAxis(0).getTitle().setText("Overround");
			pl1Volume.setVisible(false);
			pl2Volume.setVisible(false);
			backOverround.setVisible(isBackOverround());
			layOverround.setVisible(isLayOverround());
		} else {
			this.getAxisSet().getYAxis(0).getTitle().setText("Volume");
			backOverround.setVisible(false);
			layOverround.setVisible(false);
			pl1Volume.setVisible(pl1);
			pl2Volume.setVisible(pl2);
		}
		
	}
	
	
	public void setPl1Volume(Date[] xData, double[] yData){
		setPl1VolumeX(xData);
		setPl1VolumeY(yData);
	}
	
	public void setPl1VolumeX(Date[] xData){
		pl1Volume.setXDateSeries(xData);
	}
	
	public void setPl1VolumeY(double[] yData){
		pl1Volume.setYSeries(yData);
	}
	
	public void setPl2Volume(Date[] xData, double[] yData){
		setPl2VolumeX(xData);
		setPl2VolumeY(yData);
	}
	
	public void setPl2VolumeX(Date[] xData){
		pl2Volume.setXDateSeries(xData);
	}
	
	public void setPl2VolumeY(double[] yData){
		pl2Volume.setYSeries(yData);
	}
	
	
	public void setLayOverround(Date[] xData, double[] yData){
		setLayOverroundX(xData);
		setLayOverroundY(yData);
	}
	
	public void setLayOverroundX(Date[] xData){
		layOverround.setXDateSeries(xData);
	}
	
	public void setLayOverroundY(double[] yData){
		layOverround.setYSeries(yData);
	}
	public void setBackOverround(Date[] xData, double[] yData){
		setBackOverroundX(xData);
		setBackOverroundY(yData);
	}
	
	public void setBackOverroundX(Date[] xData){
		backOverround.setXDateSeries(xData);
	}
	
	public void setBackOverroundY(double[] yData){
		backOverround.setYSeries(yData);
	}

	public boolean isBackOverround() {
		return isBackOverround;
	}



	public void setBackOverround(boolean isBackOverround) {
		this.isBackOverround = isBackOverround;
	}



	public boolean isLayOverround() {
		return isLayOverround;
	}



	public void setLayOverround(boolean isLayOverround) {
		this.isLayOverround = isLayOverround;
	}



	public void adjust() {
		this.getAxisSet().adjustRange();
		
	}

}
