package org.ic.tennistrader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.ui.LoginShell;
import org.ic.tennistrader.ui.MainWindow;
import org.ic.tennistrader.utils.Colours;
import org.ic.tennistrader.authentication.BetfairAuthenticator;
import org.ic.tennistrader.authentication.Encrypt;

public final class Main {

	public static final String TITLE = "Tennis Trader";

	private static final Logger log = Logger.getLogger(Main.class);

	public static String username = "";
	public static String password = "";

	public static String testUsername = "";
	public static String testPassword = "";

	private Main() {
	}

	public static void main(String[] args) {

		// read the config file
		readAndDecryptConfigFile();

		// start up the app
		final Display display = new Display();
		final MainWindow mw = new MainWindow(display);
		Colours.setColors(display);

		if (args.length == 1) {
			if ("-test".equals(args[0])) {
				startMainWindow(display, mw);
			} else if ("-testb".equals(args[0])) {
				String username = Main.testUsername;
				String password = Main.testPassword;
				log.info("username " + username);
				Main.username = Main.testUsername;
				Main.password = Main.testPassword;
				if (BetfairAuthenticator.checkLogin(username, password)) {
					startMainWindow(display, mw);
				} else {
					// login failed
				}
			}
		} else {

			final LoginShell ls = new LoginShell(display);

			mw.addLoadListener(new Listener() {
				@Override
				public void handleEvent(Event event) {
					if ("Done!".equals(event.text)) {
						ls.finishProgressBar();
						ls.dispose();
					} else {
						ls.updateProgressBar(10);
						ls.setText(event.text);
					}
				}
			});

			ls.addLoginSuccessListener(new Listener() {
				@Override
				public void handleEvent(Event arg0) {
					startMainWindow(display, mw);
				}
			});

			ls.run(display);
		}
	}

	public static void readAndDecryptConfigFile() {
		String filename = "config.local";
		String line;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileInputStream(filename));
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String[] lines = line.split(":=");

				String name = lines[0];
				String value = lines[1];

				log.info("split " + name + " " + value);

				if ("username".equals(name)) {
					testUsername = value;
				} else if ("password".equals(name)) {
					testPassword = Encrypt.decrypt(value);
				}
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			log.error("HAVE YOU ADDED A config.local FILE IN THE PROJECT ROOT?");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		log.info("username set to \'" + username + "\' password set");
	}

	public static String getTestUsername() {
		if ("".equals(testUsername)) {
			readAndDecryptConfigFile();
		}
		return testUsername;
	}

	public static String getTestPassword() {
		if ("".equals(testPassword)) {
			readAndDecryptConfigFile();
		}
		return testPassword;
	}

	private static void startMainWindow(final Display display,
			final MainWindow mw) {
		mw.show();
		mw.run(display);
	}

}
