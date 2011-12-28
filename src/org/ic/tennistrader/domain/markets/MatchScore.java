package org.ic.tennistrader.domain.markets;

public class MatchScore {
	private String firstPlayerLastName;
	private int firstPlayerScore, secondPlayerScore;

	public MatchScore(int firstPlayerScore, int secondPlayerScore) {
		this.firstPlayerScore = firstPlayerScore;
		this.secondPlayerScore = secondPlayerScore;
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
