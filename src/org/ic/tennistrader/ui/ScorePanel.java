package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.Score;
import org.ic.tennistrader.service.LiveDataFetcher;

public class ScorePanel {

	private Score score;

	private static final Display display = new Display();
	private static final Shell shell = new Shell(display, SWT.SHELL_TRIM);

	public static void main(String[] args) {

		
		/*
		 * group.setBackground(display.getSystemColor(14)); group.setLayout(new
		 * FillLayout()); shell.setLayout(new FillLayout()); Button button = new
		 * Button(group, SWT.PUSH ); Button button2 = new Button(group,
		 * SWT.PUSH); Button button3 = new Button(shell, SWT.PUSH);
		 * button.setText("Push me"); button2.setText("Push me 2");
		 */
		Rectangle r = shell.getClientArea();
		Image image = new Image(display, r.height, r.width);

		Group group = new Group(shell, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new FillLayout());
		shell.setLayout(new FillLayout());
		final Canvas canvas = new Canvas(group, SWT.NO_REDRAW_RESIZE);
		
		
		final Color color = new org.eclipse.swt.graphics.Color(
                display, 0, 128, 0);
		final Color fontColor = new org.eclipse.swt.graphics.Color(
                display, 253, 238, 0);
		canvas.setBackground(color);
		
		color.dispose();
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = canvas.getClientArea();
				GC gc = e.gc;
				int x = 5, y =5;
				makePlayerLabel(x, y, "Roger Federer", gc);
				for (int i=0;i<135;i+=30)
					makeScoreSquare(fontColor, gc, x+i + 202, y);
				
				makePlayerLabel(x, y + 30, "Rafael Nadal", gc);
				for (int i=0;i<135;i+=30)
					makeScoreSquare(fontColor, gc, x+ i + 202, y + 30);
			}

			private void makePlayerLabel(int x, int y, String string, GC gc) {
				int height = 28;
				int width = 200;
				int arc = 10;
				gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.fillRoundRectangle(x, y, width, height, arc, arc);
				gc.setForeground(fontColor);
				gc.setFont(new Font(display,"Times",height-15,SWT.BOLD));
				gc.drawText(string, x + 20, y, SWT.DRAW_TRANSPARENT);
			}

			private void makeScoreSquare(final Color fontColor, GC gc, int x,
					int y) {
				int size = 28;
				int arc = 10;
				int fontSize = size - 13;
				gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.fillRoundRectangle(x, y, size, size, arc, arc);
				gc.setForeground(fontColor);
				gc.setFont(new Font(display,"Times",fontSize,SWT.BOLD));
				gc.drawText("6", x + size/4,y, SWT.DRAW_TRANSPARENT);
			}
		});

		/*GC gc = new GC(image);
		// Rectangle bounds = image.getBounds();
		// gc.drawLine(0,0,bounds.width,bounds.height);
		// gc.drawLine(0,bounds.height,bounds.width,0);
		gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		gc.fillRoundRectangle(5, 5, 20, 20, 10, 10);
		gc.drawText("0", 5, 5, SWT.DRAW_TRANSPARENT);
		shell.setBackgroundImage(image);
		gc.dispose();
*/
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		LiveDataFetcher.stopAllThreads();
		LowerToolBar.setStop();

		display.dispose();
	}

}
