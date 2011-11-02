package src.ui.updatable;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import src.domain.MOddsMarketData;

public class UpdatableChart extends Chart implements UpdatableWidget {
    private ILineSeries firstSeries;
    // private ILineSeries secondSeries;
    private int sampleSize = 80;
    private boolean decimalOdds;
    private String xAxisTitle = "Time";
    private String yAxisDecimalTitle = "Decimal Odds";
    private String yAxisFractionalTitle = "Fractional Odds";
    
    public UpdatableChart(Composite parent, int style) {
        super(parent, style);
        setSeriesStyles();
        decimalOdds = true;
        this.getAxisSet().getXAxis(0).getTitle().setText(xAxisTitle);
        this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
    }

    private void setSeriesStyles() {
        ISeriesSet seriesSet = this.getSeriesSet();
        // build first series
        firstSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                "back odds");
        Color color = new Color(Display.getDefault(), 255, 0, 0);
        firstSeries.setLineColor(color);
        firstSeries.setSymbolSize(4);
        firstSeries.setSymbolType(PlotSymbolType.CROSS);
        
        // build second series
        // secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
        // "MA-Fast");        
        // secondSeries.setSymbolType(PlotSymbolType.DIAMOND);
    }

    /*
     * Populates the chart with the given market data
     */
    public void fillData(MOddsMarketData data) {
        Date[] newXSeries;
        double[] newYSeries;        
        int i = 0;
        // if graph already displaying values
        if (firstSeries.getYSeries() != null) {
            Date[] prevXSeries = firstSeries.getXDateSeries();
            double[] prevYSeries = firstSeries.getYSeries();
            // if not reached max sample size
            if (prevXSeries.length < sampleSize) {
                newXSeries = new Date[prevXSeries.length + 1];
                newYSeries = new double[prevXSeries.length + 1];
                for (i = 0; i < prevXSeries.length; i++){
                    newXSeries[i] = prevXSeries[i];
                    newYSeries[i] = prevYSeries[i];
                }
            } else { // discard least recent value
                newXSeries = new Date[sampleSize];
                newYSeries = new double[sampleSize];
                for (i = 1; i < sampleSize; i++) {
                    newXSeries[i - 1] = prevXSeries[i];
                    newYSeries[i - 1] = prevYSeries[i];
                }
                i--;
            }
        } else {
            newXSeries = new Date[1];
            newYSeries = new double[1];
        }
        
        newXSeries[i] = Calendar.getInstance().getTime();
        // if data has been read from Betfair
        if (data.getPl1Back() != null && data.getPl1Back().size() > 0)
            newYSeries[i] = data.getPl1Back().get(0).getI();
        else
            if ( i > 0 ) // keep previous value if it exists
                newYSeries[i] = newYSeries[i-1];
            else // put zero if no previous value
                newYSeries[i] = 0;

        // convert to fractional odds if necessary
        if (!decimalOdds)
        	newYSeries[i]--;
        
        // set first series values
        firstSeries.setXDateSeries(newXSeries);
        firstSeries.setYSeries(newYSeries);
        if (!this.isDisposed())
        	this.getAxisSet().adjustRange();
    }
    
    public void invertAxis() {    	
    	int changeValue = decimalOdds ? (-1) : 1;
    	decimalOdds = !decimalOdds;
    	if (decimalOdds)
    		this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
    	else
    		this.getAxisSet().getYAxis(0).getTitle().setText(yAxisFractionalTitle);
    	
    	resetOddValues(changeValue);
    	updateDisplay();  
    }

	private void resetOddValues(int changeValue) {
		if (firstSeries.getYSeries() != null) {
            double[] ySeries = firstSeries.getYSeries();
            for (int i = 0; i < ySeries.length; i++)
            	ySeries[i] += changeValue;
            firstSeries.setYSeries(ySeries);
    	}		
	}

    /*
     * updates the chart with the new given market data
     */
    public void handleUpdate(final MOddsMarketData newData) {
    	fillData(newData);
        updateDisplay();
    }

	private void updateDisplay() {
		final Composite comp = this.getParent();
        final UpdatableChart chart = this;
        if (comp != null) {
            comp.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {                    
                    if (!chart.isDisposed())
                        chart.redraw();
                    if (!comp.isDisposed())
                        comp.update();
                }
            });
        }
	}

}
