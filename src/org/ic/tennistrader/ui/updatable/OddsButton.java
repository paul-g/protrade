package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.ic.tennistrader.ui.BetPlacingShell;

public class OddsButton {
	private Composite comp;
	private UpdatableMarketDataGrid dataGrid;
    private Label odds;
    private Label amount;
    private final Display display;
    private Color initialColor;
    private Color clickColor;
    private Color hoverColor;
    
    private Image backgroundImage;
    private Image highlightImage;
    private Image clickImage;
    /* 
      public static void main(){
     
        final Display display = new Display();
        final Shell shell = new Shell(display, SWT.NONE);
        
        shell.setLayout(new FillLayout());
        
        OddsButton oddsButton = new OddsButton(shell, new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 238, 210, 238), oddsFont, dataGrid)
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }
    
    */
    OddsButton(Composite parent, Color color, Font oddsFont, UpdatableMarketDataGrid dataGrid) {     	 
    	this.dataGrid = dataGrid;
        comp = new Composite(parent, SWT.BORDER);
        comp.setBackgroundMode(SWT.INHERIT_DEFAULT);
        initColors(color);
        this.display = parent.getDisplay(); 
        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        comp.setLayoutData(gd);
        comp.setLayout(rowLayout);
        comp.setBackground(color);
        
        this.initialColor = color;
        
        this.odds = new Label(comp, SWT.NONE);
        this.odds.setFont(oddsFont);
        this.amount = new Label(comp, SWT.NONE);

        addClickListener();
         
         //final Timeline rolloverTimeline = new Timeline(comp);
        // rolloverTimeline.addPropertyToInterpolate("backgroundImage", backgroundImage, highlightImage);
         //rolloverTimeline.setDuration(100);
         comp.addMouseTrackListener(new MouseTrackListener() {

             @Override
             public void mouseEnter(MouseEvent arg0) {
            	 comp.setBackground(hoverColor);
                 comp.setBackgroundImage(highlightImage);
                //rolloverTimeline.play();
             }

             @Override
             public void mouseExit(MouseEvent e) {
                // if ( !odds.isFocusControl() && !amount.isFocusControl())
                     //rolloverTimeline.playReverse();
            	 comp.setBackground(initialColor);
                 comp.setBackgroundImage(backgroundImage);
             }

             @Override
             public void mouseHover(MouseEvent arg0) {
                 // TODO Auto-generated method stub

             }
         });
         
         Menu menu = makeMenu(comp);
         comp.setMenu(menu);
         odds.setMenu(menu);
         amount.setMenu(menu);
    }
    
	private void initColors(Color initialColor) {
		this.initialColor = initialColor;
		this.clickColor = new org.eclipse.swt.graphics.Color(comp
				.getDisplay(), 84, 139, 84);
		this.hoverColor = new org.eclipse.swt.graphics.Color(comp
				.getDisplay(), 124, 205, 124);
	}
    
    private Menu makeMenu(Composite comp) {
        Menu menu = new Menu(comp.getShell(), SWT.POP_UP);
        makeItem(menu, 15.0);
        makeItem(menu, 20.0);
        makeItem(menu, 25.0);
        makeItem(menu, 30.0);
        makeItem(menu, 40.0);
        return menu;
    }
    
    private void makeItem(Menu menu, final double amount){
        final MenuItem a = new MenuItem(menu, SWT.PUSH);
        a.setText(amount + "£");
        
        a.addListener(SWT.Selection, new Listener(){
            @Override
            public void handleEvent(Event arg0) {
                    dataGrid.getBetController().addBet(OddsButton.this, amount, Double.parseDouble(odds.getText()));            	
            }
        });
    }

    public void setBackground(Color color){
        comp.setBackground(color);
    }
    
    void addClickListener(){
        Listener l = new Listener() {
            @Override
            public void handleEvent(Event e) {
            	BetPlacingShell betPlacingShell = new BetPlacingShell(OddsButton.this, dataGrid.getBetController());
            	Rectangle rect = comp.getClientArea();
            	betPlacingShell.setLocation(rect.x,rect.y);
            	comp.setBackgroundImage(clickImage);
            	setBackground(clickColor);                
                display.timerExec(100, new Runnable() {
                    @Override
                    public void run() {
                    	comp.setBackground(initialColor);
                        //comp.setBackgroundImage(backgroundImage);
                    }
                });
              }
         };
                
        comp.addListener(SWT.MouseUp, l); 
        odds.addListener(SWT.MouseUp, l);
        amount.addListener(SWT.MouseUp, l);
    }
           
    void setOdds(String odds) {
        this.odds.setText(odds);
    }

    void setAmount(String amount) {
        this.amount.setText("£" + amount);
    }
 
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setHighlightImage(Image highlightImage) {
        this.highlightImage = highlightImage;
    }
    
    public void setClickImage(Image clickImage) {
        this.clickImage = clickImage;
    }
    
    public Composite getComp() {
		return comp;
	}

	public Label getOdds() {
		return odds;
	}

    void layout() {
        comp.layout();
    }    
    
    public double getAmount() {
        String amount = this.amount.getText().substring(1);
        return Double.parseDouble(amount);
    }
}
