package org.ic.protrade.model.connection;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// The inflated version of the maket prices returned by the getMarketPricesCompressed API call
public class InflatedMarketPrices {
	// General market information
	private int marketId;
	private String currency;
	private String marketStatus;
	private int inPlayDelay;
	private int numberofWinners;
	private String marketInformation;
	private boolean discountAllowed;
	private String marketBaseRate;
	private long refreshTime;
	List<InflatedRemovedRunner> removedRunners = new ArrayList<InflatedRemovedRunner>();
	List<InflatedRunner> runners = new ArrayList<InflatedRunner>();
	boolean bSPMarket;
	
	public InflatedMarketPrices(String compressedData) {
		StringReader reader = new StringReader(compressedData);

		// Debugging info
		String field = null;
		try {
			field = "marketId";
			marketId = DecompressionUtils.readInt(reader, '~');

			field = "currency";
			currency = DecompressionUtils.readString(reader, '~');
			
			field = "marketStatus";
			marketStatus = DecompressionUtils.readString(reader, '~');
			
			field = "inPlayDelay";
			inPlayDelay = DecompressionUtils.readInt(reader, '~');
			
			field = "numberofWinners";
			numberofWinners = DecompressionUtils.readInt(reader, '~');
			
			field = "marketInformation";
			marketInformation = DecompressionUtils.readString(reader, '~');
			
			field = "discountAllowed";
			discountAllowed = DecompressionUtils.readBoolean(reader, '~');
			
			field = "marketBaseRate";
			marketBaseRate = DecompressionUtils.readString(reader, '~');
			
			field = "refreshTime";
			refreshTime = DecompressionUtils.readLong(reader, '~');
			
			field = "removedRunners";
			parseRemovedRunners(DecompressionUtils.readString(reader, '~'));
			
			field = "bSPMarket";
			bSPMarket = DecompressionUtils.readBoolean(reader, ':');
			
			// Retrieve each runner and it's associated info.
			field = "runners";
			parseRunners(DecompressionUtils.readString(reader, (char)0)); // read to end of string
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Malformed Compressed Market Data: Could not read "+field+" from string: "+compressedData);
		}
	}
	
	public int getMarketId() {
		return marketId;
	}

	public String getCurrency() {
		return currency;
	}

	public String getMarketStatus() {
		return marketStatus;
	}

	public int getInPlayDelay() {
		return inPlayDelay;
	}

	public int getNumberofWinners() {
		return numberofWinners;
	}

	public String getMarketInformation() {
		return marketInformation;
	}

	public boolean isDiscountAllowed() {
		return discountAllowed;
	}

	public String getMarketBaseRate() {
		return marketBaseRate;
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public List<InflatedRemovedRunner> getRemovedRunners() {
		return removedRunners;
	}

	public List<InflatedRunner> getRunners() {
		return runners;
	}

	public boolean isBSPMarket() {
		return bSPMarket;
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
				runners.add(new InflatedRunner(result));
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
	public static class InflatedRunner {
		private int selectionId;
		private int orderIndex;
		private double totalAmountMatched;
		private double lastPriceMatched;
		private double handicap;
		private double reductionFactor;
		private boolean vacant;
		private double farSPPrice;
		private double nearSPPrice;
		private double actualSPPrice;
		private List<InflatedPrice> backPrices = new ArrayList<InflatedPrice>(3);
		private List<InflatedPrice> layPrices = new ArrayList<InflatedPrice>(3);

		private InflatedRunner(String data) {
			// Get the main part of the runner info
			int pipeIndex = data.indexOf('|');
			StringReader reader = new StringReader(data.substring(0, pipeIndex)+ "~"); // Add a delimiter to the end so the reader never blocks
			selectionId = DecompressionUtils.readInt(reader, '~');
			orderIndex = DecompressionUtils.readInt(reader, '~');
			totalAmountMatched = DecompressionUtils.readDouble(reader, '~');
			lastPriceMatched = DecompressionUtils.readDouble(reader, '~');
			handicap = DecompressionUtils.readDouble(reader, '~');
			reductionFactor = DecompressionUtils.readDouble(reader, '~');
			vacant = DecompressionUtils.readBoolean(reader, '~');
			farSPPrice = DecompressionUtils.readDouble(reader, '~');
			nearSPPrice = DecompressionUtils.readDouble(reader, '~');
			actualSPPrice = DecompressionUtils.readDouble(reader, '~'); 
			
			// Now grab the lay & back prices
			int pipeIndexNext = data.indexOf('|', pipeIndex+1);
			try {
				StringReader backReader = new StringReader(data.substring(pipeIndex+1, pipeIndexNext));
				while (backReader.ready()) {
					InflatedPrice p = new InflatedPrice(backReader);
					if (p.getPrice() == 0.0) {
						// reached the end of the stream.
						break;
					}
					backPrices.add(p);
				}
				StringReader layReader = new StringReader(data.substring(pipeIndexNext+1));
				while (layReader.ready()) {
					InflatedPrice p = new InflatedPrice(layReader);
					if (p.getPrice() == 0.0) {
						// reached the end of the stream.
						break;
					}
					layPrices.add(p);
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

		public double getFarSPPrice() {
			return farSPPrice;
		}

		public double getNearSPPrice() {
			return nearSPPrice;
		}

		public double getActualSPPrice() {
			return actualSPPrice;
		}

		public List<InflatedPrice> getBackPrices() {
			return backPrices;
		}

		public List<InflatedPrice> getLayPrices() {
			return layPrices;
		}
		
	}
	// Information about an available price in the system
	public static class InflatedPrice {
		private double price;
		private double amountAvailable;
		@SuppressWarnings("unused")
		private String type;
		private int depth;

		private InflatedPrice(StringReader reader) {
			price = DecompressionUtils.readDouble(reader, '~');
			amountAvailable = DecompressionUtils.readDouble(reader, '~');
			type = DecompressionUtils.readString(reader, '~');
			depth = DecompressionUtils.readInt(reader, '~');
		}
		
		public double getPrice() {
			return price;
		}
		public double getAmountAvailable() {
			return amountAvailable;
		}
		public int getDepth() {
			return depth;
		}
		
	}
}
