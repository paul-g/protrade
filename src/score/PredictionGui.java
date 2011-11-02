package src.score;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridLayout;

import src.Main;
import src.utils.MatchUtils;

import java.net.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.*;
import java.util.*;

public class PredictionGui {

    private static Logger log = Logger.getLogger(Main.class);

    private Composite composite;

    private String matchName;

    private TableColumn[] columns;

    private Table scoreTable;

    private String name1, name2;

    public PredictionGui(final Composite parent, String match) {
        this.matchName = match;
        this.composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new GridLayout());

        try {
            if (MatchUtils.isMatch(match)) {
                createScoreContents(composite, match);
            }
            createProbabilityContents(composite);
            // System.out.println(match.substring(match.indexOf("n")));
        } catch (Exception e) {
            // if something goes wrong
            log.error(e.getMessage() + " ");
            e.printStackTrace();
            // throw new RuntimeException();
        }

        parent.getDisplay().timerExec(10000, new Runnable() {
            @Override
            public void run() {
                handleUpdate();
                parent.getDisplay().timerExec(10000, this);
            }
        });
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

        setScores();
    }

    private void setScores() {
        String scores = "";
        try {
            scores = extractScores(matchName);
        } catch (Exception exception) {
            //log.error(exception.getMessage());
            exception.printStackTrace();
        }

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
            System.out.println("Server 2");
            item.setText(0, "");
            item2.setText(0, "S");
        }
        
        int c = 1;

        System.out.println(matchIndex + "");
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

        System.out.println(scores);
        System.out.println("Substring: \'" + scores.substring(0, 6) + "\'");

        scores = skipEmptyLines(scores);
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

    /*
     * Gets real-time scores from http://www.livexscores.com/ Emulates a firefox
     * browser with javascript and AJAX enabled and fetches score data from
     * website. Finally, it returns the data for further parsing
     */
    private String extractScores(String match) throws Exception {

        // Create a webClient to emulate Firefox browser
        WebClient webClient = new WebClient();// BrowserVersion.FIREFOX_3_6);

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

        // Cleverly modify tennis balls gif elements of the website to
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

        String allScores = scores.asText();
        String string2 = elem2.asText();

        /*
         * int first = Math.min(string2.indexOf("WTA"), string2.indexOf("ATP"));
         * allScores += "\n" + string2.substring(first);
         */
        // System.out.println(scores.asXml());
        // System.out.println(allScores);

        return string2;

        // ////////j++;}
    }

    private String skipEmptyLines(String string) {
        while (string.charAt(0) == '\t' || string.charAt(0) == '\n'
                || string.startsWith(" ") || string.charAt(0) == '\t') {
            string = string.substring(1);
        }
        return string;
    }

    public void handleUpdate() {
        try {
            setScores();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
