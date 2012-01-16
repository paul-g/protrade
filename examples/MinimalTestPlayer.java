import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;

import java.awt.Frame;
import java.awt.Canvas;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;

/*
To run: 
 javac -cp ../lib/swt-32/swt.jar:../lib/vlcj-1.2.1.jar:. MinimalTestPlayer.java
 java -Djna.library.path=/home/linux/vlc/install/lib/ -cp ../lib/swt-32/swt.jar:../lib/vlcj-1.2.1.jar:. MinimalTestPlayer "path/to/mpeg"
*/

/**
 * An absolute minimum test player.
 */
public class MinimalTestPlayer{
  
  public static void main(String[] args) throws Exception {
    if(args.length != 1) {
      System.out.println("Specify an MRL to play");
      System.exit(1);
    }

    final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
Display display = new Display();
Shell shell = new Shell(display, SWT.SHELL_TRIM); 
shell.setLayout(new FillLayout());   
	Composite videoComposite = new Composite(shell, SWT.EMBEDDED | SWT.NO_BACKGROUND);
	Frame videoFrame = SWT_AWT.new_Frame(videoComposite);
	Canvas videoSurface = new Canvas();
	videoSurface.setBackground(java.awt.Color.black);
	videoFrame.add(videoSurface);
	videoComposite.setBounds(100, 100, 450, 200);
	videoComposite.setVisible(true);

	// "mediaPlayer" is a regular vlcj MediaPlayer instance
	mediaPlayerComponent.getMediaPlayer().setVideoSurface(new CanvasVideoSurface(videoSurface, new LinuxVideoSurfaceAdapter()));

    mediaPlayerComponent.getMediaPlayer().playMedia(args[0]);
    
    shell.setSize(450, 200);
    shell.open();

		  while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        // If no more entries in event queue
        display.sleep();
      }
    }

    display.dispose();

  }
}
