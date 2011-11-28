package org.ic.tennistrader.controller;

import java.util.List;
import org.ic.tennistrader.model.BetManager;
import org.ic.tennistrader.ui.updatable.OddsButton;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class BetController {
	private List<OddsButton> player1BackButtons;
	private List<OddsButton> player1LayButtons;
	private List<OddsButton> player2BackButtons;
	private List<OddsButton> player2LayButtons;
	private Match match;
	
	public BetController(List<OddsButton> player1Backbuttons, List<OddsButton> player1LayButtons, 
			List<OddsButton> player2BackButtons, List<OddsButton> player2LayButtons, Match match) {
		this.player1BackButtons = player1Backbuttons;
		this.player1LayButtons = player1LayButtons;
		this.player2BackButtons = player2BackButtons;
		this.player2LayButtons = player2LayButtons;
		this.match = match;
	}
		
	public void addBet(OddsButton button, double amount, double odds) {
		Player player = getBetPlayer(button);
		BetTypeEnum betType = getBetType(button);		
		BetManager.placeBet(match, player, betType, odds, amount);
	}

	private Player getBetPlayer(OddsButton button) {
		Player player;
		if (this.player1BackButtons.contains(button)) {
			player = match.getPlayerOne();
		} else if (this.player1LayButtons.contains(button)) {
			player = match.getPlayerOne();
		} else if (this.player2BackButtons.contains(button)) {
			player = match.getPlayerTwo();
		} else {
			player = match.getPlayerTwo();
		}
		return player;
	}

	private BetTypeEnum getBetType(OddsButton button) {
		BetTypeEnum betType;
		if (this.player1BackButtons.contains(button)) {
			betType = BetTypeEnum.B;
		} else if (this.player1LayButtons.contains(button)) {
			betType = BetTypeEnum.L;
		} else if (this.player2BackButtons.contains(button)) {
			betType = BetTypeEnum.B;
		} else {
			betType = BetTypeEnum.L;
		}
		return betType;
	}

	public String getBettingDetails(OddsButton button) {	
		String info = "You are betting on " + getBetPlayer(button).toString();
		info += " to " + (getBetType(button) == BetTypeEnum.B ? "win." : "lose.");		
		return info;
	}
}
