package org.ic.tennistrader.ui.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.score.PredictionCalculator;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.StandardWidgetContainer;
import org.ic.tennistrader.ui.betting.BetsDisplay;

public class WimbledonScorePanel extends StandardWidgetContainer implements
        ScorePanel {
    private Match match;
    private Table scoreTable;
    private PlayerEnum server;
    private TableColumn[] columns;
    private Display display;
    
    private Image im2;
    
    private static final String[] colLabelText = {"1", "2", "3", "4", "", "", "Sts", "",  "Gms", "", "Pts", ""};
    private int nCols = colLabelText.length;
    
    private List<Label> colLabels = new ArrayList<Label>();
    
    private Map<PlayerEnum, Map<Integer, Label>> labelMap = new HashMap<PlayerEnum, Map<Integer, Label>>();

   private final static int[] SETS = {1,2,3,4}; 
    
    private final static int NAME = 5;
    private final static int PRED_MATCH = 6;
    
    private final static int SET= 7; 
    private final static int PRED_SET= 8;
    private final static int GAME =9;
    private final static int PRED_GAME = 10;
   private final static int POINT = 11;
    private final static int PRED_POINT = 12;
    
    private static final String PRED_SIZER = makeSizer(0);
    private static final String SIZER = makeSizer(0);
    
    private static final int DEFAULT_NAME_SIZE = 30;
    private static final int DEFAULT_EXTRA_SPACE = 3; 
    private int nameSize = DEFAULT_NAME_SIZE;
    
    public WimbledonScorePanel(Composite parent, Match match) {
        super(parent, SWT.NONE);
        this.match = match;
        this.display = parent.getDisplay();
        Image im = new Image(this.display, "images/scoreboard.png" );
        this.im2 = new Image(this.display, "images/scoreboard1.png");
        this.setBackgroundImage(im);
        
        int name1Len = match.getPlayer(PlayerEnum.PLAYER1).toString().length();
        int name2Len = match.getPlayer(PlayerEnum.PLAYER2).toString().length();
        
        nameSize = (name1Len > name2Len? name1Len : name2Len); 
        
        if (nameSize < DEFAULT_NAME_SIZE) nameSize = DEFAULT_NAME_SIZE;
        
        GridLayout gl = new GridLayout();
        gl.numColumns = nCols;
        this.setLayout(gl);
        
        for (int i=0;i<nCols;i++)
            colLabels.add(makeColLabel(colLabelText[i]));
        
        initScoreLabels(PlayerEnum.PLAYER1);
        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.CENTER;

        dummyLabel(4);
        
        Label l = new Label(this, SWT.NONE);
        l.setText("vs");
        l.setFont(new Font(display, "Times", 10, SWT.NONE));
        l.setLayoutData(gd);
        l.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        
        dummyLabel(nCols-5);
        
        initScoreLabels(PlayerEnum.PLAYER2);

        LiveDataFetcher.registerForMatchUpdate(this, match);
    }


    private static String makeSizer(int n) {
        String s = new String();
        for (int i=0;i<n + DEFAULT_EXTRA_SPACE;i++)
            s+=" ";
        return s;
    }

    private void dummyLabel(int n) {
        for (int i=0;i<n;i++)
            dummyLabel();
    }


    private void dummyLabel() {
        Label l  = new Label(this, SWT.NONE);
    }


    private void initScoreLabels(PlayerEnum player) {
        Map<Integer, Label> map = new HashMap<Integer, Label>();
        
        for (int i=0;i<4;i++)
           map.put(SETS[i], makeScoreLabelWithText(this, " 0 "));
        
        map.put(NAME, makeScoreLabelWithText(this, match.getPlayer(player) + makeSizer(nameSize - match.getPlayer(player).toString().length())));
        map.put(PRED_MATCH,  makeScoreLabelWithText(this, "0%" + PRED_SIZER, new Font(display, "Times", 12, SWT.NONE), display.getSystemColor(SWT.COLOR_GREEN)));
        
        map.put(SET, makeScoreLabelWithText(this, " 0 "+ SIZER));
        map.put(PRED_SET,  makeScoreLabelWithText(this, "0%" + PRED_SIZER, new Font(display, "Times", 12, SWT.NONE), display.getSystemColor(SWT.COLOR_GREEN)));
        
        map.put(GAME, makeScoreLabelWithText(this, " 0 " + SIZER));
        map.put(PRED_GAME,  makeScoreLabelWithText(this, "0%" + PRED_SIZER, new Font(display, "Times", 12, SWT.NONE), display.getSystemColor(SWT.COLOR_GREEN)));
        
        map.put(POINT, makeScoreLabelWithText(this, " 0 "+ SIZER));
        map.put(PRED_POINT,  makeScoreLabelWithText(this, "0%" + PRED_SIZER, new Font(display, "Times", 12, SWT.NONE), display.getSystemColor(SWT.COLOR_GREEN)));
        
        labelMap.put(player, map);
    }

    private Label makeScoreLabelWithText(
            Composite comp, String string) {
        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        Font f = new Font(display, "Times", 14, SWT.BOLD);
        Label label = new Label(comp, SWT.NONE);
        label.setBackgroundImage(im2);
        label.setFont(f);
        label.setLayoutData(gd);
        label.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
        label.setText(string);
        return label;
    }
    
    private Label makeScoreLabelWithText(
            Composite comp, String string, Font f, Color foreground) {
        GridData gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        Label label = new Label(comp, SWT.NONE);
        label.setBackgroundImage(im2);
        label.setFont(f);
        label.setLayoutData(gd);
        label.setForeground(foreground);
        label.setText(string);
        return label;
    }


    private Label makeColLabel(String string) {
        Label label = new Label(this, SWT.NONE);
        label.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        label.setText(string);
        label.setFont(new Font(display, "Times", 10, SWT.NONE));
        return label;
    }


    public void setScores() {
        Score score = match.getScore();
        int playerOneScores[] = score.getPlayerOneScore();
        
        setPlayerScore(score , PlayerEnum.PLAYER1);
        setPlayerScore(score , PlayerEnum.PLAYER2);
        
        try {
        double[] result = PredictionCalculator.calculate(this.match);
        
        setPlayerPrediction(PredictionCalculator.getP1Data(result), PlayerEnum.PLAYER1);
        setPlayerPrediction(PredictionCalculator.getP2Data(result), PlayerEnum.PLAYER2);
        } catch (Exception e){}
    }

    private void setPlayerScore(Score score, PlayerEnum player) {
        Map<Integer, Label> labels = labelMap.get(player);
        //labels.get(SET).setText(playerOneScores[]);
        labels.get(POINT).setText(score.getPlayerPoints(player) + "");
    }
    
    private void setPlayerPrediction(double[] result, PlayerEnum player) {
        Map<Integer, Label> labels = labelMap.get(player);
        labels.get(PRED_POINT).setText(BetsDisplay.DOUBLE_FORMAT.format(result[0]) + "%");
        labels.get(PRED_GAME).setText(BetsDisplay.DOUBLE_FORMAT.format(result[1]) + "%");
        labels.get(PRED_SET).setText(BetsDisplay.DOUBLE_FORMAT.format(result[2]) + "%");
        labels.get(PRED_MATCH).setText(BetsDisplay.DOUBLE_FORMAT.format(result[3]) + "%");
    }

    public void setServer(PlayerEnum player) {
        if (player == PlayerEnum.PLAYER1)
            scoreTable.getItem(0).setText(0, "S");
        else
            scoreTable.getItem(1).setText(0, "S");

        server = player;
    }

    public PlayerEnum getServer() {
        return this.server;
    }

    @Override
    public void handleUpdate(MOddsMarketData newData) {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                setScores();
            }
        });
    }

    @Override
    public void setDisposeListener(DisposeListener listener) {
        this.addDisposeListener(listener);
    }
}