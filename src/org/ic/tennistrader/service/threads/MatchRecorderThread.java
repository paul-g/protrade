package org.ic.tennistrader.service.threads;

import org.apache.log4j.Logger;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.MatchCsvWriter;

public class MatchRecorderThread extends MatchThread{
    
    private MatchCsvWriter writer;
    
    private final Logger log = Logger.getLogger(MatchRecorderThread.class);
    
    public MatchRecorderThread(Match match){
        super(match);
        writer = new MatchCsvWriter(match, "data/recorded/" + match.toString()+".csv");
    }
    
    protected void runBody() {
        writer.writeMatchDetails();
        
        log.info("Wrote match info to file");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.info("Match Updater thread interupted");
        }
    }
    
    @Override
    public void setStop(){
        this.stop = true;
        
        writer.close();
    }
}
