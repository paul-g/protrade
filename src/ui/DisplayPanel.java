package src.ui;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class DisplayPanel implements Listener{
  
  final CTabFolder folder;
  ArrayList<Chart> charts;

  public DisplayPanel(Composite parent){
    folder = new CTabFolder(parent, SWT.RESIZE | SWT.BORDER);
    folder.setSimple(false);
    folder.setMinimizeVisible(true);
    folder.setMaximizeVisible(true);
    
    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.FILL;
    gridData.horizontalSpan = 2;
    gridData.verticalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace   = true;
    
    folder.setLayoutData(gridData);
    
    charts = new ArrayList<Chart>();
  }

  public void handleEvent(Event event) {
    TreeItem ti = (TreeItem) event.item;
    
    CTabItem[] items = folder.getItems();
    int pos = -1;
    
    for (int i=0;pos==-1 && i<items.length;i++) 
      if (items[i].getText().equals(ti.getText())){
    	  pos = i;
    	  
      }

    if (pos==-1) {
      CTabItem item = new CTabItem(folder, SWT.CLOSE);
      item.setText(ti.getText());
      Text text = new Text(folder, SWT.NONE);
      text.setText("Tab content for " + ti.getText());
      item.setControl(text);
      folder.setSelection(item);
      Chart chart = new Chart(folder,SWT.NONE);
      chart.getTitle().setText(ti.getText());
      charts.add(chart);
      item.setControl(chart);
      // temporarily for filling charts with random data
  	  double [] xSeries = new double[60];
	  double [] ySeries = new double[60];
	  double [] xSeries2 = new double[60];
	  double [] ySeries2 = new double[60];
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
		axisSet.adjustRange();
      // end filling charts
      
    } else 
     folder.setSelection(pos);
    
  }
  
  public void addTab(String text){
    CTabItem cti =  new CTabItem(folder, SWT.CLOSE);
    folder.setSelection(cti);
  }
  
}
