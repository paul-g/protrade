package org.ic.tennistrader.score;

public class SiteParser {

	protected String skipEmptyLines(String string) {
	    while (string.charAt(0) == '\t' || string.charAt(0) == '\n'
	            || string.startsWith(" ") || string.charAt(0) == '\t') {
	        string = string.substring(1);
	    }
	    return string;
	}

	protected String skipLines(String string, int count) {
	    while (count-- > 0) {
	        string = string
	                .substring(string.indexOf('\n') + 1, string.length());
	    }
	    return string;
	}

}
