package org.ic.tennistrader.score;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import org.ic.tennistrader.domain.match.RealMatch;
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

public class StatisticsUpdateThread extends MatchUpdaterThread{ 
	
    private List<Listener> listeners = new ArrayList<Listener>();
    
    private String page = null;
    
    private StatisticsPanel st;
    
    private Statistics playerOneStats;
    
    private Statistics playerTwoStats;
    
    private boolean statisticsPopulated = false;
    
    private static Logger log = Logger.getLogger(StatisticsUpdateThread.class);
    
    public StatisticsUpdateThread(Match match, StatisticsPanel st) {
        this.match = match;
        this.st = st;
    }
    
    public Statistics getPlayerOneStats()
    {
    	return this.playerOneStats;
    }
    
    public Statistics getPlayerTwoStats()
    {
    	return this.playerTwoStats;
    }
    
    @Override
    public void run() {
   
    }
    public String getPage(){
        return page;
    }
    
    public void addListener(Listener listener){
        this.listeners.add(listener);
    }
    
    private void updateAll(){
        for (Listener l : listeners){
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
        
        player1.setText(match.getPlayerOne().toString());
        player2.setText(match.getPlayerTwo().toString());
    
        System.out.println(match.getPlayerOne().toString());
        System.out.println(match.getPlayerTwo().toString());
        
        HtmlPage intermPage = (HtmlPage) submitButton2.click();

        HtmlElement btnContinue = (HtmlElement) intermPage
                .getElementById("addinsight");
        System.out.println("Successfully searched players");
        HtmlPage page = null;
        if (btnContinue != null)
            page = (HtmlPage) btnContinue.click();
        else
            page = intermPage;
     
        

        for (int i = 0; i < 20; i++) {
            System.out.println("Waiting");
            // page
            String stats = page.asText();
            
            if ( stats.indexOf("Head to Head Match Preview") != -1 ) 
                break;
            
            synchronized (page) {
                page.wait(500);
            }
        }
        
        webClient.closeAllWindows();
        
        return (page.asText());
    }
    
    private void parseStatistics(String stats) {
        Tree table = st.getTree();
        stats = stats.substring(stats.indexOf("Head to Head Match Preview"),
                stats.indexOf("Player Comparison"));
        stats = stats.substring(stats.indexOf("stats\n") + 6, stats.length());

        // Fill in table headers with name players and images
        String player1 = stats.substring(0, stats.indexOf('\n'));
        table.getColumn(0).setText(player1);
        int index = 0;
        String imagePlayer = "";
        String cPlayer = player1;
        while (cPlayer.indexOf(' ') > -1) {
            imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
                    + "%20";
            cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1,
                    cPlayer.length());
        }
        imagePlayer += cPlayer;

        Image imgPlayer = getImage("http://www.tennisinsight.com/images/"
                + imagePlayer + ".jpg");
        try {
            table.getColumn(0).setImage(imgPlayer);
            imgPlayer.dispose();
        } catch (NullPointerException ex) {
            table.getColumn(0)
                    .setImage(
                            getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
        }
        stats = skipLines(stats, 2);

        String player2 = stats.substring(0, stats.indexOf('\n'));
        table.getColumn(2).setText(player2);
        imagePlayer = "";
        cPlayer = player2;
        while (cPlayer.indexOf(' ') > -1) {
            imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
                    + "%20";
            cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1,
                    cPlayer.length());
        }
        imagePlayer += cPlayer;

        imgPlayer = getImage("http://www.tennisinsight.com/images/"
                + imagePlayer + ".jpg");
        try {
            table.getColumn(2).setImage(imgPlayer);
            imgPlayer.dispose();
        } catch (NullPointerException ex) {
            table.getColumn(2)
                    .setImage(
                            getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg"));
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
        basics.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        basics.setBackground(table.getDisplay().getSystemColor(
                SWT.COLOR_DARK_GREEN));
        basics.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

        for (int i = 0; i < 6; i++) {
            TreeItem item = new TreeItem(basics, SWT.CENTER);
            item.setText(0, list.get(i));
            item.setText(1, list.get(i + 6));
            item.setBackground(1,
                    table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
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

        stats = stats.substring(stats.indexOf("Match Statistics\t") + 17,
                stats.length());
        stats = skipEmptyLines(stats);

        // Match W/L
        TreeItem item1 = new TreeItem(match, SWT.CENTER);
        item1.setBackground(1,
                table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        item1.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item1.setText(1, "Match W/L %");
        stats = skipEmptyLines(stats);
        item1.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Set W/L
        TreeItem item2 = new TreeItem(match, SWT.CENTER);
        item2.setBackground(1,
                table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        item2.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item2.setText(1, "Set W/L %");
        stats = skipEmptyLines(stats);
        item2.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Game W/L
        TreeItem item3 = new TreeItem(match, SWT.CENTER);
        item3.setBackground(1,
                table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        item3.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item3.setText(1, "Game W/L %");
        stats = skipEmptyLines(stats);
        item3.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Points W/L
        TreeItem item4 = new TreeItem(match, SWT.CENTER);
        item4.setBackground(1,
                table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        item4.setText(0, stats.substring(0, stats.indexOf("%") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item4.setText(1, "Points W/L %");
        stats = skipEmptyLines(stats);
        item4.setText(2, stats.substring(0, stats.indexOf("%") + 1));
        stats = skipLines(stats, 1);

        // Tiebreaks W/L
        TreeItem item5 = new TreeItem(match, SWT.CENTER);
        item5.setBackground(1,
                table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        item5.setText(0, stats.substring(0, stats.indexOf(")") + 1));
        stats = stats.substring(stats.indexOf("W/L") + 3, stats.length());
        item5.setText(1, "Tiebreaks W/L %");
        stats = skipEmptyLines(stats);
        item5.setText(2, stats.substring(0, stats.indexOf(")") + 1));
        stats = skipLines(stats, 1);

        // Tiebreaks W/L
        TreeItem item6 = new TreeItem(match, SWT.CENTER);
        item6.setBackground(1,
                table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
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
        serves.setForeground(table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        serves.setBackground(table.getDisplay().getSystemColor(
                SWT.COLOR_DARK_GREEN));
        serves.setFont(new Font(null, "BOLD", 12, SWT.ITALIC));

        for (int i = 0; i < 6; i++) {
            TreeItem item = new TreeItem(serves, SWT.CENTER);
            item.setText(0, stats.substring(0, stats.indexOf("\t")));
            stats = stats.substring(stats.indexOf("\t") + 1, stats.length());
            item.setText(1, stats.substring(0, stats.indexOf("\t")));
            item.setBackground(1,
                    table.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
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

    public void checkStatisticsUpdate() {

        if (!statisticsPopulated) {
            try {
                String stats = this.getPage();
                if (stats != null) {
                    statisticsPopulated = true;
                    parseStatistics(stats);
                }
            } catch (Exception e) {
                // if something goes wrong
                log.error(e.getMessage() + " ");
                e.printStackTrace();
            }
        }
    }
    
    public boolean isStatisticsPopulated()
    {
    	return this.statisticsPopulated;
    }

    @Override
    public void addMatch(RealMatch match) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void runBody() {
        // TODO Auto-generated method stub
        System.out.println(match.getPlayerOne().toString() + " vs " + match.getPlayerTwo().toString());
        // try to get stats
        try {
            page = getStatistics();
        } catch (Exception e) {
            //log.error(e.getMessage());
            //log.error(e.getStackTrace());
        }
        
      // updateAll();
        this.stop = true;
    }

}
