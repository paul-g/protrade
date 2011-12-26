package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

public class WidgetContainer extends Composite {

	private int width;
	private int height;

	private Cursor wCursor;
	private Cursor eCursor;
	private Cursor nCursor;
	private Cursor sCursor;
	private Cursor nwCursor;
	private Cursor neCursor;
	private Cursor swCursor;
	private Cursor seCursor;
	Cursor dCursor;

	private static final int BORDER_WIDTH = 10;

	private CornerMenu cornerMenu;

	private Dashboard dashboard;

	private Control child;

	public WidgetContainer(final Dashboard parent, int style, int width,
			int height) {
		super(parent, style);
		this.width = width;
		this.height = height;
		setLayout(new FillLayout());
		this.dashboard = parent;

		Display display = parent.getDisplay();

		cornerMenu = new CornerMenu(this, SWT.BORDER);

		addMouseTrackListener(new MenuListener(this, cornerMenu));

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
	}

	// pre: component was already drawn
	public void setWidget(Control composite) {
		composite.setParent(this);
		child = composite;
		fitChild();
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
		fitChild();
	}
	
	public Dashboard getDashboard(){
		return dashboard;
	}
}