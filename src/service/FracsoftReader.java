package src.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * Reads data in Fracsoft format from a given file
 * 
 * @author Paul Grigoras
 * 
 */
public class FracsoftReader {
  
  private static Logger log = Logger.getLogger(FracsoftReader.class);
  
  private List<FracsoftMatchData> matchDataList = new ArrayList<FracsoftMatchData>();

  public FracsoftReader(String filename) throws FileNotFoundException {
    log.info("Creating fracsoft reader from file " + filename);
    String line;
    Scanner scanner = null;
      
    try {
      scanner = new Scanner(new FileInputStream(filename));
      
      // skip file header
      for (int i=0;i<5;i++) {
        scanner.nextLine();
      }

      // start parsing data
      while (scanner.hasNextLine()) {
        line = scanner.nextLine();
        String[] lines = line.split(",");

        //log.info("split " + lines[0] + " " + lines[4] + " " + lines[5]);
        
        matchDataList.add(new FracsoftMatchData(lines[0], lines[4], lines[5]));

      }
    } catch (FileNotFoundException e) {
      log.error(e.getMessage());
    }

    finally {
      if (scanner != null)
        scanner.close();
    }
  }
  
}
