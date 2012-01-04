package org.ic.tennistrader.ui.chart;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ChartSettings {
	
	OddsChart chart;
	TreeMap<String,Color> colorList = new TreeMap<String,Color>();
	Display display;
	Shell shell;
	
	public static void main(String[] args){
		Display d = new Display();
		ChartSettings c = new ChartSettings(d,null, "nadal", "pavel");
		while (!c.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();
	}
	
	public ChartSettings(Display display, OddsChart largeChart, String namePl1, String namePl2) {
		shell = new Shell(display, SWT.SHELL_TRIM);
		this.display = Display.getCurrent();
		this.chart = largeChart;
		initColorList();
		initWindow(namePl1, namePl2);
		
		shell.open();
		
	}

	private void initColorList() {
		colorList.put("Black", display.getSystemColor(SWT.COLOR_BLACK));
		colorList.put("Blue", display.getSystemColor(SWT.COLOR_BLUE));
		colorList.put("Cyan", display.getSystemColor(SWT.COLOR_CYAN));
		colorList.put("Dark Blue", display.getSystemColor(SWT.COLOR_DARK_BLUE));
		colorList.put("Dark Cyan", display.getSystemColor(SWT.COLOR_DARK_CYAN));
		colorList.put("Dark Gray", display.getSystemColor(SWT.COLOR_DARK_GRAY));
		colorList.put("Dark Green", display.getSystemColor(SWT.COLOR_DARK_GREEN));
		colorList.put("Dark Magenta", display.getSystemColor(SWT.COLOR_DARK_MAGENTA));
		colorList.put("Dark Red", display.getSystemColor(SWT.COLOR_DARK_RED));
		colorList.put("Dark Yellow", display.getSystemColor(SWT.COLOR_DARK_YELLOW));
		colorList.put("Gray", display.getSystemColor(SWT.COLOR_GRAY));
		colorList.put("Green", display.getSystemColor(SWT.COLOR_GREEN));
		colorList.put("Magenta", display.getSystemColor(SWT.COLOR_MAGENTA));
		colorList.put("Red", display.getSystemColor(SWT.COLOR_RED));
		colorList.put("White", display.getSystemColor(SWT.COLOR_WHITE));
		colorList.put("Yellow", display.getSystemColor(SWT.COLOR_YELLOW));
	}

	private void initWindow(String namePl1, String namePl2) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 6; 
		//layout.makeColumnsEqualWidth = true;
		shell.setLayout(layout);
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.horizontalSpan = 1;
		gridData1.grabExcessHorizontalSpace=true;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.horizontalSpan = 6;
		gridData6.grabExcessHorizontalSpace=true;
		Label colorL = new Label(shell, SWT.BEGINNING);
		colorL.setText("Color settings");
		colorL.setLayoutData(gridData6);
		Label pl1 = new Label(shell, SWT.BEGINNING);
		pl1.setText(namePl1);
		pl1.setLayoutData(gridData6);
		final ResultSet pl1BO = addRow("Back Odds",gridData1, chart.getPl1Bo());
		final ResultSet pl1MA = addRow("Moving Average",gridData1, chart.getPl1MA());
		final ResultSet pl1Pred = addRow("Predicted Odds",gridData1, chart.getPl1Pred());
		Label pl2 = new Label(shell, SWT.BEGINNING);
		pl2.setText(namePl2);
		pl2.setLayoutData(gridData6);
		final ResultSet pl2BO = addRow("Back Odds",gridData1, chart.getPl2Bo());
		final ResultSet pl2MA = addRow("Moving Average",gridData1, chart.getPl2MA());
		final ResultSet pl2Pred = addRow("Predcted Odds",gridData1, chart.getPl2Pred());
		
		Button close = new Button(shell, SWT.PUSH);
		close.setText("Close");
		close.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				shell.dispose();
			}
			
		});
		
		Button apply = new Button(shell, SWT.PUSH);
		apply.setText("Apply");
		apply.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				chart.setPl1BO(pl1BO);
				chart.setPl1MA(pl1MA);
				chart.setPl1Pred(pl1Pred);
				chart.setPl2BO(pl2BO);
				chart.setPl2MA(pl2MA);
				chart.setPl2Pred(pl2Pred);
				shell.dispose();
			}
		
		});
		
		
	}

	private ResultSet addRow(String text, GridData gridData1, LineProp prop) {
		Label label = new Label(shell, SWT.BEGINNING);
		label.setText(text);
		Button areaB = new Button(shell, SWT.CHECK);
		areaB.setText("area");
		areaB.setSelection(prop.isArea());
		Button antialiasB = new Button(shell, SWT.CHECK);
		antialiasB.setText("antialias");
		antialiasB.setSelection(prop.isAntialias());
		Button stepB = new Button(shell, SWT.CHECK);
		stepB.setText("step");
		stepB.setSelection(prop.isStep());
		final Combo combo = new Combo(shell, SWT.NULL);
		for (Map.Entry<String, Color> entry : colorList.entrySet()){
			combo.add(entry.getKey());
		}
		combo.select(getNr(prop.getColor()));
		System.out.println(getNr(prop.getColor()));
		final Label colorLabel = new Label(shell, SWT.NONE);
		colorLabel.setLayoutData(gridData1);
		colorLabel.setBackground(
				colorList.get((String)combo.getItem(combo.getSelectionIndex())));
		combo.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e){
				colorLabel.setBackground(
						colorList.get((String)combo.getItem(combo.getSelectionIndex())));
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		ResultSet res = new ResultSet(areaB, antialiasB,stepB,colorLabel);
		return res;
	}

	private int getNr(Color color) {
		int i =0;
		for (Map.Entry<String, Color> entry : colorList.entrySet()){
			if (entry.getValue().equals(color))
				return i;
			i++;
		}
		return i;
	}

	public void forceActive() {
		shell.forceActive();
	}

	public boolean isDisposed() {
		return shell.isDisposed();
	}

	class ResultSet {
		private Button area;
		private Button antialias;
		private Button step; 
		private Label label;
		
		ResultSet(Button area, Button antialias, Button step, Label l){
			this.antialias=antialias;
			this.area=area;
			this.step=step;
			this.label = l;
		}

		public int getAntialias() {
			if (antialias.getSelection()) return SWT.ON;
			else return SWT.OFF;
		}

		public void setAntialias(Button antialias) {
			this.antialias = antialias;
		}

		public boolean getStep() {
			return step.getSelection();
		}

		public void setStep(Button step) {
			this.step = step;
		}

		public Color getColor() {
			return label.getBackground();
		}

		public void setLabel(Label label) {
			this.label = label;
		}

		public boolean getArea() {
			return area.getSelection();
		}

		public void setArea(Button area) {
			this.area = area;
		}
	}
	
}
