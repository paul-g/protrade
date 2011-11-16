package org.ic.tennistrader.score;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.ic.tennistrader.Main;
import org.ic.tennistrader.utils.MatchUtils;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

public class PredictionGui {

    private static Logger log = Logger.getLogger(Main.class);

    private Composite composite;

    private TableColumn[] columns;

    private Table scoreTable;

    private String name1, name2;

    private UpdateThread updateThread;
    
    private String playerFirstNames;

    /**
* For running the prediction gui separately
*/
    public static void main(String args[]) {
        final Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());

        String matchName = "Gonzalez v Hidalgo";

        new PredictionGui(shell, matchName);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();

    }

    public PredictionGui(final Composite parent, String match) {
        this.updateThread = new UpdateThread(match);
        this.composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new GridLayout());

        try {
            if (MatchUtils.isMatch(match)) {
                createScoreContents(composite, match);
            }

            createProbabilityContents(composite); //
            //System.out.println(match.substring(match.indexOf("n")));
        } catch (Exception e) {
             // if something goes wrong
            log.error(e.getMessage() +

            " ");
            e.printStackTrace();
        }
        
        
        // Get scores once to get both players first names for use in statistics
        String firstScore= null;
        try {
             firstScore = updateThread.extractScores(match);
            // System.out.println("Score: " + this.score);
        } catch (Exception e) {
           // System.out.println(e.getMessage());
        }
        
        if (firstScore !=null)
        {
        	playerFirstNames = setScores(firstScore);
        }       

        try {
            String stats = getStatistics(match);
            Composite table = createStatisticsTable(parent);
            parseStatistics(stats, table);
        } catch (Exception e) {
            // if something goes wrong
            log.error(e.getMessage() + " ");
            e.printStackTrace();
        }

        parent.getDisplay().timerExec(1000, new Runnable() {
            @Override
            public void run() {
                handleUpdate();
                parent.getDisplay().timerExec(500, this);
            }
        });

        /*
* parent.getDisplay().asyncExec(new Runnable() {
*
* @Override public void run() { updateThread.run(); } });
*/

        /*
* parent.getDisplay().timerExec(5000, new Runnable() {
*
* @Override public void run() { updateThread.run(); } });
*/

        updateThread.start();

    }

    private void createScoreContents(Composite composite, String matchName) {
        final ToolBar toolBar = new ToolBar(composite, SWT.NONE);
        // toolBar.setBounds(new Rectangle(331, 8, 100, 30));

        final Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
        MenuItem match = new MenuItem(menu, SWT.PUSH);
        match.setText("best of 3 set tiebreaker");
        match = new MenuItem(menu, SWT.PUSH);
        match.setText("best of 3 set advantage");
        match = new MenuItem(menu, SWT.PUSH);
        match.setText("best of 5 set tiebreaker");
        match = new MenuItem(menu, SWT.PUSH);
        match.setText("best of 5 set ad(vantage");

        final ToolItem dropdown = new ToolItem(toolBar, SWT.DROP_DOWN);
        dropdown.setText("Match Type");
        dropdown.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (event.detail == SWT.ARROW) {
                    Rectangle rect = dropdown.getBounds();
                    Point pt = new Point(rect.x, rect.y + rect.height);
                    pt = toolBar.toDisplay(pt);
                    menu.setLocation(pt.x, pt.y);
                    menu.setVisible(true);
                }
            }
        });

        this.scoreTable = new Table(composite, SWT.NONE);
        scoreTable.setBounds(new Rectangle(10, 10, 270, 90));
        scoreTable.setHeaderVisible(true);

        // table.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        scoreTable.setLinesVisible(true);
        this.columns = new TableColumn[8];
        columns[0] = new TableColumn(scoreTable, SWT.NONE);
        columns[0].setText("Serving");
        columns[1] = new TableColumn(scoreTable, SWT.NONE);
        columns[1].setText("Score:");
        columns[2] = new TableColumn(scoreTable, SWT.NONE);
        columns[2].setText("Set 1");
        columns[3] = new TableColumn(scoreTable, SWT.NONE);
        columns[3].setText("Set 2");
        columns[4] = new TableColumn(scoreTable, SWT.NONE);
        columns[4].setText("Set 3");
        columns[5] = new TableColumn(scoreTable, SWT.NONE);
        columns[5].setText("Set 4");
        columns[6] = new TableColumn(scoreTable, SWT.NONE);
        columns[6].setText("Set 5");
        columns[7] = new TableColumn(scoreTable, SWT.NONE);
        columns[7].setText("Points");

        // Filling the probabilities table with data
        scoreTable.setRedraw(false);

        this.name1 = matchName.substring(0, matchName.indexOf(" v"));
        this.name2 = matchName.substring(matchName.indexOf("v ") + 2, matchName
                .length());
        if (name1.contains("/"))
            name1 = name1.substring(0, name1.indexOf("/"));
        if (name2.contains("/"))
            name2 = name2.substring(0, name2.indexOf("/"));

    }
    
    private String setScores(String scores) {
        int matchIndex = scores.indexOf(name1);

        TableItem item;
        TableItem item2;
        TableItem[] items = scoreTable.getItems();
        if (items.length == 2) {
            item = scoreTable.getItem(0);
            item2 = scoreTable.getItem(1);
        } else {
            item = new TableItem(scoreTable, SWT.NONE);
            item2 = new TableItem(scoreTable, SWT.NONE);
        }
       
        // display server
        if (matchIndex >= 8
                && scores.substring(matchIndex - 8, matchIndex - 2).compareTo(
                        "SERVER") == 0) {
            // player 1 serves
            item.setText(0, "S");
            item2.setText(0, "");
        } else {
            // player 2 serves
            item.setText(0, "");
            item2.setText(0, "S");
        }

        int c = 1;

        scores = scores.substring(matchIndex, scores.length());
        String player1 = scores.substring(0, scores.indexOf(")") + 1);
        scores = scores.substring(scores.indexOf("\n") + 1, scores.length());
        scores.trim();

        item.setText(c++, player1);

        // Match has to be in play or finished
        if (!scores.startsWith(name2)) {
            // //////////////Player 1 data
            // Skip odds
            if (scores.charAt(0) != '\t')
                scores = scores.substring(scores.indexOf("\n") + 1, scores
                        .length());
            // skip initial tab
            scores = scores.substring(1, scores.length());

            // 5 sets
            for (int i = 0; i < 5; i++) {
                item.setText(c++, scores.substring(0, scores.indexOf("\t")));
                scores = scores.substring(scores.indexOf("\t") + 1, scores
                        .length());
            }

            // Points
            item.setText(c++, scores.substring(0, 2));
            scores = scores
                    .substring(scores.indexOf("\n") + 1, scores.length());
            // //////////////END of Player 1 data
        }

        c = 1;
        
        scores = skipEmptyLines(scores);
        if(item2.getText(0).compareTo("S") == 0 && scores.startsWith("SERVER")) 
        	scores = skipLines(scores,1);
        String player2 = scores.substring(0, scores.indexOf(")") + 1);
        scores = scores.substring(scores.indexOf("\n") + 1, scores.length());
        scores.trim();

        item2.setText(c++, player2);

        // //////////////Player 2 data
        // Skip odds
        if (scores.charAt(0) != '\t' && !scores.startsWith("SERVER"))
            scores = scores
                    .substring(scores.indexOf("\n") + 1, scores.length());
        // skip initial tab
        scores = scores.substring(1, scores.length());

        // 5 sets
        for (int i = 0; i < 5; i++) {
            item2.setText(c++, scores.substring(0, scores.indexOf("\t")));
            scores = scores
                    .substring(scores.indexOf("\t") + 1, scores.length());
        }
        // Points
        item2.setText(c++, scores.substring(0, 2));

        // //////////////END of Player 2 data
        scoreTable.setRedraw(true);

        for (int i = 0, n = columns.length; i < n; i++) {
            columns[i].pack();
        }
        
        scoreTable.redraw();
        scoreTable.getParent().layout();
        
        return (player1.substring(player1.indexOf(" ") +1, player1.indexOf(" (")) + "---" + player2.substring(player2.indexOf(" ") + 1, player2.indexOf(" (")));
        
    }

    private void createProbabilityContents(Composite composite) {
        final Table table = new Table(composite, SWT.NONE);
        table.setBounds(new Rectangle(10, 110, 370, 90));
        table.setHeaderVisible(true);
        // table.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        table.setLinesVisible(true);
        TableColumn[] column = new TableColumn[5];

        column[0] = new TableColumn(table, SWT.NONE);
        column[0].setText("Probability of winning:");

        column[1] = new TableColumn(table, SWT.NONE);
        column[1].setText("Point");

        column[2] = new TableColumn(table, SWT.NONE);
        column[2].setText("Game");

        column[3] = new TableColumn(table, SWT.NONE);
        column[3].setText("Set");

        column[4] = new TableColumn(table, SWT.NONE);
        column[4].setText("Match");

        // Filling the probabilities table with data
        table.setRedraw(false);

        TableItem item = new TableItem(table, SWT.NONE);
        int c = 0;
        item.setText(c++, "Player 1");
        item.setText(c++, "62%");
        item.setText(c++, "78%");
        item.setText(c++, "57%");
        item.setText(c++, "63%");

        TableItem item2 = new TableItem(table, SWT.NONE);
        c = 0;
        item2.setText(c++, "Player 2");
        item2.setText(c++, "38%");
        item2.setText(c++, "22%");
        item2.setText(c++, "43%");
        item2.setText(c++, "37%");

        table.setRedraw(true);

        for (int i = 0, n = column.length; i < n; i++) {
            column[i].pack();
        }
    }

    private Composite createStatisticsTable(Composite composite) {
        composite.setLayout(new FillLayout());
        final Tree tree = new Tree(composite, SWT.MULTI | SWT.FULL_SELECTION
                | SWT.CENTER);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        TreeColumn[] tcolumn = new TreeColumn[3];
        tcolumn[0] = new TreeColumn(tree, SWT.LEFT);
        tcolumn[0].setText("Player 1");
        tcolumn[0].setWidth(220);
        tcolumn[0].setResizable(false);

        tcolumn[1] = new TreeColumn(tree, SWT.CENTER);
        tcolumn[1].setText("VS");
        tcolumn[1].setWidth(150);
        tcolumn[1].setResizable(false);

        tcolumn[2] = new TreeColumn(tree, SWT.RIGHT);
        tcolumn[2].setText("Player 2");
        tcolumn[2].setWidth(200);
        tcolumn[2].setResizable(false);
        // tcolumn[2].setImage(new
        // Image(composite.getDisplay(),"/home/radu/tennis-trader/lib/Verdasco.jpg"));

        return tree;
    }

    private String getStatistics(String matchName) throws Exception {
        this.name1 = matchName.substring(0, matchName.indexOf(" v"));
        this.name2 = matchName.substring(matchName.indexOf("v ") + 2, matchName
                .length());
        if (name1.contains("/"))
            name1 = name1.substring(0, name1.indexOf("/"));
        if (name2.contains("/"))
            name2 = name2.substring(0, name2.indexOf("/"));

        // Create a webClient to emulate Firefox browser
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);

        // Customize all webclient listeners and handlers for no warning/info
        // messages
        webClient.setIncorrectnessListener(new IncorrectnessListener() {
            public void notify(String string, Object object) {
            }
        });
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
            public void malformedScriptURL(HtmlPage page, String string,
                    MalformedURLException exception) {
            }

            public void loadScriptError(HtmlPage page, URL url,
                    Exception exception) {
            }

            public void scriptException(HtmlPage page, ScriptException exception) {
            }

            public void timeoutError(HtmlPage page, long int1, long int2) {
            }
        });
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setPrintContentOnFailingStatusCode(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setPopupBlockerEnabled(true);
        webClient.setAlertHandler(new AlertHandler() {
            public void handleAlert(Page page, String message) {
            }
        });
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setActiveXNative(true);
        webClient.setCssEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController() {
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request,
                    boolean async) {
                return true;
            }
        });

        webClient.setJavaScriptEnabled(true);
        HtmlPage PageLogin = webClient
                .getPage("http://www.tennisinsight.com/scoresheet.php");
        HtmlElement login = (HtmlElement) PageLogin.getElementById("LOGIN")
                .getElementsByTagName("form").get(0);

        HtmlTextInput name = (HtmlTextInput) PageLogin.getElementsByTagName(
                "input").get(0);
        HtmlPasswordInput pass = (HtmlPasswordInput) login
                .getElementsByTagName("input").get(1);
        HtmlElement submitButton = (HtmlElement) login.getElementsByTagName(
                "img").get(0);

        name.setText("radubal");
        pass.setText("placintacumere");

        System.out.println("Logging in to site");
        HtmlPage Loggedpage = (HtmlPage) submitButton.click();
        System.out.println("Successfully Logged in to site");

        HtmlTextInput player1 = (HtmlTextInput) Loggedpage
                .getElementByName("match_preview_search1");
        HtmlTextInput player2 = (HtmlTextInput) Loggedpage
                .getElementByName("match_preview_search2");
        HtmlElement body = (HtmlElement) Loggedpage
                .getElementsByTagName("body").get(0);
        HtmlElement submitButton2 = (HtmlElement) body.getElementsByAttribute(
                "td", "background", "/images/GO_green2.jpg").get(0);
        if (playerFirstNames!=null)
        {
        	player1.setText(playerFirstNames.substring(0, playerFirstNames.indexOf("---")) + " " + name1);
            player2.setText(playerFirstNames.substring(playerFirstNames.indexOf("---") +3, playerFirstNames.length()) + " " + name2);
        }
        else {
        	player1.setText(name1);
        	player2.setText(name2);
        }
        System.out.println(playerFirstNames.substring(0, playerFirstNames.indexOf("---")) + name1);
        System.out.println(playerFirstNames.substring(playerFirstNames.indexOf("---") +3, playerFirstNames.length()) + name2);
        HtmlPage intermPage = (HtmlPage) submitButton2.click();

        HtmlElement btnContinue = (HtmlElement) intermPage
                .getElementById("addinsight");
        System.out.println("Successfully searched players");
        HtmlPage page;
        if (btnContinue != null)
            page = (HtmlPage) btnContinue.click();
        else
            page = intermPage;
        webClient.closeAllWindows();

        return (page.asText());
    }

    private void parseStatistics(String stats, Composite comp) {
        Tree table = (Tree) comp;
        stats = stats.substring(stats.indexOf("Head to Head Match Preview"),
                stats.indexOf("Player Comparison"));
        stats = stats.substring(stats.indexOf("stats\n") + 6, stats.length());

        // System.out.println(stats + "/nEND OF CUT");
        /*
* try { BufferedReader in = new BufferedReader(new
* FileReader("/home/radu/tennis-trader/images/data.txt")); String str;
* while ((str = in.readLine()) != null) { stats += str+ '\n'; }
* in.close(); } catch (IOException e) { }
*/

        // Fill in table headers with name players and images
        String player1 = stats.substring(0, stats.indexOf('\n'));
        table.getColumn(0).setText(player1);
        int index =0;
        String imagePlayer = "";
        String cPlayer = player1;
        while(cPlayer.indexOf(' ') > -1)
        {
        	imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' ')) + "%20";
        	cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1 , cPlayer.length());
        }
        imagePlayer += cPlayer;
        
        Image imgPlayer = getImage("http://www.tennisinsight.com/images/" + imagePlayer + ".jpg");
        try{
        	table.getColumn(0).setImage(imgPlayer);
        	imgPlayer.dispose();
        } catch (NullPointerException ex) {
        	table.getColumn(0).setImage(getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
        }
        stats = skipLines(stats, 2);
        
        String player2 = stats.substring(0, stats.indexOf('\n'));
        table.getColumn(2).setText(player2);
        imagePlayer = "";
        cPlayer = player2;
        while(cPlayer.indexOf(' ') > -1)
        {
        	imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' ')) + "%20";
        	cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1 , cPlayer.length());
        }
        imagePlayer += cPlayer;
        
        imgPlayer = getImage("http://www.tennisinsight.com/images/" + imagePlayer + ".jpg");
        try{
        	table.getColumn(2).setImage(imgPlayer);
        	imgPlayer.dispose();
        } catch ( NullPointerException ex) {
        	table.getColumn(2).setImage(getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
        }
        
        stats = skipLines(stats, 2);

        // Fill in table
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 6; i++) {
            list.add(stats.substring(0, stats.indexOf('\n')));
            stats = skipLines(stats, 1);
        }
        stats = skipEmptyLines(stats);
        for (int i = 0; i < 6; i++) {
            list.add(stats.substring(0, stats.indexOf('\n')));
            stats = skipLines(stats, 1);
        }
        stats = skipLines(stats, 2);
        for (int i = 0; i < 6; i++) {
            list.add(stats.substring(0, stats.indexOf('\n')));
            stats = skipLines(stats, 1);
        }

        final TreeItem basics = new TreeItem(table, SWT.MULTI | SWT.CENTER);
        basics.setText(1, "Basics");
        basics
                .setForeground(table.getDisplay().getSystemColor(
                        SWT.COLOR_WHITE));
        basics.setBackground(table.getDisplay().getSystemColor(
                SWT.COLOR_DARK_GREEN));
        basics.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

        for (int i = 0; i < 6; i++) {
            TreeItem item = new TreeItem(basics, SWT.CENTER);
            item.setText(0, list.get(i));
            item.setText(1, list.get(i + 6));
            item.setBackground(1, table.getDisplay().getSystemColor(
                    SWT.COLOR_YELLOW));
            item.setText(2, list.get(i + 12));
        }

        // ////////////////////////
        // Match Statistics
        // ////////////////////////

        TreeItem match = new TreeItem(table, SWT.CENTER);
        match.setText(1, "Match Stats");
        match.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        match.setBackground(table.getDisplay().getSystemColor(
                SWT.COLOR_DARK_GREEN));
        match.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

        stats = stats.substring(stats.indexOf("Match Statistics\t") + 17, stats
                .length());
        stats = skipEmptyLines(stats);

        // Match W/L
        TreeItem item1 = new TreeItem(match, SWT.CENTER);
        item1.setBackground(1, table.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item1.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item1.setText(1, "Match W/L %");
        stats = skipEmptyLines(stats);
        item1.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Set W/L
        TreeItem item2 = new TreeItem(match, SWT.CENTER);
        item2.setBackground(1, table.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item2.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item2.setText(1, "Set W/L %");
        stats = skipEmptyLines(stats);
        item2.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Game W/L
        TreeItem item3 = new TreeItem(match, SWT.CENTER);
        item3.setBackground(1, table.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item3.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item3.setText(1, "Game W/L %");
        stats = skipEmptyLines(stats);
        item3.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Points W/L
        TreeItem item4 = new TreeItem(match, SWT.CENTER);
        item4.setBackground(1, table.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item4.setText(0, stats.substring(0, stats.indexOf("%") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item4.setText(1, "Points W/L %");
        stats = skipEmptyLines(stats);
        item4.setText(2, stats.substring(0, stats.indexOf("%") + 1));
        stats = skipLines(stats, 1);

        // Tiebreaks W/L
        TreeItem item5 = new TreeItem(match, SWT.CENTER);
        item5.setBackground(1, table.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item5.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item5.setText(1, "Tiebreaks W/L %");
        stats = skipEmptyLines(stats);
        item5.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Tiebreaks W/L
        TreeItem item6 = new TreeItem(match, SWT.CENTER);
        item6.setBackground(1, table.getDisplay().getSystemColor(
                SWT.COLOR_YELLOW));
        item6.setText(0, stats.substring(0, stats.indexOf("\t")));
        stats = stats.substring(stats.indexOf("Set") + 4, stats.length());
        item6.setText(1, "Tiebreaks/Set");
        item6.setText(2, stats.substring(0, stats.indexOf("\n")));
        stats = skipLines(stats, 2);

        // ////////////////////////
        // Service Statistics
        // ////////////////////////

        TreeItem serves = new TreeItem(table, SWT.CENTER);
        serves.setText(1, "Serve Stats");
        serves
                .setForeground(table.getDisplay().getSystemColor(
                        SWT.COLOR_WHITE));
        serves.setBackground(table.getDisplay().getSystemColor(
                SWT.COLOR_DARK_GREEN));
        serves.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

        for (int i = 0; i < 6; i++) {
            TreeItem item = new TreeItem(serves, SWT.CENTER);
            item.setText(0, stats.substring(0, stats.indexOf("\t")));
            stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
            item.setText(1, stats.substring(0, stats.indexOf("\t")));
            item.setBackground(1, table.getDisplay().getSystemColor(
                    SWT.COLOR_YELLOW));
            stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
            item.setText(2, stats.substring(0, stats.indexOf("\n")));
            stats = stats.substring(stats.indexOf("\n") + 1, stats.length());
        }
    }

    private String skipEmptyLines(String string) {
        while (string.charAt(0) == '\t' || string.charAt(0) == '\n'
                || string.startsWith(" ") || string.charAt(0) == '\t') {
            string = string.substring(1);
        }
        return string;
    }

    private String skipLines(String string, int count) {
        while (count-- > 0) {
            string = string
                    .substring(string.indexOf('\n') + 1, string.length());
        }
        return string;
    }

    public static Image getImage(String url) {
        Image img;
        try {
            URL web = new URL(url);
            InputStream stream = web.openStream();
            ImageLoader loader = new ImageLoader();
            ImageData imgData = loader.load(stream)[0];
            img = new Image(Display.getDefault(), imgData);
        } catch (Exception e) {
           // System.err.println("No image " + url + ", " + e);
            return null;
        }
        return img;
    }

    
    public void handleUpdate() {
        try {
            String score = updateThread.getScore();
            System.out.println("Fetched score");
            if (score != null) {
                System.out.println("Not null! Updating...");
                setScores(score);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private class UpdateThread extends Thread {

        String score;
        String match;

        public UpdateThread(String match) {
            this.match = match;
        }

        @Override
        public void run() {
            // keep updating the score
            while (true) {
                try {
                    this.score = extractScores(match);
                    // System.out.println("Score: " + this.score);
                } catch (Exception e) {
                   // System.out.println(e.getMessage());
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                   // System.out.println(e.getMessage());
                }
            }

            /*
* try { Thread.sleep(40000); } catch (InterruptedException e) { //
* TODO Auto-generated catch block e.printStackTrace(); }
*/
        }

        public String getScore() {
            return this.score;
        }

        /*
* Gets real-time scores from http://www.livexscores.com/ Emulates a
* firefox browser with javascript and AJAX enabled and fetches score
* data from website. Finally, it returns the data for further parsing
*/
        //@SuppressWarnings("serial")
        @SuppressWarnings("all")
        private String extractScores(String match) throws Exception {

            // Create a webClient to emulate Firefox browser
            WebClient webClient = new WebClient();// BrowserVersion.FIREFOX_3_6);

            // Customize all webclient listeners and handlers for no
            // warning/info
            // messages@SuppressWarnings("all")
            webClient.setIncorrectnessListener(new IncorrectnessListener() {
                public void notify(String string, Object object) {
                }
            });
            webClient.setCssErrorHandler(new SilentCssErrorHandler());
            webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
                public void malformedScriptURL(HtmlPage page, String string,
                        MalformedURLException exception) {
                }

                public void loadScriptError(HtmlPage page, URL url,
                        Exception exception) {
                }

                public void scriptException(HtmlPage page,
                        ScriptException exception) {
                }

                public void timeoutError(HtmlPage page, long int1, long int2) {
                }
            });
            webClient.setAlertHandler(new AlertHandler() {
                public void handleAlert(Page page, String message) {
                }
            });
            webClient.setThrowExceptionOnScriptError(false);
            webClient.setActiveXNative(true);
            webClient.setCssEnabled(true);

            webClient
                    .setAjaxController(new NicelyResynchronizingAjaxController() {
                        @Override
                        public boolean processSynchron(HtmlPage page,
                                WebRequest request, boolean async) {
                            return true;
                        }
                    });

            HtmlElement scores = null;

            webClient.waitForBackgroundJavaScript(3000);
            HtmlPage page = webClient
                    .getPage("http://www.livexscores.com/livescore/tennis");
            
            // try 20 times to wait .5 second each for filling the page.
            for (int i = 0; i < 2; i++) {
                // page
                scores = page.getElementById("allzapasy");

                if (scores.asText() != "") {
                    break;
                }
                synchronized (page) {
                    page.wait(500);
                }
            }

            scores = page.getElementById("allzapasy");

            // Modify tennis balls gif elements of the website to
            // readable text so as to identify the tennis player serving
            Iterator<HtmlElement> iter = (scores.getElementsByAttribute("img",
                    "src", "/img/tennisball.gif")).iterator();
            while (iter.hasNext()) {
                ((HtmlElement) iter.next()).setTextContent("\nSERVER");
            }

            // Getting the relevant scores
            Iterator<DomNode> itr = scores.getChildren().iterator();
            itr.next();
            HtmlElement elem = (HtmlElement) itr.next();

            Iterator<DomNode> itr2 = elem.getChildren().iterator();
            DomNode elem2 = (DomNode) itr2.next();

            String string2 = elem2.asText();

            System.out.println("Extracted score");

            return string2;
        }
    }

}

