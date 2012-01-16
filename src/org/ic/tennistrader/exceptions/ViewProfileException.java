package org.ic.tennistrader.exceptions;

public class ViewProfileException extends Exception {
    private static final long serialVersionUID = 1L;

    public ViewProfileException() {
        super("View Profile fetch failed");
    }
    
    public ViewProfileException(String msg) {
        super("View Profile fetch failed - " + msg);
    }
}
