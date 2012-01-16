package org.ic.protrade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.protrade.authentication.BetfairAuthenticator;
import org.ic.protrade.authentication.Encrypt;
import org.ic.protrade.ui.login.LoginShell;
import org.ic.protrade.ui.main.DashboardWindow;
import org.ic.protrade.ui.main.MainWindow;
import org.ic.protrade.ui.main.StandardWindow;

public final class Main {

	public static final String TITLE = "Tennis Trader";

	private static final Logger log = Logger.getLogger(Main.class);

	private static final String DASHBOARD_FLAG = "-d";

	private static final String BYPASS_FLAG = "-b";

	private static final String TEST_FLAG = "-t";

	public static String username = "";
	public static String password = "";

	public static String testUsername = "";
	public static String testPassword = "";

	private static boolean dashboardOn = false;

	private static boolean bypassOn = false;

	private static boolean testOn = false;

	private Main() {
	}

	public static void main(String[] args) {

		// read the config file
		log.info("Reading config file");
		readAndDecryptConfigFile();
		log.info("Read config file");

		// start up the app
		final Display display = new Display();
		final MainWindow mw;

		for (String string : args) {
			if (string.equals(DASHBOARD_FLAG)) {
				dashboardOn = true;
			} else if (string.equals(BYPASS_FLAG)) {
				bypassOn = true;
			} else if (string.equals(TEST_FLAG)) {
				testOn = true;
			}
		}

		log.info("Parsed args -> dashboard: " + dashboardOn + " bypass: "
				+ bypassOn + " test: " + testOn);

		if (dashboardOn) {
			mw = makeDashboardApplicationWindow(display);
		} else {
			mw = makeApplicationWindow(display);
		}

		if (bypassOn) {
			Main.username = Main.testUsername;
			Main.password = Main.testPassword;
			if (BetfairAuthenticator.checkLogin(testUsername, testPassword)) {
				startMainWindow(display, mw);
			} else {
				// login failed
			}
		} else if (testOn) {
			startMainWindow(display, mw);
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

	private static MainWindow makeApplicationWindow(final Display display) {
		return new StandardWindow(display);
	}

	private static MainWindow makeDashboardApplicationWindow(
			final Display display) {
		return DashboardWindow.getInstance();
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
