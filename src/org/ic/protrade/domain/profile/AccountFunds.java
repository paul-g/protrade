package org.ic.protrade.domain.profile;

import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.GetAccountFundsResp;

public class AccountFunds {
    private double balance;
    private double available;
    private double creditLimit;
    private double betfairPoints;
    private double exposure;
    private double exposureLimit;

    public AccountFunds() {
    }
    
    public AccountFunds(GetAccountFundsResp funds) {
        this.balance = funds.getBalance();
        this.available = funds.getAvailBalance();
        this.creditLimit = funds.getCreditLimit();
        this.betfairPoints = funds.getCurrentBetfairPoints();
        this.exposure = funds.getExposure();
        this.exposureLimit = funds.getExpoLimit();
    }
    
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getBetfairPoints() {
        return betfairPoints;
    }

    public void setBetfairPoints(double betfairPoints) {
        this.betfairPoints = betfairPoints;
    }

    public double getExposure() {
        return exposure;
    }

    public void setExposure(double exposure) {
        this.exposure = exposure;
    }

    public double getExposureLimit() {
        return exposureLimit;
    }

    public void setExposureLimit(double exposureLimit) {
        this.exposureLimit = exposureLimit;
    }
}