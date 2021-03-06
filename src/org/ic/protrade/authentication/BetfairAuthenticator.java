package org.ic.protrade.authentication;

import org.apache.log4j.Logger;
import org.ic.protrade.data.exceptions.LoginFailedException;
import org.ic.protrade.data.market.connection.BetfairConnectionHandler;
import org.ic.protrade.ui.login.LoginListener;
import org.ic.protrade.ui.login.LoginResponse;
import org.ic.protrade.ui.login.LoginShell;

public class BetfairAuthenticator {

	public static Logger log = Logger.getLogger(BetfairAuthenticator.class);

	public static boolean checkLogin(String username, String password) {
		// Perform the login
		try {
			BetfairConnectionHandler.login(username, password);
		} catch (LoginFailedException e) {
			LoginShell.log.info(e.getMessage());
			return false;
		}

		LoginShell.log.info("Login succeeded with token - "
				+ BetfairConnectionHandler.getApiContext().getToken());

		return true;
	}

	public static void checkLogin(final String username, final String password,
			final LoginListener loginListener) {
		// Perform the login
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					BetfairConnectionHandler.login(username, password);
					loginListener.handleLogin(LoginResponse.SUCCESS);
					LoginShell.log.info("Login succeeded with token - "
							+ BetfairConnectionHandler.getApiContext()
									.getToken());
				} catch (LoginFailedException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					loginListener.handleLogin(LoginResponse.FAIL);
				}
			}
		});
		t.start();
	}

}
