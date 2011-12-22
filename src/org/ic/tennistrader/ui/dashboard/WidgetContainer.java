package org.ic.tennistrader.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class WidgetContainer extends Composite {

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
	private Cursor dCursor;

	private static final int BORDER_WIDTH = 10;

	// private Button
	private CornerMenu cornerMenu;
	
	private Dashboard parent;

	public WidgetContainer(final Dashboard parent, int style, int width,
			int height) {
		super(parent, style);
		this.width = width;
		this.height = height;
		setLayout(new FillLayout());
		this.parent = parent;

		Display display = parent.getDisplay();

		cornerMenu = new CornerMenu(this, SWT.BORDER);

		addMouseTrackListener(new MenuListener(cornerMenu));

		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				System.out.println(e.x + " " + e.y);
			}
		});

		wCursor = display.getSystemCursor(SWT.CURSOR_SIZEW);
		sCursor = display.getSystemCursor(SWT.CURSOR_SIZES);
		eCursor = display.getSystemCursor(SWT.CURSOR_SIZEE);
		nCursor = display.getSystemCursor(SWT.CURSOR_SIZEN);
		dCursor = display.getSystemCursor(SWT.CURSOR_ARROW);

		nwCursor = display.getSystemCursor(SWT.CURSOR_SIZENW);
		neCursor = display.getSystemCursor(SWT.CURSOR_SIZENE);
		swCursor = display.getSystemCursor(SWT.CURSOR_SIZESW);
		seCursor = display.getSystemCursor(SWT.CURSOR_SIZESE);

		Listener l = new Listener() {
			Point origin;
			int totalX;
			int totalY;

			public void handleEvent(Event e) {
				int x = e.x;
				int y = e.y;
				switch (e.type) {
				case SWT.MouseDown:
					origin = new Point(x, y);
					break;
				case SWT.MouseUp:
					if (origin != null) {
						System.out.println("Dragged: " + (x - origin.x) + " "
								+ (y - origin.y));
						System.out.println("Total Dragged x: " + totalX
								+ " y: " + totalY);

							if (isEast(origin.x, origin.y)){
								//WidgetContainer.this.parent.handleResize(WidgetContainer.this, totalX, totalY);
						}
					}
					origin = null;
					totalX = 0;
					totalY = 0;
					break;
				case SWT.MouseMove:
					setCursor(getCursorForLocation(x, y));
					if (origin != null) {
						int dx = (x - origin.x);
						int dy = (y - origin.y);
						System.out.println("Dragged: " + dx + " " + dy );
						totalX += dx;
						totalY += dy;
						origin.x = x;
						origin.y = y;
						WidgetContainer.this.parent.handleResize(WidgetContainer.this, -5, dy);
					}
					break;
				}
			}
		};

		addListener(SWT.MouseDown, l);
		addListener(SWT.MouseUp, l);
		addListener(SWT.MouseMove, l);
	}

	// pre: component was already drawn
	public void setWidget(Control composite) {
		composite.setParent(this);
		composite.setBounds(BORDER_WIDTH, BORDER_WIDTH, width - 2
				* BORDER_WIDTH, height - 2 * BORDER_WIDTH);
	}

	class CornerMenu extends Composite {
		CornerMenu(Composite parent, int style) {
			super(parent, style);
			setLayout(new FillLayout());
			Button close = new Button(this, SWT.PUSH);
			close.setText("X");
			Button menu = new Button(this, SWT.PUSH);
			menu.setText(">");
			setSize(20, 20);
			setVisible(false);
		}
	}

	class MenuListener extends MouseTrackAdapter {

		private CornerMenu cornerMenu;

		public MenuListener(CornerMenu cornerMenu) {
			this.cornerMenu = cornerMenu;
		}

		@Override
		public void mouseExit(MouseEvent arg0) {
			setCursor(dCursor);
		}

		@Override
		public void mouseEnter(MouseEvent arg0) {
			System.out.println("Entered ");
			// cornerMenu.setVisible(true);
		}
	}

	private Cursor getCursorForLocation(int x, int y) {
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

	private Location getLocation(int x, int y) {
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

	private enum Location {
		N, S, W, E, NE, NW, SE, SW, CENTER;
	}
}