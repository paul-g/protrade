package org.ic.tennistrader.score;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.Statistics;
import org.ic.tennistrader.service.threads.MatchUpdaterThread;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

public class StatisticsUpdateThread extends MatchUpdaterThread {

    private List<Listener> listeners = new ArrayList<Listener>();

    private String page = null;

    private Statistics playerOneStats;

    private Statistics playerTwoStats;

    private StatisticsPanel st;

    private boolean statisticsPopulated = false;

    private static final int RETRY_LIMIT = 5;

    private static final int N_STATS_LINE = 4;
    private int retries;

    private static Logger log = Logger.getLogger(StatisticsUpdateThread.class);

    public StatisticsUpdateThread(Match match, StatisticsPanel st) {
        this.match = match;
        this.st = st;
        playerOneStats = new Statistics();
        playerTwoStats = new Statistics();
    }

    public Statistics getPlayerOneStats() {
        return this.playerOneStats;
    }

    public Statistics getPlayerTwoStats() {
        return this.playerTwoStats;
    }

    public String getPage() {
        return page;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    private void updateAll() {
        for (Listener l : listeners) {
            l.handleEvent(new Event());
        }
    }

    private String getStatistics() throws Exception {

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
        HtmlElement submitButton = (HtmlElement) login.getElementsByAttribute(
                "img", "onclick", "goForGold()").get(0);

        name.setText("radubal2");
        pass.setText("placintacumere");

        System.out.println("Logging in to site");

        HtmlPage loggedPage = (HtmlPage) submitButton.click();
        loggedPage.initialize();

        System.out.println("Successfully Logged in to site");

        HtmlTextInput player1 = (HtmlTextInput) loggedPage
                .getElementByName("match_preview_search1");
        HtmlTextInput player2 = (HtmlTextInput) loggedPage
                .getElementByName("match_preview_search2");
        HtmlElement body = (HtmlElement) loggedPage
                .getElementsByTagName("body").get(0);
        HtmlElement submitButton2 = (HtmlElement) body.getElementsByAttribute(
                "td", "onclick", "match_preview_form_top_right.submit()")
                .get(0);

        player1.setText(match.getPlayerOne().getLastname().toString());
        player2.setText(match.getPlayerTwo().getLastname().toString());

        HtmlPage intermPage = (HtmlPage) submitButton2.click();
        intermPage.initialize();

        HtmlElement btnContinue = (HtmlElement) intermPage
                .getElementById("addinsight");
        System.out.println("Successfully searched players");
        HtmlPage page = null;
        if (btnContinue != null)
            page = (HtmlPage) btnContinue.click();
        else
            page = intermPage;

        System.out.println("page: " + page);

        // page.initialize();

        // webClient.closeAllWindows();

        return (page.asText());
    }

    private String stats;

    private void parseStatistics(String statsString) {
        this.stats = statsString;
        stats = stats.substring(stats.indexOf("Head to Head Match Preview"),
                stats.indexOf("Player Comparison"));
        stats = stats.substring(stats.indexOf("stats\n") + 6, stats.length());

        // Fill in table headers with name players and images
        stats = getImages(stats);

        stats = skipLines(stats, 2);

        Player playerOne = match.getPlayerOne();
        Player playerTwo = match.getPlayerTwo();

        stats = skipEmptyLines(stats);
        // Fill in stats (dob, country, age etc.)
        setPlayerInfo(playerOne);

        stats = skipEmptyLines(stats);
        stats = skipLines(stats, 6);
        stats = skipEmptyLines(stats);
        stats = skipLines(stats, 1);
        setPlayerInfo(playerTwo);

        stats = skipEmptyLines(stats);

        // ////////////////////////
        // Match Statistics
        // ////////////////////////

        String[] playerOneWonLost = new String[6];
        String[] playerTwoWonLost = new String[6];

        stats = stats.substring(stats.indexOf("Match Statistics\t") + 17, stats
                .length());
        stats = skipEmptyLines(stats);

        // Matches, sets, games
        for (int i = 0; i < 3; i++) {
            playerOneWonLost[i] = stats.substring(0, stats.indexOf(")") + 1);
            stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
            stats = skipEmptyLines(stats);
            playerTwoWonLost[i] = stats.substring(0, stats.indexOf(")") + 1);
            stats = skipLines(stats, 1);
        }

        // Points
        playerOneWonLost[3] = stats.substring(0, stats.indexOf("%") + 1);
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        stats = skipEmptyLines(stats);
        playerTwoWonLost[3] = stats.substring(0, stats.indexOf("%") + 1);
        stats = skipLines(stats, 1);

        // Tiebreaks
        playerOneWonLost[4] = stats.substring(0, stats.indexOf(")") + 1);
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        stats = skipEmptyLines(stats);
        playerTwoWonLost[4] = stats.substring(0, stats.indexOf(")") + 1);
        stats = skipLines(stats, 1);

        playerOneWonLost[5] = stats.substring(0, stats.indexOf("\t") + 1);
        stats = stats.substring(stats.indexOf("Set") + 4, stats.length());
        stats = skipEmptyLines(stats);
        playerTwoWonLost[5] = stats.substring(0, stats.indexOf("\n") + 1);
        stats = skipLines(stats, 1);

        match.setPlayerOneWonLost(playerOneWonLost);
        match.setPlayerTwoWonLost(playerTwoWonLost);
        // ////////////////////////
        // Service Statistics
        // ////////////////////////

       Map<String, String [][]> statisticsMap = new HashMap<String, String[][]>();
       int[] sizes = {6, 4, 6, 4};
       for (int i=0;i<N_STATS_LINE;i++) {
           
           String statsName = getAndSkip();
           String[][] values = new String[sizes[i]][3];
           
           for (int j=0;j<sizes[i];j++) {
               values[j][0] = stats.substring(0, stats.indexOf("\t"));
               stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
               values[j][1] = stats.substring(0, stats.indexOf("\t"));
               stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
               values[j][2] = stats.substring(0, stats.indexOf("\n"));
               stats = stats.substring(stats.indexOf("\n") + 1, stats.length());
           }
           
           statisticsMap.put(statsName, values);
           
               
       }
       
       match.setStatisticsMap(statisticsMap);
       
       /*
       for (int i = 0; i < 6; i++) {
           
            TreeItem item = new TreeItem(serves, SWT.CENTER);
            item.setText(0, ));
            stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
            item.setText(1, stats.substring(0, stats.indexOf("\t")));
            item.setBackground(1, table.getDisplay().getSystemColor(
                    SWT.COLOR_YELLOW));
            stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
            item.setText(2, stats.substring(0, stats.indexOf("\n")));
            stats = stats.substring(stats.indexOf("\n") + 1, stats.length());
/*
            if (i == 2) {
                // System.out.println(Double.parseDouble(item.getText(0).substring(0,item.getText(0).length()
                // -1)));
                playerOneStats
                        .setFirstServePercent(Double.parseDouble(item
                                .getText(0).substring(0,
                                        item.getText(0).length() - 1)) / 100);
                playerTwoStats
                        .setFirstServePercent(Double.parseDouble(item
                                .getText(2).substring(0,
                                        item.getText(2).length() - 1)) / 100);
            }
            if (i == 3) {
                playerOneStats
                        .setFirstServeWins(Double.parseDouble(item.getText(0)
                                .substring(0, item.getText(0).length() - 1)) / 100);
                playerTwoStats
                        .setFirstServeWins(Double.parseDouble(item.getText(2)
                                .substring(0, item.getText(2).length() - 1)) / 100);
            }
            if (i == 4) {
                playerOneStats
                        .setSecondServeWins(Double.parseDouble(item.getText(0)
                                .substring(0, item.getText(0).length() - 1)) / 100);
                playerTwoStats
                        .setSecondServeWins(Double.parseDouble(item.getText(2)
                                .substring(0, item.getText(2).length() - 1)) / 100);
            }
        }*/
          
    }

    private void setPlayerInfo(Player playerOne) {
        playerOne.setCountry(getAndSkip());
        playerOne.setDob(getAndSkip());
        String heightAndPlays = getAndSkip();
        String[] values = heightAndPlays.split("/");
        playerOne.setHeight(values[0].trim());
        playerOne.setPlays(values[1].trim());
        playerOne.setWonLost(getAndSkip());
        playerOne.setTitles(getAndSkip());
        playerOne.setRank(getAndSkip());
    }

    private String getAndSkip() {
        String element = stats.substring(0, stats.indexOf('\n'));
        this.stats = skipLines(stats, 1);
        return element;
    }

    private String getImages(String stats) {
        String player1 = stats.substring(0, stats.indexOf('\n'));
        // table.getColumn(0).setText(player1);
        int index = 0;
        String imagePlayer = "";
        String cPlayer = player1;
        while (cPlayer.indexOf(' ') > -1) {
            imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
                    + "%20";
            cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1, cPlayer
                    .length());
        }
        imagePlayer += cPlayer;

        Image imgPlayer = getImage("http://www.tennisinsight.com/images/"
                + imagePlayer + ".jpg");
        try {
            // table.getColumn(0).setImage(imgPlayer);
            imgPlayer.dispose();
        } catch (NullPointerException ex) {
            // table
            // .getColumn(0)
            // .setImage(
            // getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
        }
        stats = skipLines(stats, 2);

        String player2 = stats.substring(0, stats.indexOf('\n'));
        // table.getColumn(2).setText(player2);
        imagePlayer = "";
        cPlayer = player2;
        while (cPlayer.indexOf(' ') > -1) {
            imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
                    + "%20";
            cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1, cPlayer
                    .length());
        }
        imagePlayer += cPlayer;

        imgPlayer = getImage("http://www.tennisinsight.com/images/"
                + imagePlayer + ".jpg");
        try {
            // table.getColumn(2).setImage(imgPlayer);
            imgPlayer.dispose();
        } catch (NullPointerException ex) {
            // table
            // .getColumn(2)
            // .setImage(
            // getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
        }
        return stats;
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

    public boolean isStatisticsPopulated() {
        return this.statisticsPopulated;
    }

    @Override
    protected void runBody() {
        System.out.println(match.getPlayerOne().toString() + " vs "
                + match.getPlayerTwo().toString());
        retries++;
        if (retries > RETRY_LIMIT)
            setStop();
        else
            // try to get stats
            try {
                page = getStatistics();
                if (page != null) {
                    parseStatistics(page);
                    statisticsPopulated = true;
                    setStop();
                    updateAll();
                } else
                    Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                // log.error(e.getMessage());
                // log.error(e.getStackTrace());
            }
    }

}
