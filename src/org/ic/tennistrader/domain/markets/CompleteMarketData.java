package org.ic.tennistrader.domain.markets;

public class CompleteMarketData {
	private MOddsMarketData mOddsMarketData;
	private SetBettingMarketData setBettingMarketData;
	
	public CompleteMarketData() {
		;
	}
	
	public CompleteMarketData(MOddsMarketData mOdds, SetBettingMarketData setBetting) {
		this.mOddsMarketData = mOdds;
		this.setBettingMarketData = setBetting;
	}

	public void setmOddsMarketData(MOddsMarketData mOddsMarketData) {
		this.mOddsMarketData = mOddsMarketData;
	}

	public MOddsMarketData getmOddsMarketData() {
		return this.mOddsMarketData;
	}

	public void setSetBettingMArketData(SetBettingMarketData setBettingMarketData) {
		this.setBettingMarketData = setBettingMarketData;
	}

	public SetBettingMarketData getSetBettingMArketData() {
		return this.setBettingMarketData;
	}
}
