package org.ic.tennistrader.score;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

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

public class StatisticsUpdateThread extends Thread{
    
    private List<Listener> listeners = new ArrayList<Listener>();

    private String page = null;
    private String playerFirstNames;
    private String matchName;
    
    public StatisticsUpdateThread(String matchName, String playerFirstNames) {
        this.matchName = matchName;
        this.playerFirstNames = playerFirstNames;
    }
    
    @Override
    public void run() {
        System.out.println(playerFirstNames);
        // keep updating the score
        try {
            page = getStatistics(matchName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
      // updateAll();
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
    
    private String getStatistics(String matchName) throws Exception {
       String name1 = matchName.substring(0, matchName.indexOf(" v"));
       String name2 = matchName.substring(matchName.indexOf("v ") + 2,
                matchName.length());
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
        if (this.playerFirstNames != null) {
            player1.setText(playerFirstNames.substring(0,
                    playerFirstNames.indexOf("---"))
                    + " " + name1);
            player2.setText(playerFirstNames.substring(
                    playerFirstNames.indexOf("---") + 3,
                    playerFirstNames.length())
                    + " " + name2);
        } else {
            player1.setText(name1);
            player2.setText(name2);
        }
        System.out.println(playerFirstNames.substring(0,
                playerFirstNames.indexOf("---"))
                + name1);
        System.out.println(playerFirstNames.substring(
                playerFirstNames.indexOf("---") + 3, playerFirstNames.length())
                + name2);
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

}
