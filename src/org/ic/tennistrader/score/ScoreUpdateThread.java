package org.ic.tennistrader.score;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.service.threads.MatchUpdaterThread;

import com.gargoylesoftware.htmlunit.AlertHandler;
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
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

public class ScoreUpdateThread extends MatchUpdaterThread {    

    public ScoreUpdateThread(Match match) {
        this.match = match;
    }

    public Score getScore() {
        return match.getScore();
    }

    /*
     * Gets real-time scores from http://www.livexscores.com/ Emulates a
     * firefox browser with javascript and AJAX enabled and fetches score
     * data from website. Finally, it returns the data for further parsing
     */
    @SuppressWarnings("serial")
    public String extractScores() throws Exception {

        // Create a webClient to emulate Firefox browser
        WebClient webClient = new WebClient();// BrowserVersion.FIREFOX_3_6);

        // Customize all webclient listeners and handlers for no
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
    
  
    
    public void handleUpdate() {
        try {
            Score score = this.getScore();
            System.out.println("Fetched score");    
            if (score != null) {
                System.out.println("Not null! Updating...");
                System.out.println("Completed!!!!!!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 

	@Override
	protected void runBody() {
		try {
            String scoreString = extractScores();
            new ScoreParser(scoreString, this.match).parseAndSetScores();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
	}

}