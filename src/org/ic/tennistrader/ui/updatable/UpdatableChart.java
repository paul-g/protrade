package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Slider;
import org.swtchart.Chart;
import org.swtchart.IErrorBar.ErrorBarType;
import org.swtchart.IErrorBar;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.ChartData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.ui.GraphicsUtils;
import org.ic.tennistrader.ui.StandardWidgetResizeListener;

public class UpdatableChart extends Chart implements UpdatableWidget {
    private ILineSeries firstSeries;
    private ILineSeries secondSeries;
    private ILineSeries maPl1Series;
    private ILineSeries maPl2Series;
    private IErrorBar pl1Spread;
    private IErrorBar pl2Spread;
    private int sampleSize = 200;
    private int seriesNr = 8;
    private boolean decimalOdds;
    private String xAxisTitle = "Time";
    private String yAxisDecimalTitle = "Decimal Odds";
    private String yAxisFractionalTitle = "Implied Odds";
    private boolean pl1Selected;
    private boolean pl2Selected;
    private boolean maPl2Selected;
    private boolean maPl1Selected;
    private boolean spPl1Selected;
    private boolean spPl2Selected;
    private ChartData chartData;
    private Match match;
    private Slider slider;

    public UpdatableChart(Composite parent, int style, Match match,
            Slider slider) {
        super(parent, style);
        chartData = new ChartData();
        createSlider(slider);
        this.match = match;
        setSeriesStyles();
        decimalOdds = true;
        pl1Selected = true;
        this.getAxisSet().getXAxis(0).getTitle().setText(xAxisTitle);
        this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
        this.getTitle().setText(match.getName());
        makeMenus(parent);
        this.getLegend().setPosition(SWT.TOP);
        // addListeners();
        this.getAxisSet().adjustRange();
        this.setBackgroundMode(SWT.INHERIT_DEFAULT);
        this.addListener(SWT.Resize, new StandardWidgetResizeListener(this));
    }

