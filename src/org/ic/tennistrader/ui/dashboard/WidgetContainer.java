package org.ic.tennistrader.ui.dashboard;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class WidgetContainer extends Composite {

	int width;
	int height;

	private final Cursor wCursor;
	private final Cursor eCursor;
	private final Cursor nCursor;
	private final Cursor sCursor;
	private final Cursor nwCursor;
	private final Cursor neCursor;
	private final Cursor swCursor;
	private final Cursor seCursor;
	Cursor dCursor;

	static final Logger log = Logger.getLogger(WidgetContainer.class);

	private static final int BORDER_WIDTH = 10;

	// private final CornerMenu cornerMenu;

	final Dashboard dashboard;

	private Control child;
	private Menu menu;

	private boolean dettached = false;

	// private final ScrolledComposite sc;

	public WidgetContainer(final Dashboard parent, int style, int width,
			int height) {
		super(parent, style);
		this.width = width;
		this.height = height;
		setLayout(new FillLayout());
		this.dashboard = parent;

		Display display = parent.getDisplay();

		// cornerMenu = new CornerMenu(this, SWT.BORDER);

		// addMouseTrackListener(new MenuListener(this, cornerMenu));

		wCursor = display.getSystemCursor(SWT.CURSOR_SIZEW);
		sCursor = display.getSystemCursor(SWT.CURSOR_SIZES);
		eCursor = display.getSystemCursor(SWT.CURSOR_SIZEE);
		nCursor = display.getSystemCursor(SWT.CURSOR_SIZEN);
		dCursor = display.getSystemCursor(SWT.CURSOR_ARROW);

		nwCursor = display.getSystemCursor(SWT.CURSOR_SIZENW);
		neCursor = display.getSystemCursor(SWT.CURSOR_SIZENE);
		swCursor = display.getSystemCursor(SWT.CURSOR_SIZESW);
		seCursor = display.getSystemCursor(SWT.CURSOR_SIZESE);

		Listener resizeListener = new WidgetContainerResizeListener(this);

		addListener(SWT.MouseDown, resizeListener);
		addListener(SWT.MouseUp, resizeListener);
		addListener(SWT.MouseMove, resizeListener);

		Listener moveListener = new WidgetContainerMoveListener(this);

		addListener(SWT.MouseUp, moveListener);
		addListener(SWT.MouseDown, moveListener);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent me) {
				dashboard.toggleMaximizedControl(WidgetContainer.this);
			}
		});

		/*
		 * sc = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL |
		 * SWT.V_SCROLL); sc.setLayout(new FillLayout());
		 * sc.setExpandHorizontal(true); sc.setExpandVertical(true);
		 */

		makeAndSetMenu();
	}

	private void makeAndSetMenu() {
		setMenu(makeAttacheddMenu(this));
	}

	// pre: component was already drawn
	public void setWidget(Control control) {
		// sc.setContent(control);
		// sc.setMinSize(control.getBounds().x, control.getBounds().y);
		control.setParent(this);
		child = control;
		fitChild();
	}

	public Control getWidget() {
		return child;
	}

	private void fitChild() {
		if (child != null)
			child.setBounds(BORDER_WIDTH, BORDER_WIDTH, width - 2
					* BORDER_WIDTH, height - 2 * BORDER_WIDTH);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	Cursor getCursorForLocation(int x, int y) {
		switch (getLocation(x, y)) {
		case N:
			return nCursor;
		case S:
			return sCursor;
		case W:
			return wCursor;
		case E:
			return eCursor;
		case NE:
			return neCursor;
		case NW:
			return nwCursor;
		case SE:
			return seCursor;
		case SW:
			return swCursor;
		default:
			return dCursor;
		}
	}

	private boolean isWest(int x, int y) {
		return x < BORDER_WIDTH;
	}

	private boolean isEast(int x, int y) {
		return x > width - BORDER_WIDTH;
	}

	private boolean isNorth(int x, int y) {
		return y < BORDER_WIDTH;
	}

	private boolean isNorthE(int x, int y) {
		return isNorth(x, y) && isEast(x, y);
	}

	private boolean isNorthW(int x, int y) {
		return isNorth(x, y) && isWest(x, y);
	}

	private boolean isSouth(int x, int y) {
		return y > height - BORDER_WIDTH;
	}

	private boolean isSouthE(int x, int y) {
		return isSouth(x, y) && isEast(x, y);
	}

	private boolean isSouthW(int x, int y) {
		return isSouth(x, y) && isWest(x, y);
	}

	Location getLocation(int x, int y) {
		if (isNorthW(x, y))
			return Location.NW;
		else if (isNorthE(x, y))
			return Location.NE;
		else if (isNorth(x, y))
			return Location.N;
		else if (isSouthW(x, y))
			return Location.SW;
		else if (isSouthE(x, y))
			return Location.SE;
		else if (isSouth(x, y))
			return Location.S;
		if (isWest(x, y))
			return Location.W;
		else if (isEast(x, y))
			return Location.E;
		return Location.CENTER;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.width = width;
		this.height = height;
		fitChild();
	}

	@Override
	public void setBounds(Rectangle rectangle) {
		super.setBounds(rectangle);
		this.width = rectangle.width;
		this.height = rectangle.height;
		fitChild();
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public boolean isDettached() {
		return dettached;
	}

	public void setDettached(boolean dettached) {
		this.dettached = dettached;
		if (dettached)
			setMenu(makeDettachedMenu(this));
		else
			setMenu(makeAttacheddMenu(this));
	}

	public static Menu makeAttacheddMenu(WidgetContainer widgetContainer) {
		Menu menu = new Menu(widgetContainer.getShell(), SWT.POP_UP);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Detach");
		item.addSelectionListener(new DettachListener(widgetContainer));
		return menu;
	}

	public static Menu makeDettachedMenu(WidgetContainer widgetContainer) {
		Shell shell = widgetContainer.getShell();
		Menu menu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Reatach");
		item.addSelectionListener(new AttachListener(widgetContainer));
		return menu;
	}

	public boolean resizeEnabled() {
		return !dashboard.isLocked();
	}

	public boolean moveEnabled() {
		return !dashboard.isLocked();
	}

}