package examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWT_AWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Canvas;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class SWTMedia {
  Display display = new Display();
  Shell shell = new Shell(display);
  
  SashForm sashForm;
  SashForm sashForm2;
  
   final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

  public SWTMedia() {
    
    shell.setLayout(new FillLayout());
    
	Composite videoComposite = new Composite(shell, SWT.EMBEDDED | SWT.NO_BACKGROUND);
	Frame videoFrame = SWT_AWT.new_Frame(videoComposite);
	Canvas videoSurface = new Canvas();
	videoSurface.setBackground(java.awt.Color.black);
	videoFrame.add(videoSurface);
	videoComposite.setBounds(100, 100, 450, 200);
	videoComposite.setVisible(true);

// "mediaPlayer" is a regular vlcj MediaPlayer instance
	mediaPlayerComponent.setVideoSurface(videoSurface);    
    shell.setSize(450, 200);
    shell.open();
    //textUser.forceFocus();
    mediaPlayerComponent.getMediaPlayer().playMedia("file:///media/A-DATA CH91/Federer-Tsonga-Barclays-Final-Short.mpeg");

    // Set up the event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        // If no more entries in event queue
        display.sleep();
      }
    }

    display.dispose();
  }

  public static void main(String[] args) {
    new SWTMedia();
  }
}

