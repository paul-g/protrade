package org.ic.protrade.ui.layout;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class LayoutDescriptor {

	private String title;
	private String description;
	private Image largeImage;
	private Image smallImage;
	private String path;
	
	private static final int IMAGE_WIDTH = 150;
	private static final int IMAGE_HEIGHT = 150;
	
	public LayoutDescriptor(String title, String description, String path) {
		super();
		this.title = title;
		this.description = description;
		this.path = path + "/dashboard.dat";
		
		ImageRegistry ir = JFaceResources.getImageRegistry();
		Image largeImage = new Image(Display.getCurrent(), path + "large.png");
		Image smallImage = new Image(Display.getCurrent(), largeImage.getImageData().scaledTo(IMAGE_WIDTH, IMAGE_HEIGHT));
		ir.put(title + "-large", largeImage);
		ir.put(title + "-small", smallImage);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLargeImage(Image largeImage) {
		this.largeImage = largeImage;
	}

	public void setSmallImage(Image smallImage) {
		this.smallImage = smallImage;
	}

	public String getTitle() {
		return title;
	}

	public Image getSmallImage() {
		return smallImage;
	}

	public Image getLargeImage() {
		return largeImage;
	}

	public String getPath() {
		return path;
	}
}