/* Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **************************************************************************/
package src;

/*
 * BusyIndicator example snippet: display busy cursor during long running task
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;

public class HelloWorld {

public static void main (String [] args) {
	final Display display = new Display ();
	final Color black = display.getSystemColor (SWT.COLOR_BLACK);

	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	shell.setText("Tennis Trader");
	
	final Tree tree = new Tree (shell, SWT.BORDER);

	loadTennisMatches(tree);


	final TreeItem [] lastItem = new TreeItem [1];
	final TreeEditor editor = new TreeEditor (tree);
	tree.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			final TreeItem item = (TreeItem) event.item;
			if (item != null && item == lastItem [0]) {
				boolean showBorder = true;
				final Composite composite = new Composite (tree, SWT.NONE);
				if (showBorder) composite.setBackground (black);
				final Text text = new Text (composite, SWT.NONE);
				final int inset = showBorder ? 1 : 0;
				composite.addListener (SWT.Resize, new Listener () {
					public void handleEvent (Event e) {
						Rectangle rect = composite.getClientArea ();
						text.setBounds (rect.x + inset, rect.y + inset, rect.width - inset * 2, rect.height - inset * 2);
					}
				});
				Listener textListener = new Listener () {
					public void handleEvent (final Event e) {
						switch (e.type) {
							case SWT.FocusOut:
								item.setText (text.getText ());
								composite.dispose ();
								break;
							case SWT.Verify:
								String newText = text.getText ();
								String leftText = newText.substring (0, e.start);
								String rightText = newText.substring (e.end, newText.length ());
								GC gc = new GC (text);
								Point size = gc.textExtent (leftText + e.text + rightText);
								gc.dispose ();
								size = text.computeSize (size.x, SWT.DEFAULT);
								editor.horizontalAlignment = SWT.LEFT;
								Rectangle itemRect = item.getBounds (), rect = tree.getClientArea ();
								editor.minimumWidth = Math.max (size.x, itemRect.width) + inset * 2;
								int left = itemRect.x, right = rect.x + rect.width;
								editor.minimumWidth = Math.min (editor.minimumWidth, right - left);
								editor.minimumHeight = size.y + inset * 2;
								editor.layout ();
								break;
							case SWT.Traverse:
								switch (e.detail) {
									case SWT.TRAVERSE_RETURN:
										item.setText (text.getText ());
										//FALL THROUGH
									case SWT.TRAVERSE_ESCAPE:
										composite.dispose ();
										e.doit = false;
								}
								break;
						}
					}
				};
				text.addListener (SWT.FocusOut, textListener);
				text.addListener (SWT.Traverse, textListener);
				text.addListener (SWT.Verify, textListener);
				editor.setEditor (composite, item);
				text.setText (item.getText ());
				text.selectAll ();
				text.setFocus ();
			}
			lastItem [0] = item;
		}
	});
	
	final TabFolder tabFolder = new TabFolder (shell, SWT.BORDER);
	Rectangle clientArea = shell.getClientArea ();
	tabFolder.setLocation (clientArea.x, clientArea.y);
	for (int i=0; i<6; i++) {
		TabItem item = new TabItem (tabFolder, SWT.NONE);
		item.setText ("TabItem " + i);
		Button button = new Button (tabFolder, SWT.PUSH);
		button.setText ("Page " + i);
		item.setControl (button);
	}
	tabFolder.pack ();
	
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

	private static void loadTennisMatches(Tree tree){

		// Wimbledon
		TreeItem wimb = new TreeItem (tree, SWT.NONE);
		wimb.setText ("Wimbledon ");
		TreeItem feddjo = new TreeItem (wimb, SWT.NONE);
		feddjo.setText ("Federer vs Djokovic ");
		TreeItem nadmur = new TreeItem (wimb, SWT.NONE);
		nadmur.setText ("Nadal vs Murray ");

		// Piatra-Neamt
		TreeItem pno = new TreeItem (tree, SWT.NONE);
		pno.setText ("Piatra-Neamt Open");
		TreeItem paco = new TreeItem (pno, SWT.NONE);
		paco.setText ("Paul vs Corina");
		TreeItem fena = new TreeItem (pno, SWT.NONE);
		fena.setText ("Ferrer vs Nalbandian ");
	}
}
