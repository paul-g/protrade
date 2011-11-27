package org.ic.tennistrader.ui;

import java.util.Arrays;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Slider;

import org.ic.tennistrader.controller.BetController;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.listener.MatchSelectionListener;
import org.ic.tennistrader.score.PredictionGui;
import org.ic.tennistrader.ui.updatable.UpdatableChart;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;

public class DisplayPanel extends StandardTabbedWidgetContainer implements MatchSelectionListener {

    private SashForm chartSash;

    private static Logger log = Logger.getLogger(DisplayPanel.class);

    public DisplayPanel(Composite parent, int style) {
        super(parent, style);
    }

    private void addPredictionGui(Composite composite, Match match) {
        new PredictionGui(composite, SWT.BORDER, match);
    }

    public void handleMatchSelection(Match match) {
        addMatchView(match);
    }
    
    private boolean firstTime = true;

    public void addMatchView(Match match) {
        CTabItem[] items = folder.getItems();
        int pos = -1;

        String matchName = match.toString();

        for (int i = 0; pos == -1 && i < items.length; i++)
            if (items[i].getText().equals(matchName)) {
                pos = i;
            }

        // check new tab has been open
        if (pos == -1) {
            
            final CTabItem item = addTab(matchName);
     
            Composite control = new Composite(folder, SWT.NONE);
            control.setLayout(new FillLayout());
            
            SashForm comp = new SashForm(control, SWT.VERTICAL);
            
            SashForm infoAndBack = new SashForm(comp, SWT.HORIZONTAL);

            if (firstTime){
                firstTime = false;
                Image newImage = setColor(item, comp);
                
                folder.setSelectionBackground(newImage);
                folder.setBackgroundImage(newImage);      
                
            }
            
            addMatchData(infoAndBack, match);

            addMarketDataGrid(infoAndBack, match);

            SashForm horizontal = new SashForm(comp, SWT.HORIZONTAL);
            
            /*if (match.isInPlay()) {
                addPredictionGui(horizontal, match);
            }*/
            
            addPredictionGui(horizontal, match);
            
            this.chartSash = new SashForm(comp, SWT.HORIZONTAL);
            addChart(chartSash, match);
            addMatchViewer(chartSash);
            chartSash.setWeights(new int[]{60,40});
            
            item.setControl(control);
            
            folder.setSelection(item);

            infoAndBack.setWeights(new int[]{20, 80});
            comp.setWeights(new int[]{20,25,50,5});
            
        
            
        } else
            // just bring the required tab under focus
            folder.setSelection(pos);
    }

    private Image setColor(CTabItem item, SashForm comp) {
        final Display display = comp.getDisplay();
        final Color foregroundColor = new org.eclipse.swt.graphics.Color(
                display, 105, 105, 105);
        final Color backgroundColor = new org.eclipse.swt.graphics.Color(
                display, 168, 168, 168);
        final Rectangle rect = item.getBounds();
        
        Image newImage = new Image(display, rect.width, rect.height);
        
        GC gc = new GC(newImage);
        gc.setForeground(foregroundColor);
        gc.setBackground(backgroundColor);
        gc.fillGradientRectangle(rect.x, rect.y, rect.width, rect.height / 2,
                true);

        gc.setForeground(backgroundColor);
        gc.setBackground(foregroundColor);
        gc.fillGradientRectangle(rect.x, rect.height / 2, rect.width,
                rect.height, true);
        gc.dispose();
        return newImage;
    }

    /**
     * Adds an area for displaying match data
     * 
     * @param comp
     * @param ti
     */
    private void addMatchData(Composite comp, final Match match) {
    	MatchDataView matchDataView = new MatchDataView(comp, SWT.BORDER, match);
    	match.registerForUpdate(matchDataView);
    }

    /**
     * Adds the back and lay chart
     * 
     * @param comp
     * @param ti
     */
    private void addChart(Composite comp, Match match) {
        // Select values on chart
        Composite c = new Composite(comp, SWT.NONE);
        c.setLayout(new FillLayout());
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        c.setLayout(gridLayout);
        //c.setLayout(new GridLayout());
        Composite slideComp = new Composite(comp.getParent(), SWT.NONE);
        slideComp.setLayout(new FillLayout());
        Slider slider = new Slider(slideComp,SWT.HORIZONTAL);
        slider.setMaximum(1);
        slider.setValues(0, 0, 1, 0, 0, 0);
       // new Charts(c,SWT.BORDER,match,slider);
        final UpdatableChart chart = new UpdatableChart(c, SWT.BORDER, match,slider);
        
        match.registerForUpdate(chart);
        
        comp.update();
    }

    /**
     * Adds the market data grid with back and lay values and amounts
     * 
     * @param comp
     * @param ti
     */
	private void addMarketDataGrid(Composite comp, Match match) {
		UpdatableMarketDataGrid table = new UpdatableMarketDataGrid(comp, SWT.NONE);
		BetController betController = new BetController(Arrays.asList(table.getP1BackButtons()),
				Arrays.asList(table.getP1LayButtons()),	Arrays.asList(table.getP2BackButtons()), 
				Arrays.asList(table.getP2LayButtons()), match);
		table.setBetController(betController);
		match.registerForUpdate(table);
		//BetManager.registerGrid(match, table);
	}

    public Control getControl(){
        return folder.getParent();
    }
    
   public void addMatchViewer(Composite comp) {

        final Browser browser;
        try {
            browser = new Browser(comp, SWT.NONE);
        } catch (SWTError e) {
            log.error("Could not instantiate Browser: " + e.getMessage());
            return;
        }
        browser.setUrl("http://www.livescorehunter.ro/index.php?option=com_lsh&view=lsh&event_id=70396&tv_id=374&tid=26339&channel=0&tmpl=component&layout=popup&Itemid=207");
        comp.layout();
    }
   
   public void addMatchViewer(){
       addMatchViewer(chartSash);
   }
   
}
