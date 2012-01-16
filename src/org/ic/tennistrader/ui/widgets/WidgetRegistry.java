package org.ic.tennistrader.ui.widgets;

import java.util.ArrayList;
import java.util.List;

public class WidgetRegistry {

	private static final WidgetRegistry INSTANCE = new WidgetRegistry();
	private List<WidgetDescriptor> widgets = null;

	public static WidgetRegistry getInstance() {
		return INSTANCE;
	}

	private WidgetRegistry() {
		widgets = new ArrayList<WidgetDescriptor>();
		initMarketGrid();
	}

	private void initMarketGrid() {
		addWidget(new WidgetDescriptor());
	}

	private void addWidget(WidgetDescriptor ld) {
		widgets.add(ld);
	}

	public List<WidgetDescriptor> getAvailableLayouts() {
		return widgets;
	}
}
