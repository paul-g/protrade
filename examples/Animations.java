import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
 
public class Animations {
  Display display = new Display();
  Shell shell = new Shell(display);
 
  public Animations() {
    shell.setLayout(new FillLayout());
 
    ImageLoader imageLoader = new ImageLoader();
    final ImageData[] imageDatas = imageLoader.load("java-tips.gif");
 
    final Image image = new Image(display, imageDatas[0].width, imageDatas[0].height);
    final Canvas canvas = new Canvas(shell, SWT.NULL);
 
    canvas.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        e.gc.drawImage(image, 0, 0);
      }
    });
 
    final GC gc = new GC(image);
 
    final Thread thread = new Thread() {
      int frameIndex = 0;
      public void run() {
        while (!isInterrupted()) {
          frameIndex %= imageDatas.length;
 
          final ImageData frameData = imageDatas[frameIndex];
          display.asyncExec(new Runnable() {
            public void run() {
              Image frame =
                new Image(display, frameData);
              gc.drawImage(frame, frameData.x, frameData.y);
              frame.dispose();
              canvas.redraw();
            }
          });
          try {
            // delay
            Thread.sleep(imageDatas[frameIndex].delayTime * 10);
          } catch (InterruptedException e) {
            return;
          }
          frameIndex += 1;
        }
      }
    };
    shell.addShellListener(new ShellAdapter() {
      public void shellClosed(ShellEvent e) {
        thread.interrupt();
      }
    });
 
    shell.setSize(400, 200);
    shell.open();
    thread.start();
 
    // Set up the event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        // If no more entries in event queue
        display.sleep();
      }
    }
 
    display.dispose();
  }
 
  private void init() {
 
  }
 
  public static void main(String[] args) {
    new Animations();
  }
}
