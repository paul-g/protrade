package org.ic.protrade.ui.widgets.video;

import java.awt.Canvas;
import java.awt.Frame;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.ic.protrade.domain.markets.MOddsMarketData;
import org.ic.protrade.ui.widgets.MatchViewerWidget;
import org.ic.protrade.ui.widgets.WidgetType;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;

public class MatchPlayer extends MatchViewerWidget {

	private static Logger log = Logger.getLogger(MatchPlayer.class);

	public MatchPlayer(Composite parent, int style) {
		super(parent, style);
		setLayout(makeLayout());

		PlayerCoolbar pc = new PlayerCoolbar(this, SWT.NONE);
		pc.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1,
				1));

		Composite videoComposite;

		try {
			final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
			videoComposite = new Composite(this, SWT.EMBEDDED
					| SWT.NO_BACKGROUND);
			Frame videoFrame = SWT_AWT.new_Frame(videoComposite);
			Canvas videoSurface = new Canvas();
			videoSurface.setBackground(java.awt.Color.black);
			videoFrame.add(videoSurface);
			// videoComposite.setBounds(100, 100, 450, 200);
			videoComposite.setVisible(true);

			// "mediaPlayer" is a regular vlcj MediaPlayer instance
			mediaPlayerComponent.getMediaPlayer().setVideoSurface(
					new CanvasVideoSurface(videoSurface,
							new LinuxVideoSurfaceAdapter()));

			mediaPlayerComponent.getMediaPlayer().playMedia(
					"/paul/home/fed-tso.wmv");
		} catch (Exception e) {
			log.info("Failed to start media player: " + e.getMessage());
			videoComposite = new Composite(this, SWT.NONE);
			Label label = new Label(videoComposite, SWT.NONE);
			label.setText("Video loading failed: " + e.getMessage());
			label.pack();
		}

		videoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
	}

	private Layout makeLayout() {
		return new GridLayout(1, true);
	}

	@Override
	public void handleUpdate(MOddsMarketData newData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public WidgetType getWidgetType() {
		// TODO Auto-generated method stub
		return null;
	}

}
