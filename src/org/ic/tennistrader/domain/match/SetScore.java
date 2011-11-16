package org.ic.tennistrader.domain.match;

public class SetScore {

    private int playerOneGames;
    private int playerTwoGames;
    
    private int tiebreakPointsPlayerOne;
    private int tiebreakPointsPlayerTwo;

    private boolean tiebreak;

    public void addPlayerOneGame() {
        if (!isFinished()) {
            if (!isTiebreak()) {
                playerOneGames++;
                tiebreak = checkTiebreak();
            } else {
                tiebreakPointsPlayerOne++;
                if (tiebreakPointsPlayerOne >= 7 && Math.abs(tiebreakPointsPlayerOne - tiebreakPointsPlayerTwo) >= 2)
                        playerOneGames++;
            }
        }
    }
    
    public void addPlayerOneGames(int games){
        for (int i=0;i<games; i++)
           this.addPlayerOneGame();
    }
    
    public void addPlayerTwoGames(int games){
        for (int i=0;i<games; i++)
           this.addPlayerTwoGame();
    }

    public void addPlayerTwoGame() {
        if (!isFinished()) {
            if (!isTiebreak()) {
                playerTwoGames++;
                tiebreak = checkTiebreak();
            } else {
                tiebreakPointsPlayerTwo++;
                if (tiebreakPointsPlayerTwo >= 7 && Math.abs(tiebreakPointsPlayerOne - tiebreakPointsPlayerTwo) >= 2)
                        playerTwoGames++;
            }
        }
    }

    public boolean isFinished() {
        // found bug - (>= 6)
        return (playerOneGames == 7 || playerTwoGames == 7)
                || (Math.abs(playerOneGames - playerTwoGames) >= 2 && (playerOneGames >=6 || playerTwoGames >=6 ));
    }
    
    public boolean isTiebreak(){
        return tiebreak;
    }
    
    private boolean checkTiebreak(){
        return (playerOneGames == playerTwoGames && playerOneGames == 6);
    }

}
