package org.ic.tennistrader.exceptions;

public class MaximumBetAmountExceededException extends Exception {
    private static final long serialVersionUID = 1L;

    public MaximumBetAmountExceededException() {
        super("The bet amount exceeds the maximum amount which can be matched");
    }
}
