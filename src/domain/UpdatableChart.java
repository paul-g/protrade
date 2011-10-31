package src.domain;

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

public class UpdatableChart extends Chart implements UpdatableWidget {
    private ILineSeries firstSeries;
    // private ILineSeries secondSeries;
    private int sampleSize = 30;

    public UpdatableChart(Composite parent, int style) {
        super(parent, style);
        setSeriesStyles();
    }

    private void setSeriesStyles() {
        ISeriesSet seriesSet = this.getSeriesSet();
        // build first series
        firstSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                "back odds");
        // set colours for first series
        Color color = new Color(Display.getDefault(), 255, 0, 0);
        firstSeries.setLineColor(color);
        firstSeries.setSymbolSize(4);
        // build second series
        // secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
        // "MA-Fast");
        // set series' symbol types
        firstSeries.setSymbolType(PlotSymbolType.CROSS);
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
        if (data.getPl1Back() != null)
            newYSeries[i] = data.getPl1Back().get(0).getI();
        else
            if ( i > 0 ) // keep previous value if it exists
                newYSeries[i] = newYSeries[i-1];
            else // put zero if no previous value
                newYSeries[i] = 0;

        // set first series values
        firstSeries.setXDateSeries(newXSeries);
        firstSeries.setYSeries(newYSeries);
        if (!this.isDisposed())
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
                }
            });
        }
    }

}
