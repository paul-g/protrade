package org.ic.tennistrader.ui.updatable;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ic.tennistrader.controller.BetController;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.utils.Pair;

public class UpdatableMarketDataGrid extends StandardWidgetContainer implements
        UpdatableWidget {

    private BetController betController;
    private OddsButton[] p1BackButtons = new OddsButton[3];
    private OddsButton[] p1LayButtons = new OddsButton[3];
    private OddsButton[] p2BackButtons = new OddsButton[3];
    private OddsButton[] p2LayButtons = new OddsButton[3];

    private Color normalColor;
    private Color layColor;
    private Color backColor;
    private Font oddsFont;
    private Font titleFont;

    private Match match;

    public UpdatableMarketDataGrid(Composite parent, int style, Match match) {
        super(parent, style);
        this.match = match;
        this.setLayout(new GridLayout(7, true));

        initColors();
        initFonts();

        // required for alignment
        @SuppressWarnings("unused")
        Label blankLabel = new Label(this, SWT.NONE);

        GridData headerData = new GridData();
        headerData.horizontalSpan = 3;
        headerData.horizontalAlignment = GridData.FILL;

        createLabel("Back", backColor, headerData, SWT.RIGHT);
        createLabel("Lay", layColor, headerData, SWT.NONE);

        initLayout(match.getPlayerOne().getLastname(), p1BackButtons,
                p1LayButtons, true);
        initLayout(match.getPlayerTwo().getLastname(), p2BackButtons,
                p2LayButtons, false);
    }

    private void createLabel(String text, Color color, GridData headerData,
            int textAlignment) {
        Label back = new Label(this, textAlignment);
        back.setLayoutData(headerData);
        back.setText(text);
        back.setBackground(color);
        back.setFont(titleFont);
    }

    private void initLayout(String playerName, OddsButton[] pBackButtons,
            OddsButton[] pLayButtons, boolean pl1) {
        Label player = new Label(this, SWT.NONE);
        player.setFont(titleFont);
        player.setText(playerName);

        for (int i = 0; i < 2; i++) {
            pBackButtons[i] = new OddsButton(this, normalColor, oddsFont, this);
            /*
             * Composite comp = pBackButtons[i].getComp(); Image
             * backBackgroundImage =
             * GraphicsUtils.makeGradientBackgroundImage(comp, 150, 150, 150,
             * 238, 210, 238 ); Image backClickImage =
             * GraphicsUtils.makeGradientBackgroundImage(comp, 84, 139, 84, 84,
             * 139, 84); Image backHoverImage =
             * GraphicsUtils.makeGradientBackgroundImage(comp, 155, 205, 155,
             * 193, 255, 193);
             * pBackButtons[i].setBackgroundImage(backBackgroundImage);
             * pBackButtons[i].setClickImage(backClickImage);
             * pBackButtons[i].setHighlightImage(backHoverImage);
             */
        }

        pBackButtons[2] = new OddsButton(this, backColor, oddsFont, this);
        pLayButtons[0] = new OddsButton(this, layColor, oddsFont, this);

        for (int i = 1; i < 3; i++)
            pLayButtons[i] = new OddsButton(this, normalColor, oddsFont, this);
    }

    private void initFonts() {
        this.oddsFont = new Font(this.getDisplay(), "Arial", 10, SWT.BOLD);
        this.titleFont = new Font(this.getDisplay(), "Arial", 10, SWT.None);
    }

    public void updateButtons(ArrayList<Pair<Double, Double>> valueList,
            OddsButton[] buttons, boolean back) {
        int i = 0, step = 1;
        if (back) {
            i = 3 - i - 1;
            step = -1;
        }
        for (Pair<Double, Double> p : valueList) {
            buttons[i].setOdds(p.first().toString());
            buttons[i].setAmount(p.second().toString());
            buttons[i].layout();
            i += step;
        }
    }

    public void handleUpdate(final MOddsMarketData newData) {
        this.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (newData.getPl1Back() != null) {
                    updateButtons(newData.getPl1Back(), p1BackButtons, true);
                    updateButtons(newData.getPl1Lay(), p1LayButtons, false);
                    updateButtons(newData.getPl2Back(), p2BackButtons, true);
                    updateButtons(newData.getPl2Lay(), p2LayButtons, false);
                    UpdatableMarketDataGrid.this.layout();
                }
            }
        });
    }

    private void initColors() {
        // The application's current background colour is 238, 238, 224
        this.layColor = new org.eclipse.swt.graphics.Color(this.getDisplay(),
                238, 210, 238);
        this.backColor = new org.eclipse.swt.graphics.Color(this.getDisplay(),
                198, 226, 255);
        this.normalColor = new org.eclipse.swt.graphics.Color(
                this.getDisplay(), 240, 240, 240);
    }

    public OddsButton[] getP1BackButtons() {
        return p1BackButtons;
    }

    public void setP1BackButtons(OddsButton[] p1BackButtons) {
        this.p1BackButtons = p1BackButtons;
    }

    public OddsButton[] getP1LayButtons() {
        return p1LayButtons;
    }

    public void setP1LayButtons(OddsButton[] p1LayButtons) {
        this.p1LayButtons = p1LayButtons;
    }

    public OddsButton[] getP2BackButtons() {
        return p2BackButtons;
    }

    public void setP2BackButtons(OddsButton[] p2BackButtons) {
        this.p2BackButtons = p2BackButtons;
    }

    public OddsButton[] getP2LayButtons() {
        return p2LayButtons;
    }

    public void setP2LayButtons(OddsButton[] p2LayButtons) {
        this.p2LayButtons = p2LayButtons;
    }

    public void setBetController(BetController betController) {
        this.betController = betController;

    }

    public BetController getBetController() {
        return betController;
    }

    @Override
    public void setDisposeListener(DisposeListener listener) {
        this.addDisposeListener(listener);

    }
}