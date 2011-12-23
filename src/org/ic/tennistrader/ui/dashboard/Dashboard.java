package org.ic.tennistrader.ui.dashboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Dashboard extends Composite{

	private final static int DEFAULT_WIDGET_COUNT = 6;
	private final static int DEFAULT_ROW_COUNT = 2;
	private final static int DEFAULT_WIDGETS_PER_ROW = DEFAULT_WIDGET_COUNT / DEFAULT_ROW_COUNT;
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
		
		for (int i=0;i<DEFAULT_ROW_COUNT; i++){
			for (int j=0;j<DEFAULT_WIDGETS_PER_ROW;j++){
				WidgetContainer wc = new WidgetContainer(this, SWT.BORDER, defaultWidgetWidth, defaultWidgetHeight);
				placeWidget(wc, j* defaultWidgetWidth , i * defaultWidgetHeight , defaultWidgetWidth, defaultWidgetHeight);
				//wc.setWidget(new Button(this, SWT.PUSH));
			}
		}
	}

	private void placeWidget(WidgetContainer simpleWidget, int x, int y, int width, int height) {
		widgetMap.put(simpleWidget, new Point(x,y));
		simpleWidget.setBounds(x, y, width, height);
	}
	
	public void handleResize(WidgetContainer wc, int dx, int dy){
		Point loc = widgetMap.get(wc);
		int width = wc.getWidth();
		int height = wc.getHeight();
		wc.setBounds(loc.x, loc.y,  width + dx, height + dy);
		wc.setHeight(height + dy);
		wc.setWidth(width + dx);
		wc.moveAbove(null);
		/*Set<WidgetContainer> s = widgetMap.keySet();
		for (WidgetContainer wcc : widgetMap.keySet()) {
			if (wcc != wc)
				wc.moveAbove(wcc);
		}*/
	}

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);

		final int shellDimension = 800;

		shell.setLayout(new FillLayout());
		shell.setSize(shellDimension, shellDimension);
		Dashboard d = new Dashboard(shell);

		// shell.setLayout(new FillLayout());

/*		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);

		final SimpleWidget[] widgets = new SimpleWidget[SIZE];

		for (int i = 0; i < SIZE; i++) {
			final SimpleWidget widget = new SimpleWidget(shell, SWT.BORDER);
			widget.setLayoutData(gd);
			widgets[i] = widget;
		}

		Menu widgetMenu = new Menu(shell, SWT.POP_UP);

		MenuItem mi = new MenuItem(widgetMenu, SWT.PUSH);
		mi.setText("Span");

		mi.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				
				 * System.out.println("Pressed "); widgets[3].dispose();
				 * widgets[2].setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				 * true, true, 2, 1)); //widgets[2].setBounds(50, 50, 200, 200);
				 * //widgets[2].setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				 * true, true, 1, 2)); shell.layout();
				 
				// /widgets[2].setBounds(0,0,300,300);
				GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true,
						true);
				layoutData.minimumHeight = 200;
				layoutData.minimumWidth = 300;

				widgets[2].setLayoutData(layoutData);
				shell.layout();
			}
		});

		widgets[2].setMenu(widgetMenu);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.minimumHeight = 200;
		layoutData.minimumWidth = 200;

		widgets[2].setLayoutData(layoutData);


		shell.setCursor(cursor);

		shell.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent arg0) {
				shell.setCursor(cursor);
			}

			@Override
			public void mouseEnter(MouseEvent arg0) {
				shell.setCursor(sizeNSCursor);
			}
		});


*/
		
		/*shell.addListener(SWT.MouseDown, l);
		shell.addListener(SWT.MouseUp, l);
		shell.addListener(SWT.MouseMove, l);
*/
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}
}