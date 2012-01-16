package org.ic.tennistrader.scrappers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.ic.tennistrader.domain.match.Player;

public class SiteParser {

	protected String skipEmptyLines(String string) {
		while (string.charAt(0) == '\t' || string.charAt(0) == '\n'
				|| string.startsWith(" ") || string.charAt(0) == '\t') {
			string = string.substring(1);
		}
		return string;
	}

	protected String skipLines(String string, int count) {
		while (count-- > 0) {
			string = string
					.substring(string.indexOf('\n') + 1, string.length());
		}
		return string;
	}

	private Image getImage(String url) {
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

	public Image getPlayerImage(Player player) {
		int index = 0;
		String imagePlayer = "";
		String cPlayer = player.toString();
		while (cPlayer.indexOf(' ') > -1) {
			imagePlayer += cPlayer.substring(index, cPlayer.indexOf(' '))
					+ "%20";
			cPlayer = cPlayer.substring(cPlayer.indexOf(' ') + 1,
					cPlayer.length());
		}
		imagePlayer += cPlayer;
		String site = "http://www.tennisinsight.com/images/" + imagePlayer
				+ ".jpg";
		try {
			URL url = new URL(site);
			URLConnection conn = url.openConnection();
			conn.connect();
			return getImage(site);
		} catch (MalformedURLException e) {
			// the URL is not in a valid form
			return getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg");
		} catch (IOException e) {
			// the connection couldn't be established
			return getImage("http://www.tennisinsight.com/images/default_thumbnail.jpg");
		}
	}
}
