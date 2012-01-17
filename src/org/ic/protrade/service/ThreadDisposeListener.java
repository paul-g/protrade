package org.ic.protrade.service;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.ic.protrade.data.match.Match;
import org.ic.protrade.ui.updatable.UpdatableWidget;

public class ThreadDisposeListener implements DisposeListener {

	private final UpdatableWidget widget;
	private final Match match;

	public ThreadDisposeListener(UpdatableWidget widget, Match match) {
		super();
		this.widget = widget;
		this.match = match;
	}

	@Override
	public void widgetDisposed(DisposeEvent arg0) {
		DataManager.unregister(widget, match);
		final Logger log = Logger.getLogger(ThreadDisposeListener.class);
		log.info("Disposed widget " + widget.getClass()
				+ " listening on match " + match.toString());
	}
}
