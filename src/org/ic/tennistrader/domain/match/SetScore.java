package org.ic.tennistrader.domain.match;

public class SetScore {

    private int playerOneGames;
    private int playerTwoGames;

    private int tiebreakPointsPlayerOne;
    private int tiebreakPointsPlayerTwo;
    
    private PlayerEnum winner;
    
    public SetScore(){}
    
    public SetScore(int playerOneGames, int playerTwoGames){
        this.playerOneGames = playerOneGames;
        this.playerTwoGames = playerTwoGames;
    }

    private void addPlayerGame(PlayerEnum player) {

        boolean isPlayerOne = player.equals(PlayerEnum.PLAYER1);
        int games = (isPlayerOne ? playerOneGames : playerTwoGames);
        int tiebreakPoints = (isPlayerOne ? tiebreakPointsPlayerOne
                : tiebreakPointsPlayerTwo);

        if (!isFinished()) {
            if (!isTiebreak()) {
                games++;
            } else {
                tiebreakPoints++;
                if (tiebreakPoints >= 7
                        && Math.abs(tiebreakPoints - opponentTieScore(player)) >= 2)
                    games++;
            }

            switch (player) {
                case PLAYER1:
                    tiebreakPointsPlayerOne = tiebreakPoints;
                    playerOneGames = games;
                    break;
                case PLAYER2:
                    tiebreakPointsPlayerTwo = tiebreakPoints;
                    playerTwoGames = games;
                    break;
            }
        }
        
        if (isFinished())
            winner = player;

    }
    
    private int opponentTieScore(PlayerEnum player){
        switch (player){
            case PLAYER1: return tiebreakPointsPlayerTwo;
            case PLAYER2: return tiebreakPointsPlayerOne;
        }
        return 0;
    }

    public void addPlayerOneGame() {
        addPlayerGame(PlayerEnum.PLAYER1);
    }

    public void addPlayerTwoGame() {
        addPlayerGame(PlayerEnum.PLAYER2);
    }

    public void addPlayerOneGames(int games) {
        for (int i = 0; i < games; i++)
            this.addPlayerOneGame();
    }

    public void addPlayerTwoGames(int games) {
        for (int i = 0; i < games; i++)
            this.addPlayerTwoGame();
    }

    public boolean isFinished() {
        // found bug - (>= 6)
        return (playerOneGames == 7 || playerTwoGames == 7)
                || (Math.abs(playerOneGames - playerTwoGames) >= 2 && (playerOneGames >= 6 || playerTwoGames >= 6));
    }

    public boolean isTiebreak() {
        return (playerOneGames == playerTwoGames && playerOneGames == 6);
    }

    public int getPlayerOneGames() {
        return playerOneGames;
    }

    public int getPlayerTwoGames() {
        return playerTwoGames;
    }

    public int getTiebreakPointsPlayerOne() {
        return tiebreakPointsPlayerOne;
    }

    public int getTiebreakPointsPlayerTwo() {
        return tiebreakPointsPlayerTwo;
    }
    
    public PlayerEnum getWinner(){
        return winner;
    }
}
