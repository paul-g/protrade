package src.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.Range;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class UpdatableChart extends Chart implements UpdatableWidget {
	private ILineSeries firstSeries;
	//private ILineSeries secondSeries;
	private int sampleSize = 60;
	

	public UpdatableChart(Composite parent, int style) {
		super(parent, style);
		setSeriesStyles();
	}
	
	private void setSeriesStyles() {
		ISeriesSet seriesSet = this.getSeriesSet();		
		// build first series
		firstSeries = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "back odds");		
		// set colours for first series
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		firstSeries.setLineColor(color);
		firstSeries.setSymbolSize(4);
		// build second series
		//secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "MA-Fast");		
		// set series' symbol types
		firstSeries.setSymbolType(PlotSymbolType.CROSS);
		//secondSeries.setSymbolType(PlotSymbolType.DIAMOND);		
	}

	/*
	 * Populates the chart with the given market data
	 */
	public void fillData(MOddsMarketData data) {
		// temporarily for filling charts with random data
		Date[] xSeries; // = new Date[60];
		//double[] ySeries = new double[60];
		//Date[] xSeries2 = new Date[60];
		//double[] ySeries2 = new double[60];
		
		Date[] newXSeries;

		if (firstSeries.getYSeries() != null ){
		  Date[] prevXSeries = firstSeries.getXDateSeries();
		  if ( prevXSeries.length < sampleSize - 1 ) {
		    newXSeries = new Date[prevXSeries.length];
		    int i;
	      for (i = 0;i<prevXSeries.length;i++)
	        newXSeries[i] = prevXSeries[i];
	      newXSeries[i] = Calendar.getInstance().getTime();
		  } else {
		    newXSeries = new Date[sampleSize];
	      int i = 1;
	      for (;i<prevXSeries.length;i++)
	        newXSeries[i-1] = prevXSeries[i];
	      newXSeries[i] = Calendar.getInstance().getTime(); 
		  }
		} else {
		  newXSeries = new Date[1];
		  newXSeries[0] = Calendar.getInstance().getTime();
		}
		
	/*	
		if (firstSeries.getYSeries() != null) {
		  xSeries = firstSeries.getXDateSeries();
		 // xSeries2 = secondSeries.getXDateSeries();
		  ySeries = firstSeries.getYSeries();
		  //ySeries2 = secondSeries.getYSeries();
		  
		  int i;
		  for (i = 0 ; i < 60; i++) {
		    if (ySeries[i] < 0) break;
		  }
      xSeries[i] = Calendar.getInstance().getTime();
		  // xSeries[i] = xSeries2[i] = Calendar.getInstance().getTime();
		  if (data.getPl1Back() != null)
		    ySeries[i] = data.getPl1Back().get(0).getI();
		  else 
		    ySeries[i] = 2;
		  // testing values
		  //ySeries2[i] = (Math.cos(i) + 2) / 2;
		  
		} else  {
		  Random randomGenerator = new Random();
	    Calendar calendar = Calendar.getInstance();   
	    // set random values on the graphs, one every 2 seconds
	    for (int i = 0; i < 60; i++) {
	      if ( i < 10) {
	        //calendar.add(Calendar.MILLISECOND, 2000); 
          xSeries[i] = calendar.getTime();	        
	        //xSeries[i] = xSeries2[i] = calendar.getTime();
	        ySeries[i] = randomGenerator.nextDouble() + 1;
	       // ySeries2[i] = (Math.cos(i) + 2) / 2;
	      }
	      else {
	        xSeries[i] = calendar.getTime();
	        //xSeries[i] = xSeries2[i] = calendar.getTime();
          ySeries[i] = -1;
          //ySeries2[i] = -1;
	      }
	    }  
		}
		*/
		
		// set first series values		
		firstSeries.setXDateSeries(newXSeries);
		//firstSeries.setYSeries(ySeries);
		// set second series values
		/*secondSeries.setXDateSeries(xSeries2);
		secondSeries.setYSeries(ySeries2);*/		
		this.getAxisSet().adjustRange();
	}

	/*
	 * updates the chart with the new given market data
	 */
	public void handleUpdate(final MOddsMarketData newData) {
		// fillData(newData);
		final Composite comp = this.getParent();
		final UpdatableChart chart = this;
		if (comp != null) {
			comp.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					fillData(newData);
					if (!chart.isDisposed())
						chart.redraw();
					if (!comp.isDisposed())
						comp.update();
					//comp.getDisplay().timerExec(1000, this);
				}
			});
		}
	}

}
