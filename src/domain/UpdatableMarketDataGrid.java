package src.domain;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;

import src.Pair;

public class UpdatableMarketDataGrid implements UpdatableWidget {
    private Composite parent;
    private Composite composite;
    private OddsButton[] p1BackButtons = new OddsButton[3];
    private OddsButton[] p1LayButtons = new OddsButton[3];
    private OddsButton[] p2BackButtons= new OddsButton[3];
    private OddsButton[] p2LayButtons = new OddsButton[3];
    private MOddsMarketData modds;
    private Label player1;
    private Label player2;
    //private Composite comp;
    
    private class OddsButton{
        
        private Composite comp;
        private Label odds;
        private Label amount;
        
        OddsButton(Composite parent, Color color){
            comp = new Composite(parent, SWT.BORDER);
            RowLayout rowLayout = new RowLayout();
            rowLayout.type = SWT.VERTICAL;
            GridData gd = new GridData();
            gd.horizontalAlignment = GridData.FILL;
            comp.setBackground(color);
            comp.setLayoutData(gd);
            comp.setLayout(rowLayout);
            this.odds = new Label(comp, SWT.NONE);
            this.amount = new Label(comp, SWT.NONE);
            odds.setBackground(color);
            amount.setBackground(color);
        }
        
        void setOdds(String odds){
            this.odds.setText(odds);
        }
        
        void setAmount(String amount){
            this.amount.setText("£" + amount);
        }
        
        Composite getComposite(){
            return comp;
        }
        
        void layout(){
            comp.layout();
        }
    }

    
    
    public UpdatableMarketDataGrid(Composite parent, TreeItem ti) {
        this.parent = parent;
        composite = new Composite(parent, SWT.BORDER);

        composite.setLayout(new GridLayout(7, true));

        Label blankLabel = new Label(composite, SWT.NONE);
        GridData headerData = new GridData();
        headerData.horizontalSpan = 3;
        headerData.horizontalAlignment = GridData.FILL;
        Label back = new Label(composite, SWT.RIGHT);
        back.setLayoutData(headerData);
        back.setText("Back");
        Label lay = new Label(composite, SWT.NONE);
        lay.setLayoutData(headerData);
        lay.setText("Lay");

        //Color cyan  = composite.getDisplay().getSystemColor(SWT.COLOR_CYAN);
        //Color magenta  = composite.getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
        
        java.awt.Color colorPink = new java.awt.Color(238, 210, 238);
        Color magenta = new org.eclipse.swt.graphics.Color(composite.getDisplay(),
                colorPink.getRed(), colorPink.getGreen(), colorPink.getBlue());
        
        java.awt.Color colorBlue = new java.awt.Color(198, 226, 255);
        Color cyan = new org.eclipse.swt.graphics.Color(composite.getDisplay(),
                colorBlue.getRed(), colorBlue.getGreen(), colorBlue.getBlue());
        
        player1 = new Label(composite, SWT.NONE);
        for (int i = 0; i < 3; i++)
            p1BackButtons[i] = new OddsButton(composite, cyan);
        for (int i = 0; i < 3; i++)
            p1LayButtons[i] = new OddsButton(composite, magenta);

        player2 = new Label(composite, SWT.NONE);
        for (int i = 0; i < 3; i++) 
            p2BackButtons[i] = new OddsButton(composite, cyan);
        for (int i = 0; i < 3; i++)
            p2LayButtons[i] = new OddsButton(composite, magenta);

       }
   
    public void updateButtons(ArrayList<Pair<Double, Double>> valueList, OddsButton[] buttons, boolean back){
        int i = 0;
        if (back) i = 3 - i - 1;
        for (Pair<Double, Double> p : valueList) {
            buttons[i].setOdds(p.getI().toString());
            buttons[i].setAmount(p.getJ().toString());
            buttons[i].layout();
            if (back) i--;
            else i++;
        }
    }
    
    public void handleUpdate(MOddsMarketData newData) {
        if (newData.getPl1Back() != null) {
            System.out.println("Setting new data");
            updateButtons(newData.getPl1Back(), p1BackButtons, true);
            updateButtons(newData.getPl1Lay(), p1LayButtons, false);
            updateButtons(newData.getPl2Back(), p2BackButtons, true);
            updateButtons(newData.getPl2Lay(), p2LayButtons, false);
            player1.setText(newData.getPlayer1());
            player2.setText(newData.getPlayer2());
            composite.layout();
        }
    }
}