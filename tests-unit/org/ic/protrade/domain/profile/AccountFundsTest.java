package org.ic.protrade.domain.profile;

import static org.junit.Assert.assertEquals;

import org.ic.protrade.data.market.connection.AccountFunds;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.GetAccountFundsResp;
import org.junit.Test;

public class AccountFundsTest {
	@Test
	public void testAccountFunds() {
		GetAccountFundsResp resp = new GetAccountFundsResp();
        resp.setBalance(2);
        resp.setAvailBalance(3);
        resp.setCreditLimit(54);
        resp.setCurrentBetfairPoints(10);
        resp.setExposure(100);
        resp.setExpoLimit(300);
		AccountFunds accountFunds = new AccountFunds(resp);
		assertEquals(resp.getBalance(), accountFunds.getBalance(), Math.pow(10, -5));
		assertEquals(resp.getAvailBalance(), accountFunds.getAvailable(), Math.pow(10, -5));
		assertEquals(resp.getCreditLimit(), accountFunds.getCreditLimit(), Math.pow(10, -5));
		assertEquals(resp.getCurrentBetfairPoints(), accountFunds.getBetfairPoints(), Math.pow(10, -5));
		assertEquals(resp.getExposure(), accountFunds.getExposure(), Math.pow(10, -5));
		assertEquals(resp.getExpoLimit(), accountFunds.getExposureLimit(), Math.pow(10, -5));		
	}
}
