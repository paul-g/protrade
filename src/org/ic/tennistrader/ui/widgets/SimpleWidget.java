package org.ic.tennistrader.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SimpleWidget extends Composite{
	
	//private Button
	private CornerMenu cornerMenu;

	SimpleWidget(final Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FillLayout());
		
		this.cornerMenu = new CornerMenu(this, SWT.BORDER);
		
		this.setSize(40, 40);
		
		this.addMouseTrackListener(new MenuListener(cornerMenu));
	}

	class CornerMenu extends Composite {
		CornerMenu(Composite parent, int style){
			super(parent, style);
			this.setLayout(new FillLayout());
			Button close = new Button(this, SWT.PUSH);
			close.setText("X");
			Button menu = new Button(this, SWT.PUSH);
			menu.setText(">");
			this.setSize(20, 20);
			this.setVisible(false);
		}
	}
	
	class MenuListener extends MouseTrackAdapter{
		
		private CornerMenu cornerMenu;
		
		public MenuListener(CornerMenu cornerMenu){
			this.cornerMenu = cornerMenu;
		}
		
		@Override
		public void mouseExit(MouseEvent arg0) {
			System.out.println("Exit ");
			//cornerMenu.setVisible(false);
		}
		
		@Override	
		public void mouseEnter(MouseEvent arg0) {
			System.out.println("Entered ");
			//cornerMenu.setVisible(true);
		}
	}
}