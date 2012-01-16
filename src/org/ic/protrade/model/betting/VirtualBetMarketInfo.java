package org.ic.protrade.model.betting;

import java.util.HashMap;

public class VirtualBetMarketInfo {
	// amounts available to match on the market
	private HashMap<Double, Double> backFirstPlayerAvailableMatches;
	private HashMap<Double, Double> layFirstPlayerAvailableMatches;
	private HashMap<Double, Double> backSecondPlayerAvailableMatches;
	private HashMap<Double, Double> laySecondPlayerAvailableMatches;
	// amount already bet by the user
	private HashMap<Double, Double> matchedBackBetsFirstPlayer;
	private HashMap<Double, Double> matchedLayBetsFirstPlayer;
	private HashMap<Double, Double> matchedBackBetsSecondPlayer;
	private HashMap<Double, Double> matchedLayBetsSecondPlayer;
	// possible profits considering bets so far
	private double firstPlayerWinnerProfit;
	private double secondPlayerWinnerProfit;
	private double firstPlayerWinnerLiability;
	private double secondPlayerWinnerLiability;

	public VirtualBetMarketInfo() {
		backFirstPlayerAvailableMatches = new HashMap<Double, Double>();
		layFirstPlayerAvailableMatches = new HashMap<Double, Double>();
		backSecondPlayerAvailableMatches = new HashMap<Double, Double>();
		laySecondPlayerAvailableMatches = new HashMap<Double, Double>();
		matchedBackBetsFirstPlayer = new HashMap<Double, Double>();
		matchedLayBetsFirstPlayer = new HashMap<Double, Double>();
		matchedBackBetsSecondPlayer = new HashMap<Double, Double>();
		matchedLayBetsSecondPlayer = new HashMap<Double, Double>();
		this.firstPlayerWinnerLiability = this.secondPlayerWinnerLiability = 0;
	}

	public HashMap<Double, Double> getBackFirstPlayerAvailableMatches() {
		return backFirstPlayerAvailableMatches;
	}

	public void setBackFirstPlayerAvailableMatches(
			HashMap<Double, Double> backFirstPlayerAvailableMatches) {
		this.backFirstPlayerAvailableMatches = backFirstPlayerAvailableMatches;
	}

	public void addBackFirstPlayerAvailableMatches(Double odds, Double amount) {
		this.backFirstPlayerAvailableMatches.put(odds, amount);
	}

	public HashMap<Double, Double> getLayFirstPlayerAvailableMatches() {
		return layFirstPlayerAvailableMatches;
	}

	public void setLayFirstPlayerAvailableMatches(
			HashMap<Double, Double> layFirstPlayerAvailableMatches) {
		this.layFirstPlayerAvailableMatches = layFirstPlayerAvailableMatches;
	}

	public void addLayFirstPlayerAvailableMatches(Double odds, Double amount) {
		this.layFirstPlayerAvailableMatches.put(odds, amount);
	}

	public HashMap<Double, Double> getBackSecondPlayerAvailableMatches() {
		return backSecondPlayerAvailableMatches;
	}

	public void setBackSecondPlayerAvailableMatches(
			HashMap<Double, Double> backSecondPlayerAvailableMatches) {
		this.backSecondPlayerAvailableMatches = backSecondPlayerAvailableMatches;
	}

	public void addBackSecondPlayerAvailableMatches(Double odds, Double amount) {
		this.backSecondPlayerAvailableMatches.put(odds, amount);
	}

	public HashMap<Double, Double> getLaySecondPlayerAvailableMatches() {
		return laySecondPlayerAvailableMatches;
	}

	public void setLaySecondPlayerAvailableMatches(
			HashMap<Double, Double> laySecondPlayerAvailableMatches) {
		this.laySecondPlayerAvailableMatches = laySecondPlayerAvailableMatches;
	}

	public void addLaySecondPlayerAvailableMatches(Double odds, Double amount) {
		this.laySecondPlayerAvailableMatches.put(odds, amount);
	}

	public HashMap<Double, Double> getMatchedBackBetsFirstPlayer() {
		return matchedBackBetsFirstPlayer;
	}

	public void addMatchedBackBetsFirstPlayer(Double odds, Double amount) {
		Double previousAmount = this.matchedBackBetsFirstPlayer.get(odds);
		if (previousAmount == null)
			previousAmount = 0.0;
		this.matchedBackBetsFirstPlayer.put(odds, previousAmount + amount);
		this.secondPlayerWinnerLiability += amount;
	}

	public HashMap<Double, Double> getMatchedLayBetsFirstPlayer() {
		return matchedLayBetsFirstPlayer;
	}

	public void addMatchedLayBetsFirstPlayer(Double odds, Double amount) {
		Double previousAmount = this.matchedLayBetsFirstPlayer.get(odds);
		if (previousAmount == null)
			previousAmount = 0.0;
		this.matchedLayBetsFirstPlayer.put(odds, previousAmount + amount);
		//System.out.println("Adding liability for first pl" + (odds - 1) * amount);
		this.firstPlayerWinnerLiability += (odds - 1) * amount;
	}

	public HashMap<Double, Double> getMatchedBackBetsSecondPlayer() {
		return matchedBackBetsSecondPlayer;
	}

	public void addMatchedBackBetsSecondPlayer(Double odds, Double amount) {
		Double previousAmount = this.matchedBackBetsSecondPlayer.get(odds);
		if (previousAmount == null)
			previousAmount = 0.0;
		this.matchedBackBetsSecondPlayer.put(odds, previousAmount + amount);
		//System.out.println("Adding liability for first pl" + amount);
		this.firstPlayerWinnerLiability += amount;
	}

	public HashMap<Double, Double> getMatchedLayBetsSecondPlayer() {
		return matchedLayBetsSecondPlayer;
	}

	public void addMatchedLayBetsSecondPlayer(Double odds, Double amount) {
		Double previousAmount = this.matchedLayBetsSecondPlayer.get(odds);
		if (previousAmount == null)
			previousAmount = 0.0;
		this.matchedLayBetsSecondPlayer.put(odds, previousAmount + amount);
		this.secondPlayerWinnerLiability += (odds - 1) * amount;
	}

	public void setFirstPlayerWinnerProfit(double firstPlayerWinnerProfit) {
		this.firstPlayerWinnerProfit = firstPlayerWinnerProfit;
	}

	public double getFirstPlayerWinnerProfit() {
		return firstPlayerWinnerProfit;
	}

	public void addFirstPlayerWinnerProfit(double newProfit) {
		this.firstPlayerWinnerProfit += newProfit;
	}

	public void setSecondPlayerWinnerProfit(double secondPlayerWinnerProfit) {
		this.secondPlayerWinnerProfit = secondPlayerWinnerProfit;
	}

	public double getSecondPlayerWinnerProfit() {
		return secondPlayerWinnerProfit;
	}

	public void addSecondPlayerWinnerProfit(double newProfit) {
		this.secondPlayerWinnerProfit += newProfit;
	}

	public double getLiability() {
		if (this.firstPlayerWinnerLiability > this.secondPlayerWinnerLiability)
			return this.firstPlayerWinnerLiability;
		return this.secondPlayerWinnerLiability;
	}
}