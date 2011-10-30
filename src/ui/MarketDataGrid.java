package src.ui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import src.Pair;

public class MarketDataGrid {

	private String player1;
	private String player2;
	private Button[] back_buttons;
	private Button[] lay_buttons;

	public MarketDataGrid(Composite composite, String title) throws Exception{
		Composite comp = new Composite(composite, SWT.NONE);
		// comp.setLayout(null);
		getPlayerNames(title);
		createHeader(comp);
		createGrid(comp);

	}

	private void getPlayerNames(String title) throws Exception {
		if (!title.contains(" v ")) throw new Exception("Invalid match");
		String[] str = title.split(" v ");
		player1 = str[0].trim();
		player2 = str[1].trim();
	}

	private void createHeader(Composite comp) {
		Display display = Display.getCurrent();

		Text selections = new Text(comp, SWT.NONE);
		selections.setBounds(50, 50, 30, 10);
		selections.setText("Selections						");
		selections.setEditable(false);
		selections.setDoubleClickEnabled(false);
		selections.pack();

		Text back = new Text(comp, SWT.NONE);
		back.setBounds(250, 50, 70, 10);
		back.setText("                     				 Back");
		back.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		back.setEditable(false);
		back.setDoubleClickEnabled(false);
		back.pack();

		Text lay = new Text(comp, SWT.NONE);
		lay.setBounds(435, 50, 70, 10);
		lay.setText("Lay                     				  ");
		lay.setBackground(display.getSystemColor(SWT.COLOR_RED));
		lay.setEditable(false);
		lay.setTouchEnabled(false);
		lay.setDoubleClickEnabled(false);
		lay.pack();

	}

	private void createGrid(Composite comp) {
		Display display = Display.getCurrent();
		Text pl1 = new Text(comp, SWT.NONE);
		pl1.setBounds(50, 75, 30, 10);
		int pl1_len = player1.length();
		pl1.setText(player1);
		pl1.pack();
		if (pl1.getSize().x > 197) {
			while (pl1.getSize().x > 197) {
				player1 = player1.substring(0, pl1_len - 1);
				pl1_len = player1.length();
				pl1.setText(player1);
				pl1.pack();
			}
		} else {
			while (pl1.getSize().x < 197) {
				player1 += " ";
				pl1_len = player1.length();
				pl1.setText(player1);
				pl1.pack();
			}
		}

		pl1.setEditable(false);
		pl1.setDoubleClickEnabled(false);
		pl1.pack();
		Text pl2 = new Text(comp, SWT.NONE);
		pl2.setBounds(50, 100, 30, 10);
		int pl2_len = player2.length();
		pl2.setText(player2);
		pl2.pack();
		if (pl2.getSize().x > 197) {
			while (pl2.getSize().x > 197) {
				player2 = player2.substring(0, pl2_len - 1);
				pl2_len = player2.length();
				pl2.setText(player2);
				pl2.pack();
			}
		} else {
			while (pl2.getSize().x < 197) {
				player2 += " ";
				pl2_len = player2.length();
				pl2.setText(player2);
				pl2.pack();
			}
		}
		pl2.setEditable(false);
		pl2.setDoubleClickEnabled(false);
		pl2.pack();

		back_buttons = new Button[6];
		int w = 0;
		int h = 0;
		for (int i = 0; i < 6; i++) {
			back_buttons[i] = new Button(comp, SWT.PUSH);
			//back_buttons[i].setText("" + Math.random() + 1);
			if (i == 2 || i == 5)
				back_buttons[i].setForeground(display
						.getSystemColor(SWT.COLOR_BLUE));
			if (i == 3) {
				w = 0;
				h = 25;
			}
			back_buttons[i].setBounds(250 + w, 75 + h, 61, 25);
			w += 61;
		}

		lay_buttons = new Button[6];
		w = 0;
		h = 0;
		for (int i = 0; i < 6; i++) {
			lay_buttons[i] = new Button(comp, SWT.PUSH);
			//lay_buttons[i].setText("" + Math.random() + 1);
			if (i == 0 || i == 3)
				lay_buttons[i].setForeground(display
						.getSystemColor(SWT.COLOR_RED));
			if (i == 3) {
				w = 0;
				h = 25;
			}
			lay_buttons[i].setBounds(435 + w, 75 + h, 61, 25);
			w += 61;
		}

	}

	public void fillButtons(int pl, int backOrLay, ArrayList<Pair<Double,Double>> data) {
		if (pl == 1){
			if (backOrLay == 1){
				for (int i =0; i<data.size() && i<3; i++){
					System.out.println(data.get(i).getI());
					back_buttons[2-i].setText((data.get(i)).getI()+"");
				}
			} else {
				for (int i =0; i<data.size() && i<3; i++){
					lay_buttons[i].setText((data.get(i)).getI()+"");
				}
			}
		} else if (pl == 2) {
			if (backOrLay == 1){
				for (int i =0; i<data.size() && i<3; i++){
					System.out.println(data.get(i).getI());
					back_buttons[5-i].setText((data.get(i)).getI()+"");
				}
			} else {
				for (int i =0; i<data.size() && i<3; i++){
					lay_buttons[3+i].setText((data.get(i)).getI()+"");
				}
			}
		}
	}
}
