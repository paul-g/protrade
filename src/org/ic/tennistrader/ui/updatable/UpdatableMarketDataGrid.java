package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.ic.tennistrader.domain.MOddsMarketData;
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

	private void createLabel(String text, Color color, GridData headerData, int textAlignment) {
		Label back = new Label(composite, textAlignment);
        back.setLayoutData(headerData);
        back.setText(text);
        back.setBackground(color);
        back.setFont(titleFont);
	}

	private Label initLayout(OddsButton[] pBackButtons,
			OddsButton[] pLayButtons) {
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
    	// The application's current background colour is 238, 238, 224
    	this.layColor = new org.eclipse.swt.graphics.Color(composite
                .getDisplay(), 238, 210, 238);
        this.backColor = new org.eclipse.swt.graphics.Color(composite
                .getDisplay(), 198, 226, 255);
        this.normalColor = new org.eclipse.swt.graphics.Color(composite
                .getDisplay(), 240, 240, 240);
    }

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
            //this.amount.setForeground(composite.getDisplay().getSystemColor(
            //        SWT.COLOR_DARK_GRAY));
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
}