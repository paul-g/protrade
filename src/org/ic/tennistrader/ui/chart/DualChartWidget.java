package org.ic.tennistrader.ui.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.ic.tennistrader.domain.ChartData;
import org.ic.tennistrader.domain.markets.MOddsMarketData;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.ui.widgets.MatchViewerWidget;
import org.ic.tennistrader.ui.widgets.WidgetType;

public class DualChartWidget extends MatchViewerWidget {

	private static Display display;
	private final UpdatableChart largeChart;
	private final OverroundChart smallChart;
	private final ChartData chartData;
	private final Match match;
	ChartSettings window = null;

	public static void main(String args[]) {
		display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		layout.makeColumnsEqualWidth = true;
		shell.setLayout(layout);
		Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");
		Label pl1 = new Label(shell, SWT.CENTER);
		pl1.setText("" + match.getPlayerOne());
		Button pl1BO = new Button(shell, SWT.CHECK);
		pl1BO.setText("Back Odds");
		Button pl1MA = new Button(shell, SWT.CHECK);
		pl1MA.setText("MA");
		Button pl1Pred = new Button(shell, SWT.CHECK);
		pl1Pred.setText("Pred");
		Button settings = new Button(shell, SWT.PUSH);
		settings.setText("Chart settings");
		Label pl2 = new Label(shell, SWT.CENTER);
		pl2.setText("" + match.getPlayerTwo());
		Button pl2BO = new Button(shell, SWT.CHECK);
		pl2BO.setText("Back Odds");
		Button pl2MA = new Button(shell, SWT.CHECK);
		pl2MA.setText("MA");
		Button pl2Pred = new Button(shell, SWT.CHECK);
		pl2Pred.setText("Pred");

		settings.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {

				Shell shell = new Shell(display, SWT.SHELL_TRIM);
				shell.open();
			}

		});

		//

		//
		// new DualChartWidget(shell, match);
		//
		// shell.setMaximized(true);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public DualChartWidget(Composite parent, Match match) {
		super(parent, SWT.NONE);
		chartData = new ChartData(match);
		this.match = match;
		setLayout(new FillLayout());

		SashForm form = new SashForm(this, SWT.VERTICAL);
		Composite settingsPanel = new Composite(form, SWT.BORDER);
		initSettingsPanel(settingsPanel);

		Slider slider = null;

		largeChart = new UpdatableChart(form, SWT.BORDER, match, slider,
				chartData);

		smallChart = new OverroundChart(form, SWT.NONE, match, largeChart,
				chartData, slider);

		slider = new Slider(form, SWT.HORIZONTAL);
		initSlider(slider);

		form.setWeights(new int[] { 7, 65, 25, 3 });
	}

	private void initSettingsPanel(Composite settingsPanel) {
		settingsPanel.setLayout(new FillLayout());
		Button settings = new Button(settingsPanel, SWT.PUSH);
		settings.setText("Chart settings");
		settings.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				String namePl1 = match.getPlayerOne().toString();
				String namePl2 = match.getPlayerTwo().toString();
				if (window == null || window.isDisposed()) {
					window = new ChartSettings(display, largeChart, namePl1,
							namePl2);
				} else {
					window.forceActive();
				}
			}
		});
	}

	private void initSlider(Slider slider) {
		slider.setMaximum(1);
		slider.setValues(0, 0, 1, 0, 0, 0);
		slider.setMaximum(1);
		slider.setSelection(0);
		slider.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Slider slider = (Slider) event.widget;
				if (slider.getMaximum() > largeChart.getSampleSize())
					largeChart.showSeries(slider.getSelection(), true);
				smallChart.showSeries(slider.getSelection(), true);
			}
		});
		largeChart.setSlider(slider);
		smallChart.setSlider(slider);
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

	@Override
	public WidgetType getName() {
		return WidgetType.DUAL_CHART;
	}
}
