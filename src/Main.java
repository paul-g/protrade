package src;


import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import src.domain.*;
import src.exceptions.LoginFailedException;
import src.service.BetfairConnectionHandler;
import src.ui.DisplayPanel;
import src.ui.LoginShell;
import src.ui.MenuPanel;
import src.ui.NavigationPanel;
import src.ui.ToolBarPanel;

public class Main {

  public static final String TITLE = "Tennis Trader";

  private static Logger log = Logger.getLogger(Main.class);
  
  public static String USER = "";

  public static void main(String[] args) {
    
    final Display display = new Display();
    new LoginShell(display);
  }

  
  private static void printEvents(int level, EventMarketBetfair event) {
      String msg = "";
      for(int i = 0 ; i < level; i++)
        msg += "\t";
      msg += event.toString();
      log.info(msg);
      for (EventMarketBetfair e : event.getChildren()) {
        printEvents(level + 1, e);
      }
    
    /*
    for (Tournament t : tournaments) {
      log.info(t.toString());
      for (EventMarketBetfair m : t.getChildren()) {
        log.info("\t " + m.toString());
      }
    }
    */
  }
}
