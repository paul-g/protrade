package org.ic.tennistrader.ui.richlist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class RichListView extends Composite{
	
	public static final String EVENT_TEXT = "RichListViewSelection";

	
	private List<RichListElement> elements = new ArrayList<RichListElement>();

	public RichListView(Composite parent, int style) {
		super(parent, style);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.pack = true;
		rowLayout.marginLeft = 20;
        rowLayout.marginTop = 20;
        rowLayout.marginRight = 20;
        rowLayout.marginBottom = 20;
        rowLayout.spacing = 10;
        rowLayout.wrap = false;
		setLayout(rowLayout);
		pack();
	}
	
	public void addElement(RichListElement rle){
		rle.setParent(this);
		elements.add(rle);
		pack();
	}

}