    private void addListeners() {
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseScrolled(MouseEvent e) {
                int units = e.count;
                // IAxis yAxis = getAxisSet().getYAxis(0);
                // System.out.println(e.x + " " + e.y);
                if (units > 0) {
                    // Rotating forward
                    getAxisSet().zoomIn();
                    // getAxisSet().getXAxis(0).zoomIn();
                } else {
                    // Rotating backward
                    getAxisSet().zoomOut();
                    // getAxisSet().getXAxis(0).zoomOut();
                }
            }
        });

    }

    private void createSlider(Slider slider) {
        this.slider = slider;
        slider.setMaximum(1);
        slider.setSelection(0);

        slider.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                Slider slider = (Slider) event.widget;
                if (slider.getMaximum() > sampleSize)
                    showSeries(slider.getSelection(), true);
            }
        });
    }

    private void setSeriesStyles() {

        ISeriesSet seriesSet = this.getSeriesSet();
        // build first series
        firstSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                "back odds " + match.getPlayerOne());
        Color color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
        firstSeries.setLineColor(color);
        firstSeries.setSymbolSize(4);
        firstSeries.setSymbolType(PlotSymbolType.CROSS);
        pl1Spread = firstSeries.getYErrorBar();
        pl1Spread.setType(ErrorBarType.BOTH);
        pl1Spread.setVisible(false);
        // build second series
        secondSeries = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                "back odds " + match.getPlayerTwo());
        Color colorSr2 = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
        secondSeries.setLineColor(colorSr2);
        secondSeries.setSymbolSize(4);
        secondSeries.setSymbolType(PlotSymbolType.DIAMOND);
        secondSeries.setVisible(false);
        pl2Spread = secondSeries.getYErrorBar();
        pl2Spread.setType(ErrorBarType.BOTH);
        pl2Spread.setVisible(false);
        // building moving averages player 1
        maPl1Series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                "MA " + match.getPlayerOne());
        Color color3 = Display.getCurrent().getSystemColor(
                SWT.COLOR_DARK_MAGENTA);
        maPl1Series.setLineColor(color3);
        maPl1Series.setSymbolSize(4);
        maPl1Series.setSymbolType(PlotSymbolType.SQUARE);
        maPl1Series.setVisible(false);

        // building moving averages player 2
        maPl2Series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE,
                "MA " + match.getPlayerTwo());
        Color color4 = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
        maPl2Series.setLineColor(color4);
        maPl2Series.setSymbolSize(4);
        maPl2Series.setSymbolType(PlotSymbolType.TRIANGLE);
        maPl2Series.setVisible(false);

    }

    /**
     * Populates the chart with the given market data
     */
    public void fillData(MOddsMarketData data) {
        // add new market data to the data structures
        int i = chartData.getDataSize();
        chartData.updateData(data);

        // update size of the slider and selection based on what user was
        // previously viewing
        updateSlider(chartData.getDataSize());
        // set serieses values
        showSeries(i, false);
        if (!this.isDisposed()) {
            this.getAxisSet().getXAxis(0).adjustRange();
            this.getAxisSet().getYAxis(0).adjustRange();
        }
    }

    private void updateSlider(int i) {
        if (i >= sampleSize) {
            if (slider.getSelection() == slider.getMaximum() - 1) {
                slider.setMaximum(i + 1);
                slider.setSelection(i);
            }
            slider.setMaximum(i + 1);
            if (i == sampleSize)
                slider.setMinimum(sampleSize - 1);
            updateSlide();
        } else {
            slider.setMaximum(i + 1);
            slider.setMinimum(i);
            slider.setSelection(i);

        }

    }

    public void showSeries(int i, boolean dragged) {
        int size = (i + 1) < sampleSize ? (i + 1) : sampleSize;
        Date showXSeries[] = new Date[size];
        ArrayList<double[]> dataArray = new ArrayList<double[]>();
        for (int k = 0; k < seriesNr; k++) {
            dataArray.add(k, new double[size]);
        }
        int z = i < sampleSize ? 0 : 1;
        if (slider.getMaximum() == slider.getSelection() + 1 || dragged) {
            // variables for updating series according decimal/implied setting
            int pow = 1;
            int k = 1;
            if (!decimalOdds) {
                pow = -1;
                k = 100;
            }

            for (int a = 0; a < size; a++) {
                int nr = 0;
                int b = (i - sampleSize + 1) * z + a;
                showXSeries[a] = chartData.getxSeries().get(b);
                dataArray.get(nr)[a] = Math.pow(chartData.getPl1YSeries()
                        .get(b), pow)
                        * k;
                nr++;
                dataArray.get(nr)[a] = Math.pow(chartData.getPl2YSeries()
                        .get(b), pow)
                        * k;
                nr++;
                dataArray.get(nr)[a] = Math.pow(chartData.getMaPl1().get(b),
                        pow) * k;
                nr++;
                dataArray.get(nr)[a] = Math.pow(chartData.getMaPl2().get(b),
                        pow) * k;
                nr++;
                dataArray.get(nr)[a] = (Math.pow(chartData.getPl1Lay().get(b)
                        .first(), pow) - Math.pow(chartData.getPl1YSeries()
                        .get(b), pow))
                        * k;
                nr++;
                dataArray.get(nr)[a] = (Math.pow(
                        chartData.getPl1YSeries().get(b), pow) - Math.pow(
                        chartData.getPl1Lay().get(b).second(), pow)) * k;
                nr++;
                dataArray.get(nr)[a] = (Math.pow(chartData.getPl2Lay().get(b)
                        .first(), pow) - Math.pow(chartData.getPl2YSeries()
                        .get(b), pow))
                        * k;
                nr++;
                dataArray.get(nr)[a] = (Math.pow(
                        chartData.getPl2YSeries().get(b), pow) - Math.pow(
                        chartData.getPl2Lay().get(b).second(), pow)) * k;
            }

            firstSeries.setXDateSeries(showXSeries);
            firstSeries.setYSeries(dataArray.get(0));
            secondSeries.setXDateSeries(showXSeries);
            secondSeries.setYSeries(dataArray.get(1));
            maPl1Series.setXDateSeries(showXSeries);
            maPl1Series.setYSeries(dataArray.get(2));
            maPl2Series.setXDateSeries(showXSeries);
            maPl2Series.setYSeries(dataArray.get(3));
            pl1Spread.setPlusErrors(dataArray.get(4));
            pl1Spread.setMinusErrors(dataArray.get(5));
            pl2Spread.setPlusErrors(dataArray.get(6));
            pl2Spread.setMinusErrors(dataArray.get(7));
            updateDisplay();
        }
    }

    // switches between the two odds representations
    public void invertAxis() {
        decimalOdds = !decimalOdds;
        if (decimalOdds)
            this.getAxisSet().getYAxis(0).getTitle().setText(yAxisDecimalTitle);
        else
            this.getAxisSet().getYAxis(0).getTitle()
                    .setText(yAxisFractionalTitle);

        showSeries(slider.getSelection(), true);
        updateDisplay();
    }

    /**
     * updates the chart with the new given market data
     */
    public void handleUpdate(final MOddsMarketData newData) {
        // final Composite comp = this.getParent();
        final UpdatableChart chart = this;
        if (chart != null) {
            chart.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    fillData(newData);
                    if (!chart.isDisposed()) {
                        chart.redraw();
                        chart.getParent().update();
                    }
                    // if (!comp.isDisposed())
                    // comp.update();
                }
            });
        }
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

    private void updateSlide() {
        final Composite comp = slider.getParent();
        if (comp != null) {
            comp.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (!slider.isDisposed())
                        slider.redraw();
                    if (!comp.isDisposed())
                        comp.update();
                }
            });
        }
    }

    private void makeMenus(Composite parent) {
        Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
        final MenuItem toggle = new MenuItem(menu, SWT.PUSH);
        toggle.setText("Toggle");
        this.setMenu(menu);
        this.getPlotArea().setMenu(menu);

        toggle.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                UpdatableChart.this.invertAxis();
            }
        });

        final MenuItem player1 = new MenuItem(menu, SWT.CHECK);
        player1.setText(match.getPlayerOne().toString());
        this.setMenu(menu);
        this.getPlotArea().setMenu(menu);

        final MenuItem player2 = new MenuItem(menu, SWT.CHECK);
        player2.setText(match.getPlayerTwo().toString());

        final MenuItem maPlayer1 = new MenuItem(menu, SWT.CHECK);
        maPlayer1.setText("MA " + match.getPlayerOne());

        final MenuItem maPlayer2 = new MenuItem(menu, SWT.CHECK);
        maPlayer2.setText("MA " + match.getPlayerTwo());

        final MenuItem spPlayer1 = new MenuItem(menu, SWT.CHECK);
        spPlayer1.setText("Back/Lay Spread " + match.getPlayerOne());

        final MenuItem spPlayer2 = new MenuItem(menu, SWT.CHECK);
        spPlayer2.setText("Back/Lay Spread " + match.getPlayerTwo());

        final MenuItem stretch = new MenuItem(menu, SWT.CHECK);
        stretch.setText("Stretch");

        player1.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                // cannot deselect both
                if (!player1.getSelection() && !player2.getSelection()) {
                    player1.setSelection(true);
                    return;
                }

                pl1Selected = !pl1Selected;
                firstSeries.setVisible(pl1Selected);
                updateDisplay();

            }

        });

        player2.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                // cannot deselect both
                if (!player2.getSelection() && !player1.getSelection()) {
                    player2.setSelection(true);
                    return;
                }
                pl2Selected = !pl2Selected;
                secondSeries.setVisible(pl2Selected);
                updateDisplay();
            }
        });

        maPlayer1.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                maPl1Selected = !maPl1Selected;
                maPl1Series.setVisible(maPl1Selected);
                updateDisplay();
            }
        });

        maPlayer2.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                maPl2Selected = !maPl2Selected;
                maPl2Series.setVisible(maPl2Selected);
                updateDisplay();
            }
        });

        spPlayer1.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                spPl1Selected = !spPl1Selected;
                pl1Spread.setVisible(spPl1Selected);
                updateDisplay();
            }
        });

        spPlayer2.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                spPl2Selected = !spPl2Selected;
                pl2Spread.setVisible(spPl2Selected);
                updateDisplay();
            }
        });

        stretch.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                SashForm sf = (SashForm) UpdatableChart.this.getParent()
                        .getParent();
                if (sf.getMaximizedControl() == null)
                    sf.setMaximizedControl(UpdatableChart.this.getParent());
                else
                    sf.setMaximizedControl(null);
            }
        });

        // first player selected by default
        player1.setSelection(true);

        // set the menu
        this.setMenu(menu);
        this.getPlotArea().setMenu(menu);
    }

    @Override
    public void setDisposeListener(DisposeListener listener) {
        this.addDisposeListener(listener);
    }

    /*
     * private class ZoomListener implements
     * MouseWheelListener,org.eclipse.swt.events.MouseWheelListener{ private
     * UpdatableChart uc;
     * 
     * public ZoomListener (UpdatableChart uc) { this.uc = uc; }
     * 
     * @Override public void mouseWheelMoved(final MouseWheelEvent e) { int
     * units = e.getWheelRotation(); IAxis yAxis = uc.getAxisSet().getYAxis(0);
     * if (units > 0) { // Rotated backward yAxis.zoomOut();
     * System.out.println("Zooming out..."); } else { // Rotated forward
     * yAxis.zoomIn(); System.out.println("Zooming in..."); } }
     * 
     * @Override public void mouseScrolled(MouseEvent e) {
     * System.out.println(e.toString()); } }8
     */
}
