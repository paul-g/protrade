package org.ic.tennistrader.score;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.service.MatchUpdaterThread;

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
    private Match match;    
    private ScorePanel sc;    
    private String scoreString;

    public ScoreUpdateThread(Match match, ScorePanel sc) {
        this.match = match;
        this.sc = sc;
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
    
    private void parseScores() {
    	int[] playerOneGames = new int[6];
    	int[] playerTwoGames = new int[6];
        int matchStart = scoreString.indexOf(match.getPlayerOne().getLastname() + " " + match.getPlayerOne().getFirstname());
        
        // display server
        if (matchStart >= 8
                && scoreString.substring(matchStart - 8, matchStart - 2).compareTo(
                        "SERVER") == 0) {
            // player 1 serves
            sc.setServer(PlayerEnum.PLAYER1);
        }
 
        scoreString = scoreString.substring(matchStart, scoreString.length());
        //String player1 = scoreString.substring(0, scoreString.indexOf(")") + 1);
        scoreString = scoreString.substring(scoreString.indexOf("\n") + 1, scoreString.length());
        scoreString.trim();

        // Match has to be in play or finished
        if (!scoreString.startsWith(match.getPlayerTwo().getLastname() + " " + match.getPlayerTwo().getFirstname())) {
            // //////////////Player 1 data
            // Skip odds
            if (scoreString.charAt(0) != '\t')
                scoreString = skipLines(scoreString,1);
            
            // skip initial tab
            scoreString = scoreString.substring(1, scoreString.length());
            
            // for 5 sets max
            int pos = 0;
            for (int i = 0; i < 5; i++) {
            	playerOneGames[i] = Integer.parseInt(scoreString.substring(0, scoreString.indexOf("\t")));
                scoreString = scoreString.substring(scoreString.indexOf("\t") + 1,
                        scoreString.length());
                
                if (scoreString.startsWith("\n") || 
                	scoreString.startsWith("\t") || 
                	scoreString.startsWith(" "))
                	{ pos = i+1; i = 5; scoreString = scoreString.trim(); }
            }          
          
            // Points
            if (!scoreString.startsWith(match.getPlayerTwo().getLastname())) {
            	playerOneGames[pos] = Integer.parseInt(scoreString.substring(0, 2));
            	scoreString = skipEmptyLines(scoreString);
            } else {
            	scoreString = skipLines(scoreString,1);
            }
            // //////////////END of Player 1 data
        }

        if (scoreString.startsWith("SERVER")) {
            scoreString = skipLines(scoreString, 1);
            // player 2 serves
        	sc.setServer(PlayerEnum.PLAYER2);
        }
        
        scoreString.trim();

        // //////////////Player 2 data
        // Skip odds
        if (scoreString.charAt(0) != '\t' && !scoreString.startsWith("SERVER"))
            scoreString = scoreString
                    .substring(scoreString.indexOf("\n") + 1, scoreString.length());
        // skip initial tab
        scoreString = scoreString.substring(1, scoreString.length());
  
        // 5 sets
        int pos = 0;
        for (int i = 0; i < 5; i++) {
            playerTwoGames[i]= Integer.parseInt(scoreString.substring(0, scoreString.indexOf("\t")));
            scoreString = scoreString
                    .substring(scoreString.indexOf("\t") + 1, scoreString.length());
            
            if (scoreString.startsWith("\n") || 
                scoreString.startsWith("\t") || 
                scoreString.startsWith(" "))
        		{ pos = i+1; i = 5; scoreString = scoreString.trim();}
        }
       
        // Points
        playerTwoGames[pos] = Integer.parseInt(scoreString.substring(0, 2));
        // //////////////END of Player 2 data
        
        Score score = new Score();
        score.setSets(playerOneGames, playerTwoGames);
        match.setScore(score);
        sc.setScores();
    }
    
    public void handleUpdate() {
        try {
            Score score = this.getScore();
            System.out.println("Fetched score");
            if (score != null) {
                System.out.println("Not null! Updating...");
                parseScores();
                System.out.println("Completed!!!!!!");
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
    
    private String skipLines(String string, int count) {
        while (count-- > 0) {
            string = string
                    .substring(string.indexOf('\n') + 1, string.length());
        }
        return string;
    }
    
    private String skipEmptyLines(String string) {
        while (string.charAt(0) == '\t' || string.charAt(0) == '\n'
                || string.startsWith(" ") || string.charAt(0) == '\t') {
            string = string.substring(1);
        }
        return string;
    }

	@Override
	protected void runBody() {
		try {
            this.scoreString = extractScores();
        } catch (Exception e) {
        }

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
	}

}