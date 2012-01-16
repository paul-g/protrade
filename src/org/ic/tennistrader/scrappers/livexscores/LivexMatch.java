package org.ic.tennistrader.scrappers.livexscores;

public class LivexMatch {

	private String player1;
	private String player2;
	private String player1Score;
	private String player2Score;

	public LivexMatch(String player1, String player2, String player1Score,
			String player2Score) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
	}

	public String getPlayer1Score() {
		return player1Score;
	}

	public void setPlayer1Score(String player1Score) {
		this.player1Score = player1Score;
	}

	public String getPlayer2Score() {
		return player2Score;
	}

	public void setPlayer2Score(String player2Score) {
		this.player2Score = player2Score;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	@Override
	public String toString() {
		String match = "\n";
		match += makeLine(player1, player1Score);
		match += makeLine(player2, player2Score);
		return match;
	}

	private String makeLine(String string1, String string2) {
		return string1 + "\t\t\t" + string2 + "\n";
	}
}
