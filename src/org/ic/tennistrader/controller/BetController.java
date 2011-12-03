package org.ic.tennistrader.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.ic.tennistrader.model.BetManager;
import org.ic.tennistrader.ui.updatable.OddsButton;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.exceptions.OddsButtonNotFoundException;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class BetController {
	private List<OddsButton> player1BackButtons;
	private List<OddsButton> player1LayButtons;
	private List<OddsButton> player2BackButtons;
	private List<OddsButton> player2LayButtons;
	private Match match;
	private static Logger log = Logger.getLogger(BetController.class);
	
	public BetController(List<OddsButton> player1Backbuttons, List<OddsButton> player1LayButtons, 
			List<OddsButton> player2BackButtons, List<OddsButton> player2LayButtons, Match match) {
		this.player1BackButtons = player1Backbuttons;
		this.player1LayButtons = player1LayButtons;
		this.player2BackButtons = player2BackButtons;
		this.player2LayButtons = player2LayButtons;
		this.match = match;
	}
		
    public void addBet(OddsButton button, double amount, double odds) {
        PlayerEnum player;
        /*
        Double maxAmount = button.getAmount();
        if (amount > maxAmount) {
            // do smth
        } else {
        */
            try {
                player = getBetPlayer(button);
                BetTypeEnum betType = getBetType(button);
                BetManager.placeBet(match, player, betType, odds, amount);
            } catch (OddsButtonNotFoundException e) {
                log.error(e.getMessage());
            }
        //}
    }

	private PlayerEnum getBetPlayer(OddsButton button) throws OddsButtonNotFoundException {
		PlayerEnum player;
		if (this.player1BackButtons.contains(button)) {
			player = PlayerEnum.PLAYER1;
		} else if (this.player1LayButtons.contains(button)) {
			player = PlayerEnum.PLAYER1;
		} else if (this.player2BackButtons.contains(button)) {
			player = PlayerEnum.PLAYER2;
		} else if (this.player2LayButtons.contains(button)){
			player = PlayerEnum.PLAYER2;
		} else {
		    throw new OddsButtonNotFoundException();
		}
		return player;
	}

	public BetTypeEnum getBetType(OddsButton button) throws OddsButtonNotFoundException {
		BetTypeEnum betType;
		if (this.player1BackButtons.contains(button)) {
			betType = BetTypeEnum.B;
		} else if (this.player1LayButtons.contains(button)) {
			betType = BetTypeEnum.L;
		} else if (this.player2BackButtons.contains(button)) {
			betType = BetTypeEnum.B;
		} else if (this.player2LayButtons.contains(button)){
			betType = BetTypeEnum.L;
		} else {
		    throw new OddsButtonNotFoundException();
		}
		return betType;
	}

	public String getBettingDetails(OddsButton button)
			throws OddsButtonNotFoundException {
		String info = (getBetType(button) == BetTypeEnum.B ? "Back: " : "Lay: ")
				+ "You are betting on "
				+ (getBetPlayer(button).equals(PlayerEnum.PLAYER1) ? match
						.getPlayerOne().toString() : match.getPlayerTwo()
						.toString());
		info += " to "
				+ (getBetType(button) == BetTypeEnum.B ? "win." : "lose.");
		return info;
	}
}
