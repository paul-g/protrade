package org.ic.tennistrader;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.ic.tennistrader.ui.LoginShell;
import org.ic.tennistrader.ui.MainWindow;
import org.ic.tennistrader.authentication.Encrypt;

public class Main {

  public static final String TITLE = "Tennis Trader";

  private static Logger log = Logger.getLogger(Main.class);
  
  public static String USERNAME = "";
  public static String PASSWORD = "";
  
  public static String TEST_USERNAME =  "";
  public static String TEST_PASSWORD =  "";
  
  public static void main(String[] args) {
    // read the config file
    readConfigFile();
    
    // start up the app
    final Display display = new Display();
    final LoginShell ls = new LoginShell(display);
    ls.addLoginSuccessListener(new Listener() {
        @Override
        public void handleEvent(Event arg0) {
            new MainWindow(display, ls).run(display);
        }
    });
    ls.run(display);
    
  }

  public static void readConfigFile(){
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
          TEST_USERNAME = value;
        else if (name.equals("password")) {
          TEST_PASSWORD = Encrypt.decrypt(value);
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
    
    log.info("username set to \'" + USERNAME + "\' password set");
  }

}
