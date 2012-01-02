package org.ic.tennistrader.ui.dashboard;

import java.io.FileNotFoundException;
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

public class Dashboard extends Composite {

	private final static int DEFAULT_WIDGET_COUNT = 6;
	private final static int DEFAULT_ROW_COUNT = 2;
	private final static int DEFAULT_WIDGETS_PER_ROW = DEFAULT_WIDGET_COUNT
			/ DEFAULT_ROW_COUNT;
	final static int SIZE = 6;

	private static final Logger log = Logger.getLogger(Dashboard.class);
 
	private DashboardConfiguration data;

	public Dashboard(Shell shell) {
		super(shell, SWT.NONE);
		init(shell);
		addDefaultWidgets();
	}
	
	public Dashboard(Shell shell, String filename) {
		super(shell, SWT.NONE);
		init(shell);
		loadDashboardConfiguration(filename);
	}

	private void init(Shell shell) {
		Rectangle r = shell.getClientArea();
		data = new DashboardConfiguration(this);
		data.setDefaultWidgetHeight(r.height / DEFAULT_ROW_COUNT);
		data.setDefaultWidgetWidth(r.width / DEFAULT_WIDGETS_PER_ROW);

		log.info("Initializing dashboard with default widget count "
				+ DEFAULT_WIDGET_COUNT + " default height: "
				+ data.getDefaultWidgetHeight() + " default width: " + data.getDefaultWidgetWidth());

		addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Resize");
			}
		});

	}

	private void addDefaultWidgets() {
		for (int i = 0; i < DEFAULT_ROW_COUNT; i++) {
			for (int j = 0; j < DEFAULT_WIDGETS_PER_ROW; j++) {
				WidgetContainer wc = new WidgetContainer(this, SWT.BORDER,
						data.getDefaultWidgetWidth(), data.getDefaultWidgetHeight());
				placeWidget(wc, j * data.getDefaultWidgetWidth(),
						i * data.getDefaultWidgetHeight(), data.getDefaultWidgetWidth(),
						data.getDefaultWidgetHeight());
				wc.setWidget(new WidgetPlaceholder(this, SWT.NONE, wc));
			}
		}
	}

	
	private void placeWidget(WidgetContainer simpleWidget, int x, int y,
			int width, int height) {
		updateWidgetPosition(simpleWidget, x, y);
		simpleWidget.setBounds(x, y, width, height);
	}
	
	public void move(WidgetContainer wc, int dx, int dy){
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
		data.updateLocation(wc, new Point(newX, newY));
	}

	private Point getLocation(WidgetContainer wc) {
		Point loc = data.getLocation(wc);
		return loc;
	}

	public void setMaximizedControl(WidgetContainer wc) {
		Rectangle rect = getClientArea();
//		updateWidgetPosition(wc, rect.x, rect.y);
		wc.setBounds(rect);
		wc.moveAbove(null);
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
	
	public void save(){
		log.info("Saving Dashboard");
		data.save("dashboard.dat");
	}
	
	private void loadDashboardConfiguration(String filename){
		try {
			data.load(filename);
			layoutWidgets();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void layoutWidgets(){
		log.info("Updating layout");
		Set<WidgetContainer> widgets = data.getWidgetMap().keySet();
		for (WidgetContainer wc : widgets){
			Point p = data.getWidgetMap().get(wc);
			Rectangle r = new Rectangle(p.x, p.y, wc.getWidth(), wc.getHeight());
			wc.setBounds(r);
		}
	}
}