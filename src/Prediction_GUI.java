import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.custom.*;
import java.io.*;

public class Prediction_GUI {
  public void run() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(null);
    shell.setText("Tennis Predictor");
    //shell.setBounds(new Rectangle(0, 0, 400, 400));
    //shell.setSize(new Point(500,500));
    shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    

    createScoreContents(shell, display);
    createProbabilityContents(shell, display);
    
    shell.pack();
    shell.open();
    // Set up the event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        // If no more entries in the event queue
        display.sleep();
      }
    }
    display.dispose();
	
  }
  
  private void createScoreContents(Composite composite, Display display) 
  {
	  Button server1= new Button(composite, SWT.RADIO);
      server1.setBounds(new Rectangle(300, 45, 22, 20));
      server1.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
      Button server2 = new Button(composite, SWT.RADIO);
      server2.setBounds(new Rectangle(300, 65, 22, 20));
      server2.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
      
	  Label server = new Label(composite, SWT.NONE);
	  server.setBounds(new Rectangle(282, 10, 48, 30));
      server.setText("Server");
      server.setAlignment(SWT.CENTER);
      server.setBackground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
	  
	  final ToolBar toolBar = new ToolBar(composite, SWT.NONE);
	  toolBar.setBounds(new Rectangle(331, 8, 100, 30));
	  
	  final Menu menu = new Menu((Shell)composite, SWT.POP_UP);
	  MenuItem match = new MenuItem(menu, SWT.PUSH);
	  match.setText("best of 3 set tiebreaker");
	  match = new MenuItem(menu, SWT.PUSH);
	  match.setText("best of 3 set advantage");
	  match = new MenuItem(menu, SWT.PUSH);
	  match.setText("best of 5 set tiebreaker");
	  match = new MenuItem(menu, SWT.PUSH);
	  match.setText("best of 5 set advantage");
	      
	    final ToolItem dropdown = new ToolItem(toolBar, SWT.DROP_DOWN);
	    dropdown.setText("Match Type");
	    dropdown.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        if (event.detail == SWT.ARROW) {
	          Rectangle rect = dropdown.getBounds();
	          Point pt = new Point(rect.x, rect.y + rect.height);
	          pt = toolBar.toDisplay(pt);
	          menu.setLocation(pt.x, pt.y);
	          menu.setVisible(true);
	        }
	      }
	    });
      
	  final Table table = new Table(composite, SWT.NONE);
	  table.setBounds(new Rectangle(10, 10, 270, 90));
	  table.setHeaderVisible(true);
	  table.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	  table.setLinesVisible(true);
	  TableColumn[] column = new TableColumn[4];
	  column[0] = new TableColumn(table, SWT.NONE);
	  column[0].setText("Score:");
	  StyleRange ScoreStyle = new StyleRange();
	  ScoreStyle.background = display.getSystemColor(SWT.COLOR_BLUE);
	  
	  
	  column[1] = new TableColumn(table, SWT.NONE);
	  column[1].setText("Points");	  
	  column[2] = new TableColumn(table, SWT.NONE);
	  column[2].setText("Games");	  
	  column[3] = new TableColumn(table, SWT.NONE);
	  column[3].setText("Sets");
	  	  
	  // Filling the probabilities table with data
	  table.setRedraw(false);
	  
	  TableItem item = new TableItem(table, SWT.NONE);
	  int c = 0;
	  item.setText(c++, "Player 1");
	  item.setText(c++, "0");
	  item.setText(c++, "1");
	  item.setText(c++, "1");
	  
	  TableItem item2 = new TableItem(table, SWT.NONE);
	  c = 0;
	  item2.setText(c++, "Player 2");
	  item2.setText(c++, "40");
	  item2.setText(c++, "3");
	  item2.setText(c++, "1");
	  
	  table.setRedraw(true);

	  for (int i = 0, n = column.length; i < n; i++) {
		  column[i].pack();
      }
   }
  
  private void createProbabilityContents(Composite composite, Display display) 
  {
	  final Table table = new Table(composite, SWT.NONE);
	  table.setBounds(new Rectangle(10, 110, 370, 90));
	  table.setHeaderVisible(true);
	  table.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	  table.setLinesVisible(true);
	  TableColumn[] column = new TableColumn[5];	
	  
	  column[0] = new TableColumn(table, SWT.NONE); 	  
	  column[0].setText("Probability of winning:");
	 
	  
	  column[1] = new TableColumn(table, SWT.NONE);
	  column[1].setText("Point");
	  
	  column[2] = new TableColumn(table, SWT.NONE);
	  column[2].setText("Game");
	  
	  column[3] = new TableColumn(table, SWT.NONE);
	  column[3].setText("Set");
	  
	  column[4] = new TableColumn(table, SWT.NONE);
	  column[4].setText("Match");
	  	  
	  // Filling the probabilities table with data
	  table.setRedraw(false);
	  
	  TableItem item = new TableItem(table, SWT.NONE);
	  int c = 0;
	  item.setText(c++, "Player 1");
	  item.setText(c++, "62%");
	  item.setText(c++, "78%");
	  item.setText(c++, "57%");
	  item.setText(c++, "63%");
	  
	  TableItem item2 = new TableItem(table, SWT.NONE);
	  c = 0;
	  item2.setText(c++, "Player 2");
	  item2.setText(c++, "38%");
	  item2.setText(c++, "22%");
	  item2.setText(c++, "43%");
	  item2.setText(c++, "37%");
	  
	  table.setRedraw(true);

	  for (int i = 0, n = column.length; i < n; i++) {
		  column[i].pack();
      }
   }
   
   public static void main(String[] args) {
	  new Prediction_GUI().run();
	  
	  
	  ///// IO STUFF - TO BE REPLACED BY INTERFACE 
		System.out.println("What are the serve percentages of the two players?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    int serve1 = 0, serve2 = 0;
	    int serving = 0;
	    try {
	      serve1 = Integer.parseInt(br.readLine());
	      serve2 = Integer.parseInt(br.readLine());
	    } catch (IOException e) {}
	    assert(serve1 <= 100 && serve1 >= 0 &&
	    	   serve2 <= 100 && serve2 >= 0);
	    System.out.println("Who is serving?");
	    try {
	        serving = Integer.parseInt(br.readLine());
	      } catch (IOException e) {}
	    assert(serving ==1 || serving ==2);
	    //System.out.println("Your name is " + serve2);
	    boolean serving1 = false; boolean serving2 = false;
	    if (serving ==1)
	    	serving1=true; 
	    else serving2 =true;
	    
	    //// OBJECT CREATION + Calculator calls    
		Player player1 = new Player(serve1, serving1 );
		Player player2 = new Player(serve2, serving2 );
		
		Game game = new Game(player1, player2, 0, 0);
   }
}
