package org.ic.protrade.ui.chart;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.ic.protrade.data.match.PlayerEnum;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;

public class ChartSettings extends Dialog{	
	private UpdatableChart chart;
	
	private TreeMap<String, PlotSymbolType> symbolList = new TreeMap<String, PlotSymbolType>();
	private String namePl1, namePl2;	
	
	private HashMap<SeriesProperties, ResultSet> propertiesResults = new HashMap<SeriesProperties, ResultSet>();
	
	private Composite composite;
	
	/*
	public static void main(String[] args){
		Display d = new Display();
		ChartSettings c = new ChartSettings(d,null, "nadal", "pavel");
		while (!c.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();
	}
	*/
	
	public ChartSettings(Composite settingsPanel, UpdatableChart largeChart) {
		super(settingsPanel.getShell());
		// this.display = Display.getCurrent();
		this.chart = largeChart;
		this.namePl1 = largeChart.getPlayer1Name();
		this.namePl2 = largeChart.getPlayer2Name();
		// initColorList();
		
		initSymbolList();
		
		//initWindow(namePl1, namePl2);
		
		//mainShell.open();

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.composite = (Composite) super.createDialogArea(parent);
		initWindow(namePl1, namePl2);
		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// createButton(parent, RESET_ID, "Reset All", false);
	}
	
	@Override
	protected void okPressed(){
		for (SeriesProperties prop : propertiesResults.keySet()) {
			prop.setLineProp(propertiesResults.get(prop));
		}
		super.okPressed();
	}
	
	
	private void initSymbolList() {
		symbolList.put("Circle", PlotSymbolType.CIRCLE);
		symbolList.put("Square", PlotSymbolType.SQUARE);
		symbolList.put("Diamond", PlotSymbolType.DIAMOND);
		symbolList.put("Triangle", PlotSymbolType.TRIANGLE);
		symbolList.put("Inverted Triangle", PlotSymbolType.INVERTED_TRIANGLE);
		symbolList.put("Cross", PlotSymbolType.CROSS);
		symbolList.put("Plus", PlotSymbolType.PLUS);
		symbolList.put("None", PlotSymbolType.NONE);
	}
	
	/*
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
	*/

	private void initWindow(String namePl1, String namePl2) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 8; 
		//layout.makeColumnsEqualWidth = true;
		composite.setLayout(layout);
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.horizontalSpan = 1;
		gridData1.grabExcessHorizontalSpace=true;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.horizontalSpan = 8;
		gridData6.grabExcessHorizontalSpace=true;
		
		FontRegistry fontRegistry = new FontRegistry(Display.getCurrent());
		fontRegistry.put("label1", new FontData[] { new FontData("Arial", 14,
				SWT.BOLD) });
		fontRegistry.put("label", new FontData[] { new FontData("Courier New",
				12, SWT.BOLD) });
		
		Label colorL = new Label(composite, SWT.BEGINNING);
		colorL.setText("Chart settings");
		colorL.setFont(fontRegistry.get("label1"));
		colorL.setLayoutData(gridData6);
		
		/*
		Label pl1 = new Label(shell, SWT.BEGINNING);
		pl1.setText(namePl1);
		pl1.setFont(fontRegistry.get("label"));
		pl1.setLayoutData(gridData1);
		*/
		
		for (SeriesProperties prop : this.chart.getChartMenu().getSeriesItems()) {
			if (prop.getPlayer().equals(PlayerEnum.PLAYER1) && prop.getChartSeries() != null
					&& prop.getChartSeries() instanceof ILineSeries) {
				ResultSet resultSet = addRow(prop.getFullName(), gridData6, prop.getLineProp());
				propertiesResults.put(prop, resultSet);
			}
				
		}
		
		/*
		Label pl2 = new Label(shell, SWT.BEGINNING);
		pl2.setText(namePl2);
		pl2.setFont(fontRegistry.get("label"));
		pl2.setLayoutData(gridData1);
		*/
		
		for (SeriesProperties prop : this.chart.getChartMenu().getSeriesItems()) {
			if (prop.getPlayer().equals(PlayerEnum.PLAYER2) && prop.getChartSeries() != null
					&& prop.getChartSeries() instanceof ILineSeries) {
				ResultSet resultSet = addRow(prop.getFullName(), gridData6, prop.getLineProp());
				propertiesResults.put(prop, resultSet);
			}
				
		}
		
