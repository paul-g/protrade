package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.service.BetManager;
import org.ic.tennistrader.ui.BetsDisplay;
import org.ic.tennistrader.utils.Pair;

public class UpdatableMarketDataGrid implements UpdatableWidget {
    private Composite composite;
    private OddsButton[] p1BackButtons = new OddsButton[3];
    private OddsButton[] p1LayButtons = new OddsButton[3];
    private OddsButton[] p2BackButtons = new OddsButton[3];
    private OddsButton[] p2LayButtons = new OddsButton[3];
    private Label player1;
    private Label player2;

    private Color normalColor;
    private Color layColor;
    private Color backColor;
    private Font oddsFont;
    private Font titleFont;
    private Color clickColor;
    private Color hoverColor;

    public UpdatableMarketDataGrid(Composite parent) {
        composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new GridLayout(7, true));

        initColors();
        initFonts();

        // required for alignment
        @SuppressWarnings("unused")
        Label blankLabel = new Label(composite, SWT.NONE);

        GridData headerData = new GridData();
        headerData.horizontalSpan = 3;
        headerData.horizontalAlignment = GridData.FILL;

        createLabel("Back", backColor, headerData, SWT.RIGHT);
        createLabel("Lay", layColor, headerData, SWT.NONE);

        player1 = initLayout(p1BackButtons, p1LayButtons);
        player2 = initLayout(p2BackButtons, p2LayButtons);
    }

    private void createLabel(String text, Color color, GridData headerData,
            int textAlignment) {
        Label back = new Label(composite, textAlignment);
        back.setLayoutData(headerData);
        back.setText(text);
        back.setBackground(color);
        back.setFont(titleFont);
    }

    private Label initLayout(OddsButton[] pBackButtons, OddsButton[] pLayButtons) {
        Label player = new Label(composite, SWT.NONE);
        player.setFont(titleFont);
        for (int i = 0; i < 2; i++)
            pBackButtons[i] = new OddsButton(composite, normalColor);
        pBackButtons[2] = new OddsButton(composite, backColor);
        pLayButtons[0] = new OddsButton(composite, layColor);
        for (int i = 1; i < 3; i++)
            pLayButtons[i] = new OddsButton(composite, normalColor);
        return player;
    }

    private void initFonts() {
        this.oddsFont = new Font(composite.getDisplay(), "Arial", 12, SWT.BOLD);
        this.titleFont = new Font(composite.getDisplay(), "Arial", 13, SWT.None);
    }

    public void updateButtons(ArrayList<Pair<Double, Double>> valueList,
            OddsButton[] buttons, boolean back) {
        int i = 0, step = 1;
        if (back) {
            i = 3 - i - 1;
            step = -1;
        }
        for (Pair<Double, Double> p : valueList) {
            buttons[i].setOdds(p.getI().toString());
            buttons[i].setAmount(p.getJ().toString());
            buttons[i].layout();
            i += step;
        }
    }

    public void handleUpdate(final MOddsMarketData newData) {
        composite.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (newData.getPl1Back() != null) {
                    updateButtons(newData.getPl1Back(), p1BackButtons, true);
                    updateButtons(newData.getPl1Lay(), p1LayButtons, false);
                    updateButtons(newData.getPl2Back(), p2BackButtons, true);
                    updateButtons(newData.getPl2Lay(), p2LayButtons, false);
                    player1.setText(newData.getPlayer1());
                    player2.setText(newData.getPlayer2());
                    composite.layout();
                }
            }
        });
    }

    private void initColors() {
        // The application's current background colour is 238, 238, 224
        this.layColor = new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 238, 210, 238);
        this.backColor = new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 198, 226, 255);
        this.normalColor = new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 240, 240, 240);
        this.clickColor =  new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 84, 139, 84);
       this.hoverColor = new org.eclipse.swt.graphics.Color(
               composite.getDisplay(), 124, 205, 124);
    }

    private class OddsButton {
        private Composite comp;
        private Label odds;
        private Label amount;
        private final Display display;
        private Color initialColor;

        OddsButton(Composite parent, Color color) {
            this.initialColor = color;
            comp = new Composite(parent, SWT.BORDER);
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
                    addBet(amount);
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
                    addBet(10.0);
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
        
        void addBet(double a){
            double o = Double.parseDouble(odds.getText());
            BetManager.addBet(o, a);
            BetsDisplay.addBet(o, a);            
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

    @Override
    public void setDisposeListener(Listener listener) {
        composite.addListener(SWT.Dispose, listener);
    }
}