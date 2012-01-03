package org.ic.tennistrader.ui.layout;

import java.util.ArrayList;
import java.util.List;

public class LayoutRegistry {

	private static final LayoutRegistry INSTANCE = new LayoutRegistry();
	private List<LayoutDescriptor> layouts = null;

	public static LayoutRegistry getInstance(){
		return INSTANCE;
	}
	
	private LayoutRegistry() {
		layouts = new ArrayList<LayoutDescriptor>();
		initChartMaster();
	}

	private void initChartMaster() {
		addLayout(new LayoutDescriptor("Chart Master", "Many chart layout",
				"templates/chart-master/"));
	}

	private void addLayout(LayoutDescriptor ld) {
		layouts.add(ld);
	}

	public List<LayoutDescriptor> getAvailableLayouts() {
		return layouts;
	}
}
