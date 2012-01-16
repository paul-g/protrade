package org.ic.protrade.exceptions;

public class LoginFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	public LoginFailedException(String msg) {
		super("Login failed - " + msg);
	}
}
