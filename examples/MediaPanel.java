    	// Fig. 21.6: MediaPanel.java
    	// A JPanel the plays media from a URL
    	import java.awt.BorderLayout;
    	import java.awt.Component;
    	import java.io.IOException;
    	import java.net.URL;
  	import javax.media.CannotRealizeException;
    	import javax.media.Manager;
    	import javax.media.NoPlayerException;
   	import javax.media.Player;
   	import javax.swing.JPanel;
   	import javax.swing.JFrame;
  	import java.net.MalformedURLException;
  	import javax.swing.JFileChooser;
  	import javax.media.PlugInManager;
  	import javax.media.Format;
  	import javax.media.format.VideoFormat;
  
   	public class MediaPanel extends JPanel
   	{
   		public static void main(String[] args){
							 JFrame mediaTest = new JFrame( "Media Tester" );
             	 mediaTest.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
             	 
             	  	 JFileChooser fileChooser = new JFileChooser();
   
         	 // show open file dialog
         	 int result = fileChooser.showOpenDialog( null );
   
   Format[] inFormats = { new VideoFormat ("MPG") };

PlugInManager.addPlugIn ("net.sourceforge.jffmpeg.VideoDecoder", inFormats, null, PlugInManager.CODEC);

try {
	PlugInManager.commit (); }
	catch (Exception e) {e.printStackTrace();}
             	 
             	 try{
             	 //URL mediaURL = new URL("file://media/6C36C2F636C2BFF6/Users/Paul/Pictures/28.11.2011/Federer-Tsonga-Barclays-Final-Short.mpg");
             	URL mediaURL = fileChooser.getSelectedFile().toURL();
             	
//							URL mediaURL = new URL("http://www.youtube.com/watch?v=kEGYBcWRDAE&feature=feedrec_grec_index");
             	 
             	              	 MediaPanel mediaPanel = new MediaPanel( mediaURL );
							
             	 mediaTest.add( mediaPanel );
   
              	 mediaTest.setSize( 300, 300 );
              	 mediaTest.setVisible( true );

             	 } catch (MalformedURLException e){
             	 	e.printStackTrace();
             	 }
             	 
}
      	 public MediaPanel( URL mediaURL )
      	 {
         	 setLayout( new BorderLayout() ); // use a BorderLayout
   
         	 // Use lightweight components for Swing compatibility
 	 Manager.setHint( Manager.LIGHTWEIGHT_RENDERER, true );
        
         	 try
         	 {
           	 // create a player to play the media specified in the URL
            	 Player mediaPlayer = Manager.createRealizedPlayer( mediaURL );
   
           	 // get the components for the video and the playback controls
            	 Component video = mediaPlayer.getVisualComponent();
           	 Component controls = mediaPlayer.getControlPanelComponent();
           
            	 if ( video != null )
          	 add( video, BorderLayout.CENTER ); // add video component
 
            	 if ( controls != null )
               	 add( controls, BorderLayout.SOUTH ); // add controls
   
            	 mediaPlayer.start(); // start playing the media clip
         	 } // end try
         	 catch ( NoPlayerException noPlayerException )
         	 {
            	 System.err.println( "No media player found" );
        	 } // end catch
         	 catch ( CannotRealizeException cannotRealizeException )
   	 {
            	 System.err.println( "Could not realize media player" );
         	 } // end catch
         	 catch ( IOException iOException )
	   {
            	 System.err.println( "Error reading from the source" );
         	 } // end catch
	      } // end MediaPanel constructor
	      
}
