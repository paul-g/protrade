package org.ic.tennistrader.ui.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.ic.tennistrader.ui.dialogs.RichListDialog;
import org.ic.tennistrader.ui.layout.LayoutDescriptor;
import org.ic.tennistrader.ui.layout.LayoutRegistry;
import org.ic.tennistrader.ui.richlist.RichListElement;
import org.ic.tennistrader.ui.richlist.RichListView;

public class LayoutDialog extends RichListDialog {

	private static final Logger log = Logger.getLogger(LayoutDialog.class);

	private String selection;

	public String getSelection() {
		return selection;
	}

	@Override
	protected void addElements(RichListView r) {
		List<LayoutDescriptor> layouts = LayoutRegistry.getInstance()
				.getAvailableLayouts();

		for (LayoutDescriptor ld : layouts) {
			final String text = ld.getPath();
			Control control = makeElementControl(new Listener() {
				@Override
				public void handleEvent(Event e) {
					log.info("Layout chosen " + text);
					selection = text;
					dialog.dispose();
				}
			});

			RichListElement element = new RichListElement(r, SWT.BORDER,
					ld.getDescription(), ld.getTitle(), ld.getSmallImage(),
					control);
		}
	}
}