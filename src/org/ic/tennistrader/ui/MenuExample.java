package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuExample {
    private Display display;
    private Shell shell;
    private Composite c;
    
    public static void main(String[] args) {
        Display display = new Display();
        new MenuExample(display).run();
    }
    
    public MenuExample(Display display){
        this.display = display;
        this.shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setSize(200,200);
        shell.setLayout(new FillLayout());
        shell.open();
        Menu m = new Menu(shell, SWT.POP_UP);
        MenuItem test = new MenuItem(m, SWT.PUSH);
        test.setText("Test");
        shell.setMenu(m);
        shell.setText("Test");
    }

    private void run() {
        while (!shell.isDisposed()){
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
    
    public Shell getShell(){
        return this.shell;
    }
    
    public Composite getComposite(){
        return c;
    }
}
