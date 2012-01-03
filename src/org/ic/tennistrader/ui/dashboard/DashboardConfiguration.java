package org.ic.tennistrader.ui.dashboard;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.score.StatisticsPanel;
import org.ic.tennistrader.service.DataManager;
import org.ic.tennistrader.ui.chart.DualChartWidget;
import org.ic.tennistrader.ui.score.WimbledonScorePanel;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;

public class DashboardConfiguration {

	private static final Logger log = Logger
			.getLogger(DashboardConfiguration.class);

	private int defaultWidgetWidth;
	private int defaultWidgetHeight;
	private Map<WidgetContainer, Point> widgetMap;

	private final Dashboard dashboard;

	public DashboardConfiguration(Dashboard dashboard) {
		widgetMap = new HashMap<WidgetContainer, Point>();
		this.dashboard = dashboard;
	}

	public Map<WidgetContainer, Point> getWidgetMap() {
		return widgetMap;
	}

	public void setWidgetMap(Map<WidgetContainer, Point> widgetMap) {
		this.widgetMap = widgetMap;
	}

	public int getDefaultWidgetHeight() {
		return defaultWidgetHeight;
	}

	public void setDefaultWidgetHeight(int defaultWidgetHeight) {
		this.defaultWidgetHeight = defaultWidgetHeight;
	}

	public int getDefaultWidgetWidth() {
		return defaultWidgetWidth;
	}

	public void setDefaultWidgetWidth(int defaultWidgetWidth) {
		this.defaultWidgetWidth = defaultWidgetWidth;
	}

	public Point getLocation(WidgetContainer wc) {
		return widgetMap.get(wc);
	}

	public void updateLocation(WidgetContainer wc, Point point) {
		widgetMap.put(wc, point);
	}

	public void save(String filename) {
		try {
			BufferedWriter out = openFile(filename);
			Rectangle displayArea = Display.getCurrent().getClientArea();
			out.write(displayArea.width + "x" + displayArea.height + "\n");
			out.write("class,x,y,width,height\n");
			Set<WidgetContainer> widgets = widgetMap.keySet();
			for (WidgetContainer wc : widgets)
				writeWidget(out, wc, widgetMap.get(wc));
			out.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void writeWidget(BufferedWriter out, WidgetContainer wc, Point point)
			throws IOException {
		String line = "" + wc.getWidget().getClass().toString().substring(5)
				+ "," + point.x + "," + point.y;
		line += "," + wc.getWidth() + "," + wc.getHeight();
		out.write(line + "\n");
	}

	private BufferedWriter openFile(String filename) throws IOException {
		FileWriter fstream = new FileWriter(filename);
		BufferedWriter out = new BufferedWriter(fstream);
		return out;
	}

	public void load(String filename) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(filename));

		Match match = new HistoricalMatch("data/full/fulldataShort.csv");

		String resolution = scanner.nextLine();
		String dimensions[] = resolution.split("x");
		int width = Integer.parseInt(dimensions[0]);
		int height = Integer.parseInt(dimensions[1]);

		log.info("Identified dashboard native resolution " + height + "x"
				+ width);

		// skip header
		scanner.nextLine();

		Rectangle area = Display.getCurrent().getClientArea();
		log.info("Rescaling dashboard. Dashboard client area: " + area);
		log.info("Current display size is : "
				+ Display.getCurrent().getClientArea());
		double xRatio = (double) width / area.width;
		double yRatio = (double) height / area.height;

		log.info("xRatio: " + xRatio + " yRatio: " + yRatio);

		while (scanner.hasNext()) {
			String s = scanner.nextLine();
			String line[] = s.split(",");
			WidgetContainer wc = new WidgetContainer(dashboard, SWT.BORDER, 10,
					20);

			if (line[0].trim().equals(
					"org.ic.tennistrader.ui.chart.DualChartWidget")) {
				wc.setWidget(new DualChartWidget(wc, match));
				log.info("Added chart");
			} else if (line[0].trim().equals(
					"org.ic.tennistrader.ui.score.WimbledonScorePanel")) {
				wc.setWidget(new WimbledonScorePanel(wc, match));
				log.info("Added score panel");
			} else if (line[0].trim().equals(
					"org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid")) {
				UpdatableMarketDataGrid grid = new UpdatableMarketDataGrid(wc,
						SWT.NONE, match);
				DataManager.registerForMatchUpdate(grid, match);
				wc.setWidget(grid);
				log.info("Added chart");
			} else if (line[0].trim().equals(
					"org.ic.tennistrader.score.StatisticsPanel")) {
				StatisticsPanel sp = new StatisticsPanel(wc, match);
				wc.setWidget(sp);
			} else {
				wc.setWidget(new WidgetPlaceholder(dashboard, SWT.BORDER, wc));
			}

			int x = Integer.parseInt(line[1]);
			int y = Integer.parseInt(line[2]);

			wc.setWidth(Integer.parseInt(line[3]));
			wc.setHeight(Integer.parseInt(line[4]));

			updateLocation(wc, new Point(x, y));
		}

		dashboard.scaleWidgets(xRatio, yRatio);
		dashboard.layoutWidgets();

		log.info("Dashboard rescaled");
	}

	public void setMatch(Match match) {
		log.info("Set match to " + match);
	}
}