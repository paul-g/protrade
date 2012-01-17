package org.ic.protrade.model.connection;

import java.io.FileInputStream;
import java.util.Scanner;

import org.ic.protrade.authentication.Encrypt;
import org.ic.protrade.data.market.connection.BetfairConnectionHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public abstract class BetfairConnectionTest {
	protected boolean setUp;
	protected static final String SETUP_FAILED_MESSAGE = "Betfair Connection test set up failed";
	protected String username;
	
	@Before
	public void setUp() {
		username = null;
		String password = null;
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
				if (name.equals("username"))
					username = value;
				else if (name.equals("password")) {
					password = Encrypt.decrypt(value);
				}
			}
			BetfairConnectionHandler.login(username, password);
			setUp = true;
		} catch (Exception e) {
			setUp = false;
		}
	}
	
	@After
	public void tearDown() {
		try {
			BetfairConnectionHandler.logout();
		} catch (Exception e) {
		}
	}
}
