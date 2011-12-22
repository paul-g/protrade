package org.ic.tennistrader.authentication;

import org.ic.tennistrader.exceptions.LoginFailedException;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;
import org.ic.tennistrader.ui.LoginShell;

public class BetfairAuthenticator {

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

}
