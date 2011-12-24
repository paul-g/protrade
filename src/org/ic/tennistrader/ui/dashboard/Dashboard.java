package org.ic.tennistrader.ui.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Dashboard extends Composite {

	private final static int DEFAULT_WIDGET_COUNT = 6;
	private final static int DEFAULT_ROW_COUNT = 2;
	private final static int DEFAULT_WIDGETS_PER_ROW = DEFAULT_WIDGET_COUNT
			/ DEFAULT_ROW_COUNT;
	private int defaultWidgetWidth;
	private int defaultWidgetHeight;

	final static int SIZE = 6;

	private static final Logger log = Logger.getLogger(Dashboard.class);

	private Map<WidgetContainer, Point> widgetMap = new HashMap<WidgetContainer, Point>();

	public Dashboard(Shell shell) {
		super(shell, SWT.NONE);
		Rectangle r = shell.getClientArea();
		defaultWidgetHeight = r.height / DEFAULT_ROW_COUNT;
		defaultWidgetWidth = r.width / DEFAULT_WIDGETS_PER_ROW;

		log.info("Initializing dashboard with default widget count "
				+ DEFAULT_WIDGET_COUNT + " default height: "
				+ defaultWidgetHeight + " default width: " + defaultWidgetWidth);

		for (int i = 0; i < DEFAULT_ROW_COUNT; i++) {
			for (int j = 0; j < DEFAULT_WIDGETS_PER_ROW; j++) {
				WidgetContainer wc = new WidgetContainer(this, SWT.BORDER,
						defaultWidgetWidth, defaultWidgetHeight);
				placeWidget(wc, j * defaultWidgetWidth,
						i * defaultWidgetHeight, defaultWidgetWidth,
						defaultWidgetHeight);
				wc.setWidget(new Button(this, SWT.PUSH));
			}
		}
	}

	private void placeWidget(WidgetContainer simpleWidget, int x, int y,
			int width, int height) {
		widgetMap.put(simpleWidget, new Point(x, y));
		simpleWidget.setBounds(x, y, width, height);
	}

	public void handleResize(WidgetContainer wc, int dx, int dy,
			Location dragLocation) {
		Point loc = widgetMap.get(wc);
		int oldWidth = wc.getWidth();
		int oldHeight = wc.getHeight();

		int oldX = loc.x;
		int oldY = loc.y;

		int newWidth = oldWidth;
		// int newWidth = oldWidth + dx;
		// int newHeight = oldHeight + dy;
		int newHeight = oldHeight;

		int newX = oldX;
		int newY = oldY;

		System.out.println(dragLocation);
		System.out.println(dx + " " + dy);

		switch (dragLocation) {
		case SE:
			newHeight = oldHeight + dy;
			newWidth = oldWidth + dx;
			break;
		case SW:
			newHeight = oldHeight + dy;
			newX = oldX + dx;
			newWidth = oldWidth - dx;
			break;
		case NE:
			newY = oldY + dy;
			newHeight = oldHeight - dy;
			newWidth = oldWidth + dx;
			break;
		case NW:
			newX = oldX + dx;
			newWidth = oldWidth - dx;
			newY = oldY + dy;
			newHeight = oldHeight - dy;
			break;
		case W:
			newX = oldX + dx;
			newWidth = oldWidth - dx;
			break;
		case E:
			newWidth = oldWidth + dx;
			break;
		case N:
			newY = oldY + dy;
			newHeight = oldHeight - dy;
			break;
		case S:
			newHeight = oldHeight + dy;
			break;
		default:
		}

		wc.setBounds(newX, newY, newWidth, newHeight);
		widgetMap.put(wc, new Point(newX, newY));
		wc.setHeight(newHeight);
		wc.setWidth(newWidth);
		wc.moveAbove(null);
	}

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);

		final int shellDimension = 800;

		shell.setLayout(new FillLayout());
		shell.setSize(shellDimension, shellDimension);
		Dashboard d = new Dashboard(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}
}