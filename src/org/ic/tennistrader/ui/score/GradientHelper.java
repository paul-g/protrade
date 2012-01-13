package org.ic.tennistrader.ui.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class GradientHelper {

	private static Image oldImage = null;

	public static void applyGradientBG(Composite composite) {
		Rectangle rect = composite.getBounds();//composite.getClientArea();
		int height = rect.height;
		height = composite.getSize().y;
		Image newImage = new Image(composite.getDisplay(), 1, Math.max(1,
				height));
		GC gc = new GC(newImage);
		gc
				.setForeground(composite.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
		gc.setBackground(composite.getDisplay().getSystemColor(
				SWT.COLOR_BLACK));
		gc.fillGradientRectangle(0, 0, 1, height, true);
		gc.dispose();
		composite.setBackgroundImage(newImage);
		
		if (oldImage != null)
			oldImage.dispose();
		oldImage = newImage;
	}
}