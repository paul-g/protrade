package org.ic.tennistrader.listener;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Text;

public class HoverListener implements MouseListener {
	private Text text;

	public HoverListener ( Text text ) {
		this.text = text;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) { 
	}

	@Override
	public void mouseDown(MouseEvent e) {
		text.setText("");
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

}