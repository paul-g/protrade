package org.ic.tennistrader.score;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.domain.match.Match;
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

    private boolean statisticsPopulated = false;

    private static final int RETRY_LIMIT = 5;

    private int retries;

    private static Logger log = Logger.getLogger(StatisticsUpdateThread.class);
    
    public StatisticsUpdateThread(Match match) {
        this.match = match;
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

    @SuppressWarnings("serial")
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

        log.info("Logging in to site");

        HtmlPage loggedPage = (HtmlPage) submitButton.click();
        loggedPage.initialize();

        log.info("Successfully Logged in to site");

        HtmlTextInput player1 = (HtmlTextInput) loggedPage
                .getElementByName("match_preview_search1");
        HtmlTextInput player2 = (HtmlTextInput) loggedPage
                .getElementByName("match_preview_search2");
        HtmlElement body = (HtmlElement) loggedPage
                .getElementsByTagName("body").get(0);
        HtmlElement submitButton2 = (HtmlElement) body.getElementsByAttribute(
                "td", "onclick", "match_preview_form_top_right.submit()")
                .get(0);

        player1.setText(match.getPlayerOne().toString());
        player2.setText(match.getPlayerTwo().toString());

        HtmlPage intermPage = (HtmlPage) submitButton2.click();
        intermPage.initialize();

        HtmlElement btnContinue = (HtmlElement) intermPage
                .getElementById("addinsight");
        
        log.info("Successfully searched players");
        
        HtmlPage page = null;
        if (btnContinue != null)
            page = (HtmlPage) btnContinue.click();
        else
            page = intermPage;

        log.info("page: " + page);

        // page.initialize();

        // webClient.closeAllWindows();

        return (page.asText());
    }

   

    public boolean isStatisticsPopulated() {
        return this.statisticsPopulated;
    }

    @Override
    protected void runBody() {
        log.info(match.getPlayerOne().toString() + " v "
                + match.getPlayerTwo().toString());
        retries++;
        if (retries > RETRY_LIMIT) {
        	log.info("Retry limit reached - stopping thread");
            setStop();
        }
        else
            // try to get stats
            try {
            	try {
            		page = getStatistics();
            	} catch(Exception e){}
                log.info("Fetched statistics");
                if (page != null) {
                	System.out.println("Started parsing");
                    (new StatisticsParser(page, this.match)).parseAndSetStatistics();
                    statisticsPopulated = true;
                    setStop();
                    updateAll();
                } else
                    Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error(e.getStackTrace());
            }
    }

}
