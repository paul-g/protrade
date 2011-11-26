package org.ic.tennistrader.service.threads;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.ic.tennistrader.domain.match.Match;

public class MatchRecorderThread extends MatchThread{
    
    private String filename = null;
    private BufferedWriter out;
    
    public MatchRecorderThread(String filename, Match match){
        super(match);
        
        this.filename = filename;
        
        openFile();
    }
    
    protected void runBody() {
        
    }
    
    private void openFile(){
        try {
            // Create file 
            FileWriter fstream = new FileWriter(filename);
            out = new BufferedWriter(fstream);
            out.write("Starting to record match ");
            out.write("Timestamp,Inplay delay,Market status,Selection ID,Selection name,BP1,BV1,BP2,BV2,BP3,BV3,LP1,LV1,LP2,LV2,LP3,LV3,Total matched,LPM, Set1, Set2, Set3, Points \n");
            //Close the output stream
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("IO error writing headers: " + ioe.getMessage());
        }
    }
    
}
