package src.ui;

import java.util.Random;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;


public class ChartDrawer {
	
	static Chart chart = null;	
	static double [] xSeries = new double[60];
	static double [] ySeries = new double[60];
	static double [] xSeries2 = new double[60];
	static double [] ySeries2 = new double[60];
	static int i = 0;
	  
	public ChartDrawer(Composite panel,String title){
		chart = new Chart(panel,SWT.NONE);
		chart.setSize(50, 100);
		chart.getTitle().setText(title);
		chart.getAxisSet().getXAxis(0).getTitle().setText("Time in minutes");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Back Odds on Nadal");
	}
	
	
	public static void showChart(){
		Random randomGenerator = new Random();
		for (int i = 0; i < 60; i++){
			xSeries[i] = i;
			xSeries2[i] = i;
			ySeries[i] = randomGenerator.nextDouble() + 1;
			ySeries2[i] = (Math.cos(i) + 2 )/ 2;
		}
		ISeriesSet seriesSet = chart.getSeriesSet();
		ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "back odds");
		series.setXSeries(xSeries);
		series.setYSeries(ySeries);
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		series.setLineColor(color);
		series.setSymbolSize(4);
		ILineSeries series2 = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "MA-Fast");
		series2.setXSeries(xSeries2);
		series2.setYSeries(ySeries2);
		series.setSymbolType(PlotSymbolType.CROSS);
		series2.setSymbolType(PlotSymbolType.DIAMOND);
		final IAxisSet axisSet = chart.getAxisSet();	
		
		Composite composite = chart.getPlotArea();
		composite.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event event) {
				for (int i=0; i<10; i++){
					axisSet.zoomIn();
				}
			}
		});
		axisSet.adjustRange();
		
	}
}
