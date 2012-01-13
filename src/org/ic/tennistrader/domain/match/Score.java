package org.ic.tennistrader.domain.match;

import static org.ic.tennistrader.domain.match.PlayerEnum.casePlayer;

import java.util.ArrayList;
import java.util.List;

import org.ic.tennistrader.exceptions.MatchNotFinishedException;

public class Score {

	public static final int AD = 50;

	private int playerOnePoints;
	private int playerTwoPoints;

	private final List<SetScore> scores = new ArrayList<SetScore>();
	private SetScore currentSet;

	private int maximumSetsPlayed = 3;

	private PlayerEnum server;

	public PlayerEnum getServer() {
		return server;
	}

	public void setServer(PlayerEnum server) {
		this.server = server;
	}

	public Score() {
		init();
	}

	public Score(int maximumSetsPlayed) {
		init();
		this.maximumSetsPlayed = maximumSetsPlayed;
	}

	private void init() {
		playerOnePoints = 0;
		playerTwoPoints = 0;
		currentSet = new SetScore();
		scores.add(currentSet);
	}

	private void addPlayerPoint(PlayerEnum player) {
		if (isFinished())
			return;

		int playerPoints = (player.equals(PlayerEnum.PLAYER1) ? playerOnePoints
				: playerTwoPoints);

		if (currentSet.isTiebreak())
			gameWon(player);

		switch (playerPoints) {
		case 0:
		case 15:
			playerPoints += 15;
			break;
		case 30:
			playerPoints += 10;
			break;
		case 40:
			if (opponentScore(player) < 40) {
				gameWon(player);
				return;
			} else {
				if (opponentScore(player) == AD)
					playerOnePoints = playerTwoPoints = 40;
				else
					playerPoints = AD;
			}
			;
			break;
		case 50:
			gameWon(player);
			return;
		}

		switch (player) {

		case PLAYER1:
			playerOnePoints = playerPoints;
			break;
		case PLAYER2:
			playerTwoPoints = playerPoints;
			break;
		}

	}

	public void addPlayerOnePoint() {
		addPlayerPoint(PlayerEnum.PLAYER1);
	}

	public void addPlayerTwoPoint() {
		addPlayerPoint(PlayerEnum.PLAYER2);
	}

	private void gameWon(PlayerEnum player) {
		if (player.isPlayerOne()) {
			currentSet.addPlayerOneGame();
		} else {
			currentSet.addPlayerTwoGame();
		}

		playerOnePoints = 0;
		playerTwoPoints = 0;

		if (currentSet.isFinished()) {
			SetScore newSet = new SetScore();
			scores.add(newSet);
			currentSet = newSet;
		}

	}

	private Integer opponentScore(PlayerEnum player) {
		return casePlayer(player, playerTwoPoints, playerOnePoints);
	}

	public SetScore getCurrentSetScore() {
		return currentSet;
	}

	public int getPlayerPoints(PlayerEnum player) {
		return casePlayer(player, playerOnePoints, playerTwoPoints);
	}

	public int getPlayerOnePoints() {
		return playerOnePoints;
	}

	public int getPlayerTwoPoints() {
		return playerTwoPoints;
	}

	public SetScore getSetScore(int setNumber) {
		if (setNumber <= scores.size() && setNumber >= 1)
			return scores.get(setNumber - 1);
		else
			return null;
	}

	public boolean isFinished() {
		int win = maximumSetsPlayed / 2 + 1;
		if (getPlayerOneSets() >= win)
			return true;
		if (getPlayerTwoSets() >= win)
			return true;
		return false;
	}

	public int getPlayerOneSets() {
		return getPlayerSets(PlayerEnum.PLAYER1);
	}

	public int getPlayerTwoSets() {
		return getPlayerSets(PlayerEnum.PLAYER2);
	}

	public int getPlayerSets(PlayerEnum player) {

		int setsWon = 0;

		for (SetScore s : scores)
			if (s.getWinner() == player)
				setsWon++;

		return setsWon;
	}

	public int[] getPlayerOneScore() {
		return getPlayerScores(PlayerEnum.PLAYER1);
	}

	public int[] getPlayerTwoScore() {
		return getPlayerScores(PlayerEnum.PLAYER2);
	}

	public int[] getPlayerScores(PlayerEnum player) {

		int playerScores[] = new int[maximumSetsPlayed];
		for (int i = 0; i < playerScores.length; i++)
			playerScores[i] = 0;

		int i = 0;

		for (SetScore s : scores) {
			playerScores[i] = (player == PlayerEnum.PLAYER1 ? s
					.getPlayerOneGames() : s.getPlayerTwoGames());
			i++;
		}

		return playerScores;
	}

	public int getMaximumSetsPlayed() {
		return maximumSetsPlayed;
	}

	public void setPlayerOnePoints(int playerOnePoints) {
		this.playerOnePoints = playerOnePoints;
	}

	public void setPlayerTwoPoints(int playerTwoPoints) {
		this.playerTwoPoints = playerTwoPoints;
	}

	public void setSets(int[] playerOneGames, int[] playerTwoGames) {
		scores.clear();
		for (int i = 0; i < maximumSetsPlayed; i++) {
			SetScore sc = new SetScore(playerOneGames[i], playerTwoGames[i]);
			scores.add(sc);
		}
		currentSet = scores.get(maximumSetsPlayed - 1);
	}

	public void setSetsVariable(int[] playerOneGames, int[] playerTwoGames) {
		scores.clear();
		for (int i = 0; i < playerOneGames.length; i++) {
			SetScore sc = new SetScore(playerOneGames[i], playerTwoGames[i]);
			scores.add(sc);
		}
		currentSet = scores.get(playerOneGames.length - 1);
	}

	public PlayerEnum getWinner() throws MatchNotFinishedException {
		if (!this.isFinished())
			throw new MatchNotFinishedException();
		if (getPlayerOneSets() > maximumSetsPlayed / 2)
			return PlayerEnum.PLAYER1;
		else
			return PlayerEnum.PLAYER2;
	}

	public int getSetsPlayed() {
		return getPlayerOneSets() + getPlayerTwoSets();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Score) {
			return ((Score) object).toString().equals(toString());
		}
		return false;
	}
}