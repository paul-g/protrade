package org.ic.protrade.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.domain.match.Match;
import org.ic.protrade.domain.match.PlayerEnum;

public class MatchCsvWriter {

    private BufferedWriter out;
    private Match match;

    private final Logger log = Logger.getLogger(MatchCsvWriter.class);

    public MatchCsvWriter(Match match, String filename) {
        this.match = match;
        openFile(filename);
    }

    public void writeMatchDetails() {
        try {
            out.write(makePlayerLine(PlayerEnum.PLAYER1));
            out.write(makePlayerLine(PlayerEnum.PLAYER2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String makePlayerLine(PlayerEnum player) {
        MOddsMarketData data = match.getLastMarketData();
        String line = "";
        if (data != null) {
            // "Timestamp,Inplay delay,Market status,Selection ID,Selection name,BP1,BV1,BP2,BV2,BP3,BV3,LP1,LV1,LP2,LV2,LP3,LV3,Total matched,LPM, Set1, Set2, Set3, Points \n";
            line += Calendar.getInstance().getTimeInMillis() + ",";

            line += data.getDelay() + "," + data.getMatchStatus() + ",";
            line += (player.equals(PlayerEnum.PLAYER1) ? data.getPlayer1SelectiondId() : data.getPlayer2SelectionId()) + ",";
            line += match.getPlayer(player).toString() + ",";
            line += data.getBackValues(player) + ",";
            line += data.getLayValues(player) + ",";
            line += data.getLastPriceMatched(player) + ",";
            line += match.getScoreAsString(player);
            line += "\n";
        }

        return line;
    }

    private void openFile(String filename) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(filename);
            out = new BufferedWriter(fstream);
            out.write("Timestamp,Inplay delay,Market status,Selection ID,Selection name,BP1,BV1,BP2,BV2,BP3,BV3,LP1,LV1,LP2,LV2,LP3,LV3,Total matched,LPM, Set1, Set2, Set3, Points \n");
            // Close the output stream
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("IO error writing headers: "
                    + ioe.getMessage());
        }
    }

    public void close() {
        log.info("Closed output buffer");
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
