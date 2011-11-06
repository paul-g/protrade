package org.ic.tennistrader.demo.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Utilities used for inflating the decompressed betfair API calls
public class DecompressionUtils {

	private DecompressionUtils() {
		// prevent instantiation
	}

	// read a boolean value from the string.
	public static boolean readBoolean(StringReader reader, char delimiter) {
		String bool = readString(reader, delimiter); 
		return bool.equalsIgnoreCase("Y") || bool.equalsIgnoreCase("true");
	}

	// read an int value from the string.
	public static int readInt(StringReader reader, char delimiter) throws NumberFormatException {
		String str = readString(reader, delimiter);
		if (str.length() == 0){
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}
	
	// read a double value from the string.
	public static double readDouble(StringReader reader, char delimiter) throws NumberFormatException {
		String str = readString(reader, delimiter);
		if (str.length() == 0){
			return 0.0d;
		} else {
			return Double.parseDouble(str);
		}
	}

	// read a long value from the string.
	public static long readLong(StringReader reader, char delimiter) throws NumberFormatException {
		String str = readString(reader, delimiter);
		if (str.length() == 0){
			return 0;
		} else {
			return Long.parseLong(str);
		}
	}

	// read a date value from the string.
	public static Date readDate(StringReader reader, char delimiter) throws ParseException {
		String date = readString(reader, ',');
		try {
			return new SimpleDateFormat("H.mm").parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Malformed Compressed Market Data: Could not parse the date from "+date);
		}
	}
	// Read a string from the data at the current index.
	public static String readString(StringReader sr, char delimiter) {
		try {
			StringBuilder sb = new StringBuilder();
			char c;
			while ((c = (char)sr.read()) != (char)-1) {
				if (c == '\\') {
					// This is an escape character. Jump past to the next char.
					sb.append((char)sr.read());
				} else if (c == delimiter) {
					break;
				}
				else {
					sb.append(c);
				}
			}
			return sb.toString();
		} catch (IOException e) {
			// Cannot happen as there is no IO here - just a read from a string
			throw new RuntimeException("Unexpected IOException", e);
		}

	}
}
