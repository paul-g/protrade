package org.ic.tennistrader.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RandomDataGenerator {
    
    private static BufferedWriter out;

    public static void main (String args[]){
        int timpestamp, delay, marketStatus;
        
        String line = generateLine();
        
        writeHeaders();
        
        writeLine(line);
        
        try { 
            out.close();
        } catch (IOException ioe){
            
        }
    }
    
    private static String generateLine(){
        return "1315828903864,0,ACTIVE,2249229,Novak Djokovic,1.66,1054.07,1.65,81228.71,1.64,27237.41,1.67,27104.33,1.68,17129.58,1.69,3856.4,794149.1,1.66,";
    }
    
    private static void writeHeaders(){
        String filename = "full-data/fulldata1.dat";
        try {
            // Create file 
            FileWriter fstream = new FileWriter(filename);
            out = new BufferedWriter(fstream);
            out.write("Timestamp,Inplay delay,Market status,Selection ID,Selection name,BP1,BV1,BP2,BV2,BP3,BV3,LP1,LV1,LP2,LV2,LP3,LV3,Total matched,LPM");
            //Close the output stream
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("IO error writing headers: " + ioe.getMessage());
        }
    }
    
    private static void writeLine(String line){
        try {
            // Create file 
            out.write(line+"/n");
            //Close the output stream
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("IO error writing line: \'" + line + "\' error: "  + ioe.getMessage());
        }
        
    }
}
