package org.ic.protrade.exceptions;

public class EndOfFracsoftFileException extends Exception {
	private static final long serialVersionUID = 1L;

	public EndOfFracsoftFileException() {
		super("Reached end of fracsoft file");
	}
}
