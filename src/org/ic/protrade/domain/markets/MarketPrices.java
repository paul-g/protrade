package org.ic.protrade.domain.markets;

import java.util.ArrayList;

import org.ic.protrade.utils.Pair;

public class MarketPrices {
	private ArrayList<Pair<Double, Double>> backPrices, layPrices;

	public MarketPrices() {
		this.backPrices = new ArrayList<Pair<Double, Double>>();
		this.layPrices = new ArrayList<Pair<Double, Double>>();
	}

	public MarketPrices(ArrayList<Pair<Double, Double>> backPrices,
			ArrayList<Pair<Double, Double>> layPrices) {
		this.backPrices = backPrices;
		this.layPrices = layPrices;
	}
	
	public void addBackPrice(Pair<Double, Double> newPrice) {
		this.backPrices.add(newPrice);
	}
	
	public void addLayPrice(Pair<Double, Double> newPrice) {
		this.layPrices.add(newPrice);
	}

	public void setLayPrices(ArrayList<Pair<Double, Double>> layPrices) {
		this.layPrices = layPrices;
	}

	public ArrayList<Pair<Double, Double>> getLayPrices() {
		return layPrices;
	}

	public void setBackPrices(ArrayList<Pair<Double, Double>> backPrices) {
		this.backPrices = backPrices;
	}

	public ArrayList<Pair<Double, Double>> getBackPrices() {
		return backPrices;
	}
	
	public String toString() {
		String msg = "Back prices: ";
		for (Pair<Double, Double> price : backPrices) {
			msg += price.second() + " at " + price.first() + " ";
		}
		msg += "Lay prices: ";
		for (Pair<Double, Double> price : layPrices) {
			msg += price.second() + " at " + price.first() + " ";
		}
		return msg;
	}
}
