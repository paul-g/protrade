package org.ic.protrade.scrappers.livexscores;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.Player;
import org.ic.protrade.domain.match.PlayerEnum;
import org.ic.protrade.domain.match.Score;
import org.ic.protrade.service.threads.MatchUpdaterThread;

import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

public class ScoreUpdateThread extends MatchUpdaterThread {

	private Score prevScore;

	private final List<Listener> scoreListener = new ArrayList<Listener>();

	private Player player1;
	private Player player2;

	private static Logger log = Logger.getLogger(ScoreUpdateThread.class);

	public ScoreUpdateThread(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public ScoreUpdateThread(Match match) {
		this.match = match;
	}

	public Score getScore() {
		return match.getScore();
	}

	/*
	 * Gets real-time scores from http://www.livexscores.com/ Emulates a firefox
	 * browser with javascript and AJAX enabled and fetches score data from
	 * website. Finally, it returns the data for further parsing
	 */
	@SuppressWarnings("serial")
	public String extractScores() throws Exception {

		// Create a webClient to emulate Firefox browser
		WebClient webClient = new WebClient();// BrowserVersion.FIREFOX_3_6);

		// Customize all webclient listeners and handlers for no
		webClient.setIncorrectnessListener(new IncorrectnessListener() {
			@Override
			public void notify(String string, Object object) {
			}
		});
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

			@Override
			public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void scriptException(HtmlPage arg0, ScriptException arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void malformedScriptURL(HtmlPage arg0, String arg1,
					MalformedURLException arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
				// TODO Auto-generated method stub

			}
		});

		webClient.setAjaxController(new NicelyResynchronizingAjaxController() {
			@Override
			public boolean processSynchron(HtmlPage page, WebRequest request,
					boolean async) {
				return true;
			}
		});

		HtmlElement scores = null;

		// webClient.waitForBackgroundJavaScript(3000);
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
			iter.next().setTextContent("\nSERVER");
		}

		String returnStr = "";
		// Getting the relevant scores
		Iterator<DomNode> itr = scores.getChildren().iterator();
		while (itr.hasNext()) {
			itr.next();
			HtmlElement elem = (HtmlElement) itr.next();
			Iterator<DomNode> itr2 = elem.getChildren().iterator();
			while (itr2.hasNext()) {
				DomNode elem2 = itr2.next();
				returnStr += elem2.asText();
			}
		}

		log.info("Extracted score");

		return returnStr;
	}

	@Override
	protected void runBody() {
		try {
			String scoreString = extractScores();
			System.out.println("Score thread - Requesting new scores.");
			if (scoreString != null) {
				String prevScore = match.getScoreAsString(PlayerEnum.PLAYER1)
						+ match.getScoreAsString(PlayerEnum.PLAYER2);
				new ScoreParser(scoreString, this.match).parseAndSetScores();
				String newScore = match.getScoreAsString(PlayerEnum.PLAYER1)
						+ match.getScoreAsString(PlayerEnum.PLAYER2);
				if (!newScore.equals(prevScore)) {
					System.out.println("Score changed!: prev: \'" + prevScore
							+ "\' \'" + newScore + "\'");
					handleUpdate();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void addListener(Listener l) {
		scoreListener.add(l);
	}

	private void handleUpdate() {
		for (Listener l : scoreListener) {
			l.handleEvent(new Event());
		}
	}
}