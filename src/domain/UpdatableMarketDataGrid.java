package src.domain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class UpdatableMarketDataGrid implements UpdatableWidget {

    public UpdatableMarketDataGrid(Composite parent) {
        Table table = new Table(parent, SWT.BORDER);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        for (int i = 0; i < 3; i++) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText("Lay");
        }

        for (int i = 0; i < 2; i++) {
            new TableItem(table, SWT.NONE);
        }
        table.addListener(SWT.MeasureItem, new Listener() {
            public void handleEvent(Event event) {
                // height cannot be per row so simply set
                event.height = 67;
            }
        });
        TableItem[] tabitems = table.getItems();
        for (int i = 0; i < 2; i++) {
            TableEditor editor = new TableEditor(table);
            Composite c = new Composite(table, SWT.NONE);
            c.setBackground(Display.getCurrent()
                    .getSystemColor(SWT.COLOR_WHITE));

            GridLayout gl = new GridLayout(3, true);
            c.setLayout(gl);

            Button b1 = new Button(c, SWT.PUSH);
            Button b2 = new Button(c, SWT.PUSH);
            Button b3 = new Button(c, SWT.PUSH);
            b1.setText("Button One");
            b2.setText("Button Two");
            b3.setText("Button Three");
            c.pack();
            editor.minimumWidth = c.getSize().x;

            editor.horizontalAlignment = SWT.LEFT;
            editor.setEditor(c, tabitems[i], 2);
        }

        for (int i = 0; i < 3; i++) {
            table.getColumn(i).pack();
        }

    }

    public void handleUpdate(MOddsMarketData newData) {

    }
}
