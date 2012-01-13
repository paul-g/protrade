package org.ic.tennistrader.ui.layout;

import java.util.ArrayList;
import java.util.List;

public class LayoutRegistry {

	private static final LayoutRegistry INSTANCE = new LayoutRegistry();
	private List<LayoutDescriptor> layouts = null;

	public static LayoutRegistry getInstance() {
		return INSTANCE;
	}

	private LayoutRegistry() {
		layouts = new ArrayList<LayoutDescriptor>();
		initChartMaster();
		initPrediction();
		initVirtualBetting();
	}

	private void initChartMaster() {
		addLayout(new LayoutDescriptor("Chart Master", "Many chart layout",
				"templates/chart-master/"));
	}

	private void initPrediction() {
		addLayout(new LayoutDescriptor("Prediction Master",
				"Prediction visual aids layout", "templates/prediction/"));
	}
	
	private void initVirtualBetting() {
		addLayout(new LayoutDescriptor("Virtual Betting",
				"Layout for virtual betting", "templates/virtual-betting/"));
	}

	private void addLayout(LayoutDescriptor ld) {
		layouts.add(ld);
	}

	public List<LayoutDescriptor> getAvailableLayouts() {
		return layouts;
	}
}
