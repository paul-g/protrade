package org.ic.tennistrader.ui.updatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class OddsButton {
	private Composite comp;
	private UpdatableMarketDataGrid dataGrid;
    private Label odds;
    private Label amount;
    private final Display display;
    private Color initialColor;
    private Color clickColor;
    private Color hoverColor;    

    OddsButton(Composite parent, Color color, Font oddsFont, UpdatableMarketDataGrid dataGrid) {     	 
    	this.dataGrid = dataGrid;
        comp = new Composite(parent, SWT.BORDER);
        initColors(color);
        this.display = parent.getDisplay(); 
        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.FILL;
        comp.setBackground(color);
        comp.setLayoutData(gd);
        comp.setLayout(rowLayout);
        this.odds = new Label(comp, SWT.NONE);
        this.odds.setFont(oddsFont);
        this.amount = new Label(comp, SWT.NONE);
        odds.setBackground(color);
        amount.setBackground(color);

         addClickListener();
         addEnterListener();
         addExitListener();
         
         
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
            	//BetController.addBet(OddsButton.this, amount, Double.parseDouble(odds.getText()));
            	dataGrid.getBetController().addBet(OddsButton.this, amount, Double.parseDouble(odds.getText()));
            }
        });
    }

    void setBackgroundColor(Color color){
        odds.setBackground(color);
        amount.setBackground(color);
        comp.setBackground(color);
    }
    
    void addClickListener(){
        Listener l = new Listener() {
            @Override
            public void handleEvent(Event e) {
            	//BetController.addBet(OddsButton.this, 10.0, Double.parseDouble(odds.getText()));
            	dataGrid.getBetController().addBet(OddsButton.this, 10.0, Double.parseDouble(odds.getText()));
                setBackgroundColor(clickColor);
                
                display.timerExec(100, new Runnable() {
                    @Override
                    public void run() {
                        setBackgroundColor(hoverColor);
                    }
                });
              }
         };
                
        comp.addListener(SWT.MouseUp, l); 
        odds.addListener(SWT.MouseUp, l);
        amount.addListener(SWT.MouseUp, l);
    }
            
    
    void addEnterListener(){
        Listener l = new Listener() {
            @Override
            public void handleEvent(Event e) {
                setBackgroundColor(hoverColor);
              }
         };
                
        comp.addListener(SWT.MouseEnter, l); 
        odds.addListener(SWT.MouseEnter, l);
        amount.addListener(SWT.MouseEnter, l);
    }
    
    void addExitListener(){
        Listener l = new Listener() {
            @Override
            public void handleEvent(Event e) {
                setBackgroundColor(initialColor);
              }
         };
                
        comp.addListener(SWT.MouseExit, l); 
        odds.addListener(SWT.MouseExit, l);
        amount.addListener(SWT.MouseExit, l);
    }

    void setOdds(String odds) {
        this.odds.setText(odds);
    }

    void setAmount(String amount) {
        this.amount.setText("£" + amount);
    }

    void layout() {
        comp.layout();
    }
}
