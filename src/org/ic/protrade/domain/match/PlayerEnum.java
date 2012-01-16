package org.ic.protrade.domain.match;

public enum PlayerEnum {
        PLAYER1, PLAYER2;
        
        /**
         * If the first argument is PLAYER1 returns the second argument else returns the third argument.
         * 
         * @param player
         * @param ifPlayerOneReturnValue - the return value if player is PlayerEnum.PLAYER1
         * @param ifPlayerTwoReturnValue - the return value if player is PlayerEnum.PLAYER2
         * @return If the first argument is PLAYER1 returns the second argument else returns the third argument.
         */
        public static <T> T casePlayer(PlayerEnum player, T ifPlayerOneReturnValue, T ifPlayerTwoReturnValue){
        	if (player == PLAYER1) 
        		return ifPlayerOneReturnValue;
        	return ifPlayerTwoReturnValue;
        }
        
        public boolean isPlayerOne(){
        	return (this.equals(PLAYER1));
        }
        
        public boolean isPlayerTwo(){
        	return (this.equals(PLAYER2));
        }
}
