package org.ic.tennistrader.ui.dashboard;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.Match;

public class Dashboard extends Composite {

	private final static int DEFAULT_WIDGET_COUNT = 6;
	private final static int DEFAULT_ROW_COUNT = 2;
	private final static int DEFAULT_WIDGETS_PER_ROW = DEFAULT_WIDGET_COUNT
			/ DEFAULT_ROW_COUNT;
	final static int SIZE = 6;

	private static final Logger log = Logger.getLogger(Dashboard.class);

	private DashboardConfiguration dashboardConfiguration;

	private Rectangle clientArea;

	public Dashboard(Composite parent) {
		super(parent, SWT.NONE);
		init(parent);
		addDefaultWidgets();
	}

	public Dashboard(Composite parent, String filename) {
		super(parent, SWT.NONE);
		init(parent);
		loadDashboardConfiguration(filename);
	}

	private void init(Composite parent) {
		Rectangle r = parent.getClientArea();
		clientArea = getClientArea();
		dashboardConfiguration = new DashboardConfiguration(this);
		dashboardConfiguration.setDefaultWidgetHeight(r.height
				/ DEFAULT_ROW_COUNT);
		dashboardConfiguration.setDefaultWidgetWidth(r.width
				/ DEFAULT_WIDGETS_PER_ROW);

		log.info("Initializing dashboard with default widget count "
				+ DEFAULT_WIDGET_COUNT + " default height: "
				+ dashboardConfiguration.getDefaultWidgetHeight()
				+ " default width: "
				+ dashboardConfiguration.getDefaultWidgetWidth());

		addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				Rectangle area = getClientArea();

				log.info("Previous client area " + clientArea + " "
						+ " new client area" + getClientArea());

				if (clientArea.width != 0 && clientArea.height != 0
						&& area.width != 0 && area.height != 0) {
					double heightRatio = (double) clientArea.height
							/ getClientArea().height;
					double widthRatio = (double) clientArea.width
							/ getClientArea().width;
					scaleWidgets(widthRatio, heightRatio);
					layoutWidgets();
				}

				clientArea = area;
			}
		});

	}

	private void addDefaultWidgets() {
		for (int i = 0; i < DEFAULT_ROW_COUNT; i++) {
			for (int j = 0; j < DEFAULT_WIDGETS_PER_ROW; j++) {
				WidgetContainer wc = new WidgetContainer(this, SWT.BORDER,
						dashboardConfiguration.getDefaultWidgetWidth(),
						dashboardConfiguration.getDefaultWidgetHeight());
				placeWidget(wc,
						j * dashboardConfiguration.getDefaultWidgetWidth(),
						i * dashboardConfiguration.getDefaultWidgetHeight(),
						dashboardConfiguration.getDefaultWidgetWidth(),
						dashboardConfiguration.getDefaultWidgetHeight());
				wc.setWidget(new WidgetPlaceholder(this, SWT.NONE, wc));
			}
		}
	}

	private void placeWidget(WidgetContainer simpleWidget, int x, int y,
			int width, int height) {
		updateWidgetPosition(simpleWidget, x, y);
		simpleWidget.setBounds(x, y, width, height);
	}

	public void move(WidgetContainer wc, int dx, int dy) {
		Point loc = getLocation(wc);
		int newX = loc.x + dx;
		int newY = loc.y + dy;
		wc.setLocation(newX, newY);
		updateWidgetPosition(wc, newX, newY);
		wc.moveAbove(null);
	}

	public void handleResize(WidgetContainer wc, int dx, int dy,
			Location dragLocation) {
		Point loc = getLocation(wc);
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
		updateWidgetPosition(wc, newX, newY);
		wc.setHeight(newHeight);
		wc.setWidth(newWidth);
		wc.moveAbove(null);
	}

	private void updateWidgetPosition(WidgetContainer wc, int newX, int newY) {
		dashboardConfiguration.updateLocation(wc, new Point(newX, newY));
	}

	private Point getLocation(WidgetContainer wc) {
		Point loc = dashboardConfiguration.getLocation(wc);
		return loc;
	}

	public void setMaximizedControl(WidgetContainer wc) {
		Rectangle rect = getClientArea();
		// updateWidgetPosition(wc, rect.x, rect.y);
		wc.setBounds(rect);
		wc.moveAbove(null);
	}

	public void save() {
		log.info("Saving Dashboard");
		dashboardConfiguration.save("templates/dashboard.dat");
	}

	private void loadDashboardConfiguration(String filename) {
		try {
			dashboardConfiguration.load(filename);
			layoutWidgets();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void layoutWidgets() {
		log.info("Updating layout");
		Set<WidgetContainer> widgets = dashboardConfiguration.getWidgetMap()
				.keySet();
		for (WidgetContainer wc : widgets) {
			Point p = dashboardConfiguration.getWidgetMap().get(wc);
			Rectangle r = new Rectangle(p.x, p.y, wc.getWidth(), wc.getHeight());
			wc.setBounds(r);
		}
	}

	private void scaleWidgets(double xRatio, double yRatio) {
		log.info("Scaling widgets -> xRatio :" + xRatio + " yRatio " + yRatio);
		Map<WidgetContainer, Point> map = dashboardConfiguration.getWidgetMap();
		Set<WidgetContainer> widgets = map.keySet();
		for (WidgetContainer wc : widgets) {
			Point p = map.get(wc);
			int newX = (int) (p.x / xRatio);
			int newY = (int) (p.y / yRatio);
			Point newP = new Point(newX, newY);
			map.put(wc, newP);
			int newWidth = (int) (wc.getWidth() / xRatio);
			wc.setWidth(newWidth);
			int newHeight = (int) (wc.getHeight() / yRatio);
			wc.setHeight(newHeight);
		}
	}

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);

		final int shellDimension = 800;

		shell.setLayout(new FillLayout());
		shell.setSize(shellDimension, shellDimension);

		new Dashboard(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public void setMatch(Match match) {
		dashboardConfiguration.setMatch(match);
	}
}