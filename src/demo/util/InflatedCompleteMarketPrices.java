package src.demo.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// The inflated version of the maket prices returned by the getMarketPricesCompressed API call
public class InflatedCompleteMarketPrices {
	// General market information
	private int marketId;
	private int inPlayDelay;
	List<InflatedRemovedRunner> removedRunners = new ArrayList<InflatedRemovedRunner>();
	List<InflatedCompleteRunner> runners = new ArrayList<InflatedCompleteRunner>();
	
	public InflatedCompleteMarketPrices(String compressedData) {
		StringReader reader = new StringReader(compressedData);

		// Debugging info
		String field = null;
		try {
			field = "marketId";
			marketId = DecompressionUtils.readInt(reader, '~');

			field = "inPlayDelay";
			inPlayDelay = DecompressionUtils.readInt(reader, '~');
			
			field = "removedRunners";
			parseRemovedRunners(DecompressionUtils.readString(reader, ':'));

			// Retrieve each runner and it's associated info.
			field = "runners";
			parseRunners(DecompressionUtils.readString(reader, (char)0)); // read to end of string
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Malformed Compressed Complete Market Data: Could not read "+field+" from string: "+compressedData);
		}
	}
	
	public int getMarketId() {
		return marketId;
	}

	public int getInPlayDelay() {
		return inPlayDelay;
	}


	public List<InflatedRemovedRunner> getRemovedRunners() {
		return removedRunners;
	}

	public List<InflatedCompleteRunner> getRunners() {
		return runners;
	}


	//////////////////////////////////////////////////////////////////////////////
	// Utility methods
	//////////////////////////////////////////////////////////////////////////////
	private void parseRemovedRunners(String data) {
		if (data.length() > 0) {
			StringReader rrReader = new StringReader(data);
			while (true) {
				String result = DecompressionUtils.readString(rrReader, ';');
				if(result.length() == 0) {
					break;
				}
				removedRunners.add(new InflatedRemovedRunner(result));
			}
		}
	}
	
	private void parseRunners(String data) {
		if (data.length() > 0) {
			StringReader rrReader = new StringReader(data);
			while (true) {
				String result = DecompressionUtils.readString(rrReader, ':');
				if(result.length() == 0) {
					break;
				}
				runners.add(new InflatedCompleteRunner(result));
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// Placeholder classes
	//////////////////////////////////////////////////////////////////////////////
	
	// Information about a runner removed from the event
	public static class InflatedRemovedRunner {
		private String name;
		private Date removedDate;
		private String adjustmentFactor;
		
		private InflatedRemovedRunner(String data) {
			StringReader rrReader = new StringReader(data + ","); // Add a delimiter to the end so the reader never blocks
			name = DecompressionUtils.readString(rrReader, ',');
			try {
				removedDate = DecompressionUtils.readDate(rrReader, ',');
			} catch (ParseException e) {
				throw new IllegalArgumentException("Malformed Compressed Market Data: Could not read date from string: "+data);
			}

			// providing null reads to the end of the string
			adjustmentFactor = DecompressionUtils.readString(rrReader, ','); 
		}

		public String getName() {
			return name;
		}

		public Date getRemovedDate() {
			return removedDate;
		}

		public String getAdjustmentFactor() {
			return adjustmentFactor;
		}
		
	}
	
	// Information about an active runner
	public static class InflatedCompleteRunner {
		private int selectionId;
		private int orderIndex;
		private double totalAmountMatched;
		private double lastPriceMatched;
		private double handicap;
		private double reductionFactor;
		private boolean vacant;
		private int asianLineId;
		private double farSPPrice;
		private double nearSPPrice;
		private double actualSPPrice;
		private List<InflatedCompletePrice> prices = new ArrayList<InflatedCompletePrice>();

		private InflatedCompleteRunner(String data) {
			// Get the main part of the runner info
			int priceStart = data.indexOf('|');
			StringReader reader = new StringReader(data.substring(0, priceStart)+ "~"); // Add a delimiter to the end so the reader never blocks
			selectionId = DecompressionUtils.readInt(reader, '~');
			orderIndex = DecompressionUtils.readInt(reader, '~');
			totalAmountMatched = DecompressionUtils.readDouble(reader, '~');
			lastPriceMatched = DecompressionUtils.readDouble(reader, '~');
			handicap = DecompressionUtils.readDouble(reader, '~');
			reductionFactor = DecompressionUtils.readDouble(reader, '~');
			vacant = DecompressionUtils.readBoolean(reader, '~');
			asianLineId = DecompressionUtils.readInt(reader, '~');
			farSPPrice = DecompressionUtils.readDouble(reader, '~');
			nearSPPrice = DecompressionUtils.readDouble(reader, '~');
			actualSPPrice = DecompressionUtils.readDouble(reader, '~'); 
			
			// Now grab the prices
			
			try {
				StringReader priceReader = new StringReader(data.substring(priceStart+1));
				while (priceReader.ready()) {
					InflatedCompletePrice p = new InflatedCompletePrice(priceReader);
					if (p.getPrice() == 0.0) {
						// reached the end of the stream.
						break;
					}
					prices.add(p);
				}

			} catch (IOException e) {
				// Cannot happen as there is no IO here - just a read from a string
				throw new RuntimeException("Unexpected IOException", e);
			}
			
		}

		public int getSelectionId() {
			return selectionId;
		}

		public int getOrderIndex() {
			return orderIndex;
		}

		public double getTotalAmountMatched() {
			return totalAmountMatched;
		}

		public double getLastPriceMatched() {
			return lastPriceMatched;
		}

		public double getHandicap() {
			return handicap;
		}

		public double getReductionFactor() {
			return reductionFactor;
		}

		public boolean isVacant() {
			return vacant;
		}
		
		public int getAsianLineId()	{
			return asianLineId;
		}

		public double getFarSPPrice() {
			return farSPPrice;
		}

		public double getNearSPPrice() {
			return nearSPPrice;
		}

		public double getActualSPPrice() {
			return actualSPPrice;
		}

		public List<InflatedCompletePrice> getPrices() {
			return prices;
		}
		
	}
	// Information about an available price in the system
	public static class InflatedCompletePrice {
		private double price;
		private double backAmountAvailable;
		private double layAmountAvailable;
		private double totalBSPLayLiability;
		private double totalBSPBackersStake;
		
		private InflatedCompletePrice(StringReader reader) {
			price = DecompressionUtils.readDouble(reader, '~');
			backAmountAvailable = DecompressionUtils.readDouble(reader, '~');
			layAmountAvailable = DecompressionUtils.readDouble(reader, '~');
			totalBSPLayLiability = DecompressionUtils.readDouble(reader, '~');
			totalBSPBackersStake = DecompressionUtils.readDouble(reader, '~');
			
		}
		
		public double getPrice() {
			return price;
		}
		public double getBackAmountAvailable() {
			return backAmountAvailable;
		}
		public double getLayAmountAvailable() {
			return layAmountAvailable;
		}
		public double getTotalBSPLayLiability() {
			return totalBSPLayLiability;
		}
		public double getTotalBSPBackersStake() {
			return totalBSPBackersStake;
		}
		
	}
}
