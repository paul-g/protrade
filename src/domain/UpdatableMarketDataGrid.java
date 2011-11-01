package src.domain;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
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
    private OddsButton[] p2BackButtons = new OddsButton[3];
    private OddsButton[] p2LayButtons = new OddsButton[3];
    private MOddsMarketData modds;
    private Label player1;
    private Label player2;
    // private Composite comp;

    private Color normalColor;
    private Color layColor;
    private Color backColor;
    private Font oddsFont;
    private Font titleFont;
    
    private class OddsButton {

        private Composite comp;
        private Label odds;
        private Label amount;

        OddsButton(Composite parent, Color color) {
            comp = new Composite(parent, SWT.BORDER);
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
            this.amount.setForeground(composite.getDisplay().getSystemColor(
                    SWT.COLOR_DARK_GRAY));
        }

        void setOdds(String odds) {
            this.odds.setText(odds);
        }

        void setAmount(String amount) {
            this.amount.setText("£" + amount);
        }

        Composite getComposite() {
            return comp;
        }

        void layout() {
            comp.layout();
        }
    }

    public UpdatableMarketDataGrid(Composite parent, TreeItem ti) {
        this.parent = parent;
        
        composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new GridLayout(7, true));

        initColors();
        
        Label blankLabel = new Label(composite, SWT.NONE);

        GridData headerData = new GridData();
        headerData.horizontalSpan = 3;
        headerData.horizontalAlignment = GridData.FILL;

        this.oddsFont = new Font(composite.getDisplay(),"Arial",14,SWT.BOLD);
        this.titleFont = new Font(composite.getDisplay(),"Arial",16,SWT.None);
        
        Label back = new Label(composite, SWT.RIGHT);
        back.setLayoutData(headerData);
        back.setText("Back");
        back.setBackground(backColor);
        back.setFont(titleFont);

        Label lay = new Label(composite, SWT.NONE);
        lay.setLayoutData(headerData);
        lay.setText("Lay");
        lay.setBackground(layColor);
        lay.setFont(titleFont);
        

        player1 = new Label(composite, SWT.NONE);
        //player1.setFont(font);
        for (int i = 0; i < 2; i++)
            p1BackButtons[i] = new OddsButton(composite, normalColor);
        p1BackButtons[2] = new OddsButton(composite, backColor);

        p1LayButtons[0] = new OddsButton(composite, layColor);
        for (int i = 1; i < 3; i++)
            p1LayButtons[i] = new OddsButton(composite, normalColor);

        player2 = new Label(composite, SWT.NONE);
        //player2.setFont(font);
        for (int i = 0; i < 2; i++)
            p2BackButtons[i] = new OddsButton(composite, normalColor);
        p2BackButtons[2] = new OddsButton(composite, backColor);

        p2LayButtons[0] = new OddsButton(composite, layColor);
        for (int i = 1; i < 3; i++)
            p2LayButtons[i] = new OddsButton(composite, normalColor);

    }

    public void updateButtons(ArrayList<Pair<Double, Double>> valueList,
            OddsButton[] buttons, boolean back) {
        int i = 0;
        if (back)
            i = 3 - i - 1;
        for (Pair<Double, Double> p : valueList) {
            buttons[i].setOdds(p.getI().toString());
            buttons[i].setAmount(p.getJ().toString());
            buttons[i].layout();
            if (back)
                i--;
            else
                i++;
        }
    }

    public void handleUpdate(MOddsMarketData newData) {
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

    private void initColors() {
        this.layColor = new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 238, 210, 238);

        this.backColor = new org.eclipse.swt.graphics.Color(
                composite.getDisplay(), 198, 226, 255);

        this.normalColor = composite.getDisplay().getSystemColor(
                SWT.COLOR_GRAY);

    }
}