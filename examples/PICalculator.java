import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Illustrates multithread UI programming issues.
 */
public class PICalculator {
  Display display = new Display();
  Shell shell = new Shell(display);
  Button buttonThread = new Button(shell, SWT.PUSH);
  Button buttonAsyncExec = new Button(shell, SWT.PUSH);

  public PICalculator(boolean asyncExecEnabled) {
    shell.setText("PI Calculator");
    shell.setSize(400, 80);

    Rectangle clientArea = shell.getClientArea();

    buttonThread.setText(
      "Click here to calculate PI  [Non-UI thread UI Update]");
    buttonThread.setBounds(
      clientArea.x,
      clientArea.y,
      clientArea.width,
      clientArea.height / 2);
    buttonThread.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }
      public void widgetSelected(SelectionEvent e) {
        buttonThread.setText("Calculation in progress ...");
        getTask(buttonThread).start();
      }
    });
    
    buttonAsyncExec.setText("Click here to calculate PI  [asynExec method UI Update]");
    buttonAsyncExec.setBounds(
      clientArea.x,
      clientArea.y + clientArea.height / 2,
      clientArea.width,
      clientArea.height / 2);
    buttonAsyncExec.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }
      public void widgetSelected(SelectionEvent e) {
        buttonAsyncExec.setText("Calculation in progress ...");
        getTask2(buttonAsyncExec).start();
      }
    });
    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    display.dispose();
  }

  public static void main(String[] args) {
    // new CalculatePI(false);
    new PICalculator(true);
  }

  public Thread getTask(Button button) {
    final Button theButton = button;
    return new Thread() {
      public void run() {
        double pi = calculatePI(9999999);
        theButton.setText("PI = " + pi); // Update UI.
      }
    };
  }

  public Thread getTask2(Button button) {
    final Button theButton = button;
    return new Thread() {

      public void run() {
        final double pi = calculatePI(9999999);
        
        display.asyncExec(new Runnable() {
          public void run() {
            
            // Update UI.
            
            theButton.setText("PI = " + pi); 
            
          }
        });
      }
    };
  }  
  /**
   * Calculate value of PI using Vieta's formula. For a complete discussion,
   * please visit:
   * http://documents.wolfram.com/v4/GettingStarted/CalculatingPi.html
   * 
   * @param nestedLevel -
   *            level of nested square roots in Vieta's formula.
   * @return value of PI
   */
  public static double calculatePI(int nestedLevel) {
    double product = 1;
    double lastSqrtValue = 0;

    for (int i = 0; i < nestedLevel; i++) {
      double sqrt = getNextSqrtValue(lastSqrtValue);
      product *= 0.5 * sqrt;

      lastSqrtValue = sqrt;
    }

    return 2 / product;
  }

  /**
   * Return the square root item value.
   * 
   * @param lastValue -
   *            last square root item value.
   * @return
   */
  public static double getNextSqrtValue(double lastValue) {
    return Math.sqrt(2 + lastValue);
  }
}


