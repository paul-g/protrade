package org.ic.tennistrader.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class RandomDataGenerator {    
    private static BufferedWriter out;
    
    private static final int BEFORE_PLAY_COUNT = 1000;
    private static final int AFTER_PLAY_COUNT  = 1000;
    private static final int TOTAL_COUNT = 5600;
    
    private static long initialTimestamp;
    private static long currentTimestamp;
    
    private static int inPlayDelay = 0;
    
    private static final int SELCTION_ID_PL1 = 2249229;
    private static final int SELCTION_ID_PL2 = 2251410;
    
    private static final String PL1_NAME = "Novak Djokovic";
    private static final String PL2_NAME = "Rafael Nadal"; 
    
    private static final double INITIAL_SEED = 1.66; 
    
    private static final double PL1_INITIAL_BACK_SEED = 1.66;
    private static final double PL1_INITIAL_LAY_SEED = 1.66;
    private static final double PL2_INITIAL_BACK_SEED = 5.72;
    private static final double PL2_INITIAL_LAY_SEED = 5.72;

    private static int totalMatched = 0;
    private static double lpm = 1.6;
    
    private static int playerOneGames[] = new int[3];
    private static int playerTwoGames[] = new int[3];
    
    private static int playerOnePoints = 0;
    private static int playerTwoPoints = 0;
    
    
    //TODO: enum
    private static final String MARKET_ACTIVE = "ACTIVE";
    private static final String MARKET_SUSPENDED = "SUSPENDED";
    
    private static String marketStatus;

    public static void main (String args[]){
        
        initialTimestamp = Calendar.getInstance().getTimeInMillis();
        currentTimestamp = initialTimestamp;
        
        writeHeaders();
        init();
        
        int i = 0;
        
        for (;i<BEFORE_PLAY_COUNT;i++){
            makeNewEntry();
        }
        
        setMatchInPlay();
        
        for (;i<TOTAL_COUNT - AFTER_PLAY_COUNT; i++ ){
            makeNewEntry();
        }
        
        setMatchFinished();
        
        for (;i<TOTAL_COUNT;i++){
            makeNewEntry();
        }
        
        try { 
            out.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    private static void init() {
        marketStatus = MARKET_ACTIVE;
        
        for (int i=0;i<playerOneGames.length;i++){
            playerOneGames[i] = 0;
            playerTwoGames[i] = 0;
        }
    }

    private static void setMatchFinished() {
        // TODO : not sure about this 
        marketStatus = MARKET_SUSPENDED;
    }

    private static void setMatchInPlay() {
        // equivalent to saying match is in play
        inPlayDelay = 5;
    }

    private static void makeNewEntry(){
        writeLine(generateLine(true));
        writeLine(generateLine(false));
    }
    
    private static String generateLine(boolean player1){
        //return "1315828903864,0,ACTIVE,2249229,Novak Djokovic,1.66,1054.07,1.65,81228.71,1.64,27237.41,1.67,27104.33,1.68,17129.58,1.69,3856.4,794149.1,1.66,";
        String backOdds = makeCsvWithoutTermination(makeOdds());
        
        String layOdds  = makeCsvWithoutTermination(makeOdds());
        
        String playerName = (player1? PL1_NAME : PL2_NAME );
        String selectionId = (player1? SELCTION_ID_PL1 : SELCTION_ID_PL2) + "";
        
        String games  = makeCsvWithoutTermination(getScoreAsString(player1));
        
        String points = makeCsvWithoutTermination(getPointsAsString(player1)); 
        
        String line = makeCsv(currentTimestamp + "", inPlayDelay + "", marketStatus, selectionId, playerName, backOdds, layOdds, totalMatched + "", lpm + "", games, points);
        
        update();
        
        return line;
    }
    
    private static void update() {
        
        currentTimestamp+=1000;
        
        
    }

    private static String getPointsAsString(boolean player1) {
        return (player1? playerOnePoints : playerTwoPoints) + "";
    }

    private static String[] getScoreAsString(boolean player1) {
        String [] scoresString = new String[3];
        int [] scores = (player1? playerOneGames : playerTwoGames);
        for (int i=0;i<scores.length;i++)
             scoresString[i] = scores[i] + "";
        return scoresString;
    }

    private static String makeCsvWithoutTermination(String... args) {
        String string  = makeCsv(args);
        return string.substring(0, string.length() -1);
    }

    private static void writeHeaders(){
        String filename = "full-data/fulldata1.dat";
        try {
            // Create file 
            FileWriter fstream = new FileWriter(filename);
            out = new BufferedWriter(fstream);
            out.write("Timestamp,Inplay delay,Market status,Selection ID,Selection name,BP1,BV1,BP2,BV2,BP3,BV3,LP1,LV1,LP2,LV2,LP3,LV3,Total matched,LPM, Set1, Set2, Set3, Points \n");
            //Close the output stream
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("IO error writing headers: " + ioe.getMessage());
        }
    }
    
    private static void writeLine(String line){
        try {
            // Create file 
            out.write(line+"\n");
            //Close the output stream
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("IO error writing line: \'" + line + "\' error: "  + ioe.getMessage());
        }
        
    }
    
    private static String makeCsv(String... args){
        String csvLine = "";
        for (String s: args) {
            csvLine+=s + ",";
        }
        return csvLine;
    }
    
    private static String[] makeOdds(){
        String [] odds = new String[6];
        for (int i=0;i<5;i+=2){
            // the odd
            odds[i] = INITIAL_SEED+"";
            // the available amount
            odds[i+1] = 23456 + "";
        }
        return odds;
    }
}
