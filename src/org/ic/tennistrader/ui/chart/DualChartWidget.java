package org.ic.tennistrader.ui.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;

public class DualChartWidget extends Composite implements UpdatableWidget{
	
	private final UpdatableChart largeChart;
	private final OverroundChart smallChart;
	private final ChartData chartData;
	private final Match match;
	
	public static void main(String args[]) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout());
		
		Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
		
		new DualChartWidget(shell,  match);
		
		shell.setMaximized(true);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public DualChartWidget(Composite parent, Match match){
		super(parent, SWT.NONE);
		chartData = new ChartData(match);
		setLayout(new FillLayout());
		this.match = match;
		SashForm form = new SashForm(this, SWT.VERTICAL);
		
		Slider slider = new Slider(form, SWT.HORIZONTAL);
		slider.setMaximum(1);
		slider.setValues(0, 0, 1, 0, 0, 0);
		
		largeChart = new UpdatableChart(form, SWT.BORDER, match,
				slider, chartData);
		
		smallChart = new OverroundChart(form, SWT.NONE, match, largeChart, chartData, slider);
		
		form.setWeights(new int[]{5,70,25});
		
		DataManager.registerForMatchUpdate(this, match);
	}

	@Override
	public void handleUpdate(final MOddsMarketData newData) {
		chartData.updateData(newData);
		largeChart.handleUpdate(newData);
		smallChart.handleUpdate(newData);
	}

	@Override
	public void setDisposeListener(DisposeListener listener) {
		largeChart.setDisposeListener(listener);
		smallChart.setDisposeListener(listener);
	}

	@Override
	public void handleBettingMarketEndOFSet() {
		// TODO Auto-generated method stub
		
	}
}
