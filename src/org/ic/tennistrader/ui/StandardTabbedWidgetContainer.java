package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class StandardTabbedWidgetContainer {
    protected final CTabFolder folder;
    protected Display display;
    protected CTabItem selected;

    public StandardTabbedWidgetContainer(Composite parent, int style) {
        folder = new CTabFolder(parent, SWT.RESIZE | SWT.BORDER);
        folder.setSimple(false);
        folder.setMinimizeVisible(true);
        folder.setMaximizeVisible(true);
        folder.layout();
        folder.setLayoutData(makeLayoutData());
        setOnClickMenu();
    }

    private GridData makeLayoutData() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        return gridData;
    }

    protected void setOnClickMenu() {
        Menu popup = new Menu(folder);
        MenuItem openItem = new MenuItem(popup, SWT.NONE);
        openItem.setText("Open in a new window");
        openItem.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selected != null) {
                    // create the new shell
                    Shell shell = new Shell(display, SWT.SHELL_TRIM);
                    shell.setLayout(new FillLayout());

                    // get selected tab
                    CTabItem ni = selected;

                    shell.setText(ni.getText());
                    Composite comp = (Composite) ni.getControl();
                    SashForm sashForm = (SashForm) comp.getChildren()[0];
                    sashForm.setFocus();
                    sashForm.setParent(shell);
                    sashForm.pack();

                    // ni.setControl(new Composite(folder, SWT.NONE));
                    ni.dispose();
                    shell.pack();
                    shell.open();
                }
            }
        });

        // close option
        MenuItem closeItem = new MenuItem(popup, SWT.NONE);
        closeItem.setText("Close");
        closeItem.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                if (selected != null)
                    selected.dispose();
            }
        });

        // close all option
        MenuItem closeAll = new MenuItem(popup, SWT.NONE);
        closeAll.setText("Close All");
        closeAll.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                CTabItem[] items = folder.getItems();
                for (int i = 0; i < items.length; i++)
                    items[i].dispose();
            }
        });

        folder.addListener(SWT.MenuDetect,
                new StandardTabbedWidgetRightClickListener(popup, folder));

        folder.addMouseListener(new MouseListener() {
            public void mouseDoubleClick(MouseEvent e) {
                MainWindow.toggleMaximizeMatchDisplay();
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
    }
    
    public CTabItem addTab(String title){
        final CTabItem item = new CTabItem(folder, SWT.CLOSE);
        
        item.setText(title);
        
        folder.setSelection(item);
        
        item.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent arg0) {
                // dispose children on close
                item.getControl().dispose();
            }
        });
        
        return item;
    }
    
    public void addWidgetAsTab(StandardWidgetContainer swc){
        final CTabItem item = new CTabItem(folder, SWT.CLOSE);
        
        item.setText(swc.getTitle());
        
        folder.setSelection(item);
        
        item.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent arg0) {
                // dispose children on close
                item.getControl().dispose();
            }
        });
        
        item.setControl(swc);
    }

	public int getTabPosition(String matchName) {
		int pos = -1;
		CTabItem[] items = folder.getItems();
	    for (int i = 0; pos == -1 && i < items.length; i++)
	        if (items[i].getText().equals(matchName)) {
	            pos = i;
	        }
		return pos;
	}
}
