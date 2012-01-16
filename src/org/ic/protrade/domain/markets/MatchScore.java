package org.ic.protrade.domain.markets;

public class MatchScore {
	private String firstPlayerLastName;
	private int firstPlayerScore, secondPlayerScore;	

	public MatchScore(int firstPlayerScore, int secondPlayerScore) {
		this.firstPlayerScore = firstPlayerScore;
		this.secondPlayerScore = secondPlayerScore;
	}
	
	public static MatchScore getMatchScore(String scoreName) {
		char i = 0;//runnerName.charAt(0);
		while (!Character.isDigit(scoreName.charAt(i)))
			i++;
		String lastName = scoreName.substring(0, i - 1);
		int firstScore = Integer.parseInt(((Character)scoreName.charAt(i)).toString());
		i++;
		while (!Character.isDigit(scoreName.charAt(i)))
			i++;
		int secondScore = Integer.parseInt(((Character)scoreName.charAt(i)).toString());
		MatchScore matchScore = new MatchScore(firstScore, secondScore);
		matchScore.setFirstPlayerLastName(lastName);
		return matchScore;
	}

	public int getSecondPlayerScore() {
		return secondPlayerScore;
	}

	public int getFirstPlayerScore() {
		return firstPlayerScore;
	}

	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (this.getClass() != object.getClass())
			return false;
		MatchScore matchScore = (MatchScore) object;
		return this.firstPlayerScore == matchScore.getFirstPlayerScore()
				&& this.secondPlayerScore == matchScore.getSecondPlayerScore();
	}

	public int hashCode() {
		return this.firstPlayerScore * 10 + this.secondPlayerScore;
	}
	
	public String toString() {
		return this.firstPlayerScore + ", " + this.secondPlayerScore + " ";
	}

	public void setFirstPlayerLastName(String firstPlayerLastName) {
		this.firstPlayerLastName = firstPlayerLastName;
	}

	public String getFirstPlayerLastName() {
		return firstPlayerLastName;
	}
}
