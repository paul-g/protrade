package org.ic.tennistrader.ui.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartMenu {
	private List<SeriesProperties> seriesItems;
	
	public ChartMenu() {
		seriesItems = new ArrayList<SeriesProperties>();
	}
	
	public void addSeriesItem(SeriesProperties newSeriesProperties) {
		seriesItems.add(newSeriesProperties);
	}

	public void setSeriesItems(List<SeriesProperties> seriesItems) {
		this.seriesItems = seriesItems;
	}

	public List<SeriesProperties> getSeriesItems() {
		return seriesItems;
	}	
}