		/*
		Button close = new Button(mainShell, SWT.PUSH);
		close.setText("Close");
		close.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				mainShell.dispose();
			}
			
		});
		
		Button apply = new Button(mainShell, SWT.PUSH);
		apply.setText("Apply");
		apply.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				
				//chart.setPl1BO(pl1BO);
				//chart.setPl1MA(pl1MA);
				//chart.setPl1Pred(pl1Pred);
				//chart.setPl2BO(pl2BO);
				//chart.setPl2MA(pl2MA);
				//chart.setPl2Pred(pl2Pred);
				
				for (SeriesProperties prop : results.keySet()) {
					prop.setLineProp(results.get(prop));
				}
				mainShell.dispose();
			}
		
		});		
		*/
	}
	

	private ResultSet addRow(String text, GridData gridData,
			final LineProp prop) {
		Group g = new Group(composite, SWT.SHADOW_ETCHED_IN);
		GridLayout layout = new GridLayout();
		layout.numColumns = 7;
		g.setLayout(layout);
		g.setLayoutData(gridData);
		g.setText(text);
		Button areaB = new Button(g, SWT.CHECK);
		areaB.setText("area");
		areaB.setSelection(prop.isArea());
		Button antialiasB = new Button(g, SWT.CHECK);
		antialiasB.setText("antialias");
		antialiasB.setSelection(prop.isAntialias());
		Button stepB = new Button(g, SWT.CHECK);
		stepB.setText("step");
		stepB.setSelection(prop.isStep());
		Label symbL = new Label(g, SWT.NULL);
		symbL.setText("Symbol:");
		final Combo combo = new Combo(g, SWT.NULL);
		for (Map.Entry<String, PlotSymbolType> entry : symbolList.entrySet()) {
			combo.add(entry.getKey());
		}
		combo.select(getNr(prop.getSymbolType()));
		final Button b = new Button(g, SWT.PUSH | SWT.BORDER);
		final Text t = new Text(g, SWT.BORDER | SWT.MULTI);
		t.setBackground(prop.getColor());
		b.setText("Change Line Color");
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(ChartSettings.this.getShell());
				cd.setText("ColorDialog Demo");
				cd.setRGB(prop.getColor().getRGB());
				RGB newColor = cd.open();
				if (newColor == null) {
					return;
				}
				Color newC = new Color(Display.getCurrent(), newColor);
				t.setBackground(newC);
			}
		});
		ResultSet res = new ResultSet(areaB, antialiasB, stepB, t,
				combo);
		return res;
	}

	private int getNr(PlotSymbolType symb) {
		int i = 0;
		for (Map.Entry<String, PlotSymbolType> entry : symbolList.entrySet()) {
			if (entry.getValue().equals(symb))
				return i;
			i++;
		}
		return i;
	}
	
	/*
	public void forceActive() {
		mainShell.forceActive();
	}

	public boolean isDisposed() {
		return mainShell.isDisposed();
	}
	*/
	

	public String getNamePl1() {
		return namePl1;
	}

	public void setNamePl1(String namePl1) {
		this.namePl1 = namePl1;
	}

	public String getNamePl2() {
		return namePl2;
	}

	public void setNamePl2(String namePl2) {
		this.namePl2 = namePl2;
	}


	class ResultSet {
		private Button area;
		private Button antialias;
		private Button step;
		private Text text;
		private Combo symbol;

		ResultSet(Button area, Button antialias, Button step, Text t,
				Combo symbol) {
			this.antialias = antialias;
			this.area = area;
			this.step = step;
			this.text = t;
			this.symbol = symbol;
		}

		public int getAntialias() {
			if (antialias.getSelection())
				return SWT.ON;
			else
				return SWT.OFF;
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
			return text.getBackground();
		}

		public void setText(Text text) {
			this.text = text;
		}

		public boolean getArea() {
			return area.getSelection();
		}

		public void setArea(Button area) {
			this.area = area;
		}
		
		public PlotSymbolType getSymbolType(){
			return symbolList.get(symbol.getItem(symbol.getSelectionIndex()));
		}

	}
	
}
