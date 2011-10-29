package src.ui;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import src.score.PredictionGui;
import src.service.BetfairExchangeHandler;
import src.service.GraphUpdater;

public class DisplayPanel implements Listener {

	final CTabFolder folder;
	ArrayList<Chart> charts;

	public DisplayPanel(Composite parent) {
		folder = new CTabFolder(parent, SWT.RESIZE | SWT.BORDER);
		folder.setSimple(false);
		folder.setMinimizeVisible(true);
		folder.setMaximizeVisible(true);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		folder.setLayoutData(gridData);

		charts = new ArrayList<Chart>();
	}

	public void handleEvent(Event event) {
		TreeItem ti = (TreeItem) event.item;

		CTabItem[] items = folder.getItems();
		int pos = -1;

		for (int i = 0; pos == -1 && i < items.length; i++)
			if (items[i].getText().equals(ti.getText())) {
				pos = i;

			}

		// check new tab has been open
		if (pos == -1) {

			CTabItem item = new CTabItem(folder, SWT.CLOSE);
			item.setText(ti.getText());
			folder.setSelection(item);

			Composite comp = new Composite(folder, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			RowLayout rowLayout = new RowLayout();
			rowLayout.type = SWT.VERTICAL;
			FillLayout fillLayout = new FillLayout();
			fillLayout.type = SWT.VERTICAL;
			comp.setLayout(fillLayout);

			/*
			Chart chart = new Chart(comp, SWT.NONE);
			chart.getTitle().setText(ti.getText());
			GridData chartData = new GridData();
			chartData.horizontalSpan = 2;
			// chartData.horizontalAlignment = SWT.FILL;
			// chart.setLayoutData(chartData);
			charts.add(chart);
			*/

			Label marketData = new Label(comp, SWT.NONE);
			marketData.setText(BetfairExchangeHandler
					.getMarketOdds(NavigationPanel.getMatch(ti)
							.getEventBetfair()));

			addPredictionGui(comp, ti.getText());

			item.setControl(comp);		
									
			GraphUpdater gu = new GraphUpdater(ti.getText(), comp);
						
			// temporarily for filling charts with random data
			//fillChartData2(chart);

			comp.update();			
			
			gu.run();
			
		} else
			// just bring the required tab under focus
			folder.setSelection(pos);		
	}

	/*
	private void fillChartData2(Chart chart) {
		// temporarily for filling charts with random data
		double[] xSeries = new double[60];
		double[] ySeries = new double[60];
		double[] xSeries2 = new double[60];
		double[] ySeries2 = new double[60];
		Random randomGenerator = new Random();
		for (int i = 0; i < 60; i++) {
			xSeries[i] = i;
			xSeries2[i] = i;
			ySeries[i] = randomGenerator.nextDouble() + 1;
			ySeries2[i] = (Math.cos(i) + 2) / 2;
		}
		ISeriesSet seriesSet = chart.getSeriesSet();
		ILineSeries series = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "back odds");
		series.setXSeries(xSeries);
		series.setYSeries(ySeries);
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		series.setLineColor(color);
		series.setSymbolSize(4);
		ILineSeries series2 = (ILineSeries) seriesSet.createSeries(
				SeriesType.LINE, "MA-Fast");
		series2.setXSeries(xSeries2);
		series2.setYSeries(ySeries2);
		series.setSymbolType(PlotSymbolType.CROSS);
		series2.setSymbolType(PlotSymbolType.DIAMOND);
		final IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();
		// end filling charts
	}
	*/

	public void addTab(String text) {
		CTabItem cti = new CTabItem(folder, SWT.CLOSE);
		cti.setText(text);
		folder.setSelection(cti);
	}

	private void addPredictionGui(Composite composite, String title) {
		new PredictionGui(composite, title);
	}

}
