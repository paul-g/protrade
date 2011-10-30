package src;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import src.service.FracsoftReader;
import src.ui.LoginShell;
import src.utils.Encrypt;

public class Main {

  public static final String TITLE = "Tennis Trader";

  private static Logger log = Logger.getLogger(Main.class);
  
  public static String USERNAME = "";
  public static String PASSWORD = "";
  
  public static void main(String[] args) {
    // read the config file
    readConfigFile();
    
    // create a fracsoft reader
    try { 
        new FracsoftReader("fracsoft-data/fracsoft1.csv");
    } catch (Exception e){
      log.error(e.getMessage());
    }
    
    // start up the app
    final Display display = new Display();
    new LoginShell(display);
  }

  private static void readConfigFile(){
    String filename = "config.local";
    String line;
    Scanner scanner = null;
    try {
      scanner = new Scanner(new FileInputStream(filename));
      while (scanner.hasNextLine()){
        line = scanner.nextLine();
        String[] lines = line.split(":=");
        
        String name = lines[0];
        String value = lines[1];
        
        log.info("split " + name + " " + value);
        
        if (name.equals("username"))
          USERNAME = value;
        else if (name.equals("password")) {
          PASSWORD = Encrypt.decrypt(value);
        }
      }
    } catch (FileNotFoundException e){
      log.error(e.getMessage());
      log.error("HAVE YOU ADDED A config.local FILE IN THE PROJECT ROOT?");
    } catch (GeneralSecurityException gse) {
    
    } catch (IOException ioe) {
    }
    
    finally{
      if (scanner != null)
        scanner.close();
    } 
    
    log.info("username set to \'" + USERNAME + "\' password set to \'" + PASSWORD + "\'" );
  }

}
