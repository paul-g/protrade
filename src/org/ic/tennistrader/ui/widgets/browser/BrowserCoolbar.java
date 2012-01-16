package org.ic.tennistrader.ui.widgets.browser;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.ic.tennistrader.ui.toolbars.BrowserToolBar;
import org.ic.tennistrader.ui.toolbars.DashboardCoolBar;

public class BrowserCoolbar extends Composite {

	private final CoolBar coolBar;

	private CoolItem browserItem;

	private CoolItem searchItem;

	private static Logger log = Logger.getLogger(DashboardCoolBar.class);
	private final BrowserWidget parent;

	public BrowserCoolbar(BrowserWidget parent, int style) {
		super(parent, SWT.NONE);
		this.parent = parent;
		setLayout(new FillLayout());
		coolBar = new CoolBar(this, SWT.NONE);

		makeDashboardItem();
		makeSearchItem();

		coolBar.pack();

		int searchWidth = Display.getCurrent().getClientArea().width
				- browserItem.getBounds().width;

		log.info("shell width: " + parent.getClientArea().width
				+ "search box width: " + searchWidth + " profile bar width: "
				+ browserItem.getBounds().width);

		searchItem.setPreferredSize(searchWidth, searchItem.getSize().y);
		searchItem.setSize(searchWidth, searchItem.getSize().y);
	}

	private void makeDashboardItem() {
		browserItem = new CoolItem(coolBar, SWT.NONE);
		BrowserToolBar btb = new BrowserToolBar(this);
		ToolBar tb = btb.getToolbar();
		tb.pack();
		Point p = tb.getSize();
		tb.setSize(p);
		Point p2 = browserItem.computeSize(p.x, p.y);
		browserItem.setControl(tb);
		browserItem.setPreferredSize(p2);
	}

	private void makeSearchItem() {
		searchItem = new CoolItem(coolBar, SWT.NONE);
		Composite c = new Composite(coolBar, SWT.NONE);
		c.setLayout(new GridLayout(3, false));
		Button b = new Button(c, SWT.PUSH);
		b.setImage(new Image(Display.getCurrent(), "images/toolbar/search.png"));
		b.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false,
				true, 1, 1));
		final Text t = new Text(c, SWT.NONE);
		t.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		t.setText("Search...");
		t.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true,
				1, 1));
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				super.mouseDoubleClick(arg0);
				t.setText("");
			}
		});

		Button x = new Button(c, SWT.PUSH);
		x.setImage(new Image(Display.getCurrent(), "images/toolbar/cross.png"));
		x.setLayoutData(new GridData(GridData.END, GridData.FILL, false, true,
				1, 1));

		searchItem.setControl(c);
		searchItem.setPreferredSize(t.computeSize(SWT.DEFAULT, 36));

		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				parent.setUrl(t.getText());
			}
		});

	}

	public BrowserWidget getBrowser() {
		return parent;
	}

	public CoolBar getCoolBar() {
		return coolBar;
	}
}
