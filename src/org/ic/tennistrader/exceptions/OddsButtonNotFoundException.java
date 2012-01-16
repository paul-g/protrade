package org.ic.tennistrader.exceptions;

public class OddsButtonNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public OddsButtonNotFoundException() {
        super("Odds button was not found");
    }

}
