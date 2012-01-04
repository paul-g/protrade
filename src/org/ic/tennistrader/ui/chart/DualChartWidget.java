package org.ic.tennistrader.ui.chart;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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

	private final UpdatableChart largeChart;
	private final OverroundChart smallChart;
	private ChartData chartData;
	private Match match;
	private ChartSettings window = null;

	private static final Logger log = Logger.getLogger(DualChartWidget.class);

	public static void main(String args[]) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);

		shell.setLayout(new FillLayout());

		Match match = new HistoricalMatch("data/fracsoft/fracsoft1.csv");

		// new DualChartWidget(shell, match);
		new DualChartWidget(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public DualChartWidget(Composite parent) {
		super(parent, SWT.NONE);
		match = null;
		chartData = null;
		setLayout(new FillLayout());

		SashForm form = new SashForm(this, SWT.VERTICAL);
		Composite settingsPanel = new Composite(form, SWT.BORDER);
		initSettingsPanel(settingsPanel);

		Slider slider = null;

		largeChart = new UpdatableChart(form, SWT.NONE, slider, chartData);

		smallChart = new OverroundChart(form, SWT.NONE, largeChart, chartData,
				slider);

		slider = new Slider(form, SWT.HORIZONTAL);
		initSlider(slider);

		form.setWeights(new int[] { 7, 65, 25, 3 });

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
					window = new ChartSettings(Display.getCurrent(),
							largeChart, namePl1, namePl2);
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
	public WidgetType getWidgetType() {
		return WidgetType.DUAL_CHART;
	}

	@Override
	public void setMatch(Match match) {
		log.info("Setting match to " + match);
		this.match = match;
		this.chartData = new ChartData(match);
		largeChart.setMatch(match);
		largeChart.setChartData(chartData);
	}
}
