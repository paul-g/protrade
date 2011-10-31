package src.domain;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import src.Pair;
import src.service.BetfairExchangeHandler;
import src.ui.NavigationPanel;

public class UpdatableMarketDataGrid implements UpdatableWidget {

	private Button[] backButtons;
	private Button[] layButtons;
	private MOddsMarketData modds;
	private Table table;

	public UpdatableMarketDataGrid(Composite parent, TreeItem ti) {
		table = new Table(parent, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// height cannot be per row so simply set
				event.height = 67;
			}
		});
		createHeader();
		createRows();
		createButtons(); 
		for (int i = 0; i < 3; i++) {
			table.getColumn(i).pack();

		}
		//modds = BetfairExchangeHandler
        //.getMarketOdds(NavigationPanel.getMatch(ti).getEventBetfair());
		//setValues(modds);
	}
	
	public void setValues(MOddsMarketData modds) {
		fillSelCol(modds);
		fillButtons(1, 1, modds.getPl1Back());
	    fillButtons(1, 2, modds.getPl1Lay());
	    fillButtons(2, 1, modds.getPl2Back());
	    fillButtons(2, 2, modds.getPl2Lay());
	}

	private void createSelections() {
		// TODO Auto-generated method stub

	}

	private void createButtons() {
		TableItem[] tabitems = table.getItems();
		backButtons = new Button[6];
		layButtons = new Button[6];

		for (int i = 0; i < 4; i++) {
			TableEditor editor = new TableEditor(table);
			Composite c = new Composite(table, SWT.NONE);
			// c.setBackground(Display.getCurrent()
			// .getSystemColor(SWT.COLOR_WHITE));

			GridLayout gl = new GridLayout(3, true);
			c.setLayout(gl);
			GridData gridData = new GridData();
			gridData.verticalSpan = 2;
			gridData.verticalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			for (int j = 0; j < 3; j++) {
				if (i >= 2) {
					layButtons[(i - 2) * 3 + j] = new Button(c, SWT.PUSH);
					layButtons[(i - 2) * 3 + j].setLayoutData(gridData);
					if (j==0 || j==3) layButtons[(i - 2) * 3 + j].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				} else {
					backButtons[i * 3 + j] = new Button(c, SWT.PUSH);
					backButtons[i * 3 + j].setLayoutData(gridData);
					if (j==2 || j==5) layButtons[(i - 2) * 3 + j].setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				}
			}
			c.pack();
			editor.minimumWidth = c.getSize().x;
			editor.horizontalAlignment = SWT.LEFT;
			System.out.println(i % 2 + " " + (i / 2 + 1));
			editor.setEditor(c, tabitems[i % 2], i / 2 + 1);
		}

	}

	private void fillSelCol(MOddsMarketData modds) {
		TableItem[] tabitems = table.getItems();
		tabitems[0].setText(0, modds.getPlayer1());
		tabitems[1].setText(0, modds.getPlayer2());
	}

	public void fillButtons(int pl, int backOrLay, ArrayList<Pair<Double,Double>> data) {
		if (data == null) return;
		if (pl == 1){
			if (backOrLay == 1){
				for (int i =0; i<data.size() && i<3; i++){
					backButtons[2-i].setText((data.get(i)).getI()+"");
				}
			} else {
				for (int i =0; i<data.size() && i<3; i++){
					layButtons[i].setText((data.get(i)).getI()+"");
				}
			}
		} else if (pl == 2) {
			if (backOrLay == 1){
				for (int i =0; i<data.size() && i<3; i++){
					backButtons[5-i].setText((data.get(i)).getI()+"");
				}
			} else {
				for (int i =0; i<data.size() && i<3; i++){
					layButtons[3+i].setText((data.get(i)).getI()+"");
				}
			}
		}
	}

	private void createRows() {
		for (int i = 0; i < 2; i++) {
			new TableItem(table, SWT.NONE);
		}
	}

	private void createHeader() {
		TableColumn selCol = new TableColumn(table, SWT.NONE);
		selCol.setText("Selections");
		TableColumn backCol = new TableColumn(table, SWT.NONE);
		backCol.setText("Back");
		TableColumn layCol = new TableColumn(table, SWT.NONE);
		layCol.setText("Lay");
	}

	public void handleUpdate(MOddsMarketData newData) {
		if (newData.getPl1Back() != null) {
			System.out.println("Setting new data");
			setValues(newData);
		}
		for (Button b : backButtons) {
			b.getParent().update();
		}
		table.redraw();
		table.update();
		table.getParent().update();
	}
}
