package org.ic.tennistrader.ui.richlist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

public class RichListElement extends Composite {

	private List<Listener> infoListeners = new ArrayList<Listener>();
	private List<Listener> actionListeners = new ArrayList<Listener>();

	private String description;
	private String name;
	private Image image;

	private static final String DEFAULT_IMAGE_KEY = "DefaultRichListElement";
	private static final String DEFAULT_IMAGE_PATH = "images/richlist/default.png";
	private static final int NCOLS = 5;
	private static final int NROWS = 3;

	private static final int IMAGE_WIDTH = 150;
	private static final int IMAGE_HEIGHT = 150;

	private RichListView parentList;

	private Control control;

	public RichListElement(RichListView parent, int style, String description,
			String name) {
		super(parent, style);
		init(parent, style, description, name, null);
		makeDescription();

	}

	public RichListElement(RichListView parent, int style, String description,
			String name, Control control) {
		super(parent, style);
		init(parent, style, description, name, null);
		setElementControl(control);
		makeDescription();
	}

	public RichListElement(RichListView parent, int style, String description,
			String name, Image image) {
		super(parent, style);
		init(parent, style, description, name, image);
		makeDescription();
	}

	public RichListElement(RichListView parent, int style, String description,
			String name, Image image, Control control) {
		super(parent, style);
		init(parent, style, description, name, image);
		setElementControl(control);
		makeDescription();
	}

	private void init(RichListView parentList, int style, String description,
			String name, Image image) {
		this.parentList = parentList;
		this.description = description;
		this.name = name;
		setImage(image);
		setLayout(makeLayout());
		makeImageControl();
		makeTitle();
	}

	private void setElementControl(Control control) {
		this.control = control;
		control.setParent(this);
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
	}

	private Control makeDescription() {
		Label l = new Label(this, SWT.BORDER);
		l.setText(description);
		l.setLayoutData(makeDescriptionLayoutData());
		return l;
	}

	private Object makeDescriptionLayoutData() {
		return new GridData(SWT.FILL, SWT.FILL, true, true, NCOLS - 2,
				NROWS - 1);
	}

	private Control makeTitle() {
		Label l = new Label(this, SWT.BORDER);
		l.setText(name);
		l.setLayoutData(makeTitleLayoutData());
		return l;
	}

	private Object makeTitleLayoutData() {
		return new GridData(SWT.FILL, SWT.FILL, true, false, NCOLS - 2, 1);
	}

	private Control makeImageControl() {
		Button button = new Button(this, SWT.PUSH | SWT.FLAT);
		button.setLayoutData(makeImageControlLayoutData());
		button.setImage(new Image(Display.getCurrent(), image.getImageData()
				.scaledTo(IMAGE_WIDTH, IMAGE_HEIGHT)));

		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				handleInfo();
			}

		});
		return button;
	}

	private Object makeImageControlLayoutData() {
		return new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
	}

	private Image getDefaultImage() {
		ImageRegistry ir = JFaceResources.getImageRegistry();
		Image image = ir.get(DEFAULT_IMAGE_KEY);
		if (image == null) {
			image = new Image(Display.getCurrent(), DEFAULT_IMAGE_PATH);
			ir.put(DEFAULT_IMAGE_KEY, image);
		}
		return image;
	}

	private void setImage(Image image) {
		if (image == null) {
			this.image = getDefaultImage();
		} else {
			this.image = image;
		}
	}

	private Layout makeLayout() {
		GridLayout layout = new GridLayout(NCOLS, true);
		return layout;
	}

	public void handleAction() {
		for (Listener l : actionListeners) {
			l.handleEvent(new Event());
		}
	}

	private void handleInfo() {
		for (Listener l : infoListeners) {
			l.handleEvent(new Event());
		}
	}

	public void addActionListener(Listener listener) {
		actionListeners.add(listener);
	}

	public void addInfoListener(Listener listener) {
		infoListeners.add(listener);
	}
}