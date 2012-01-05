package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.ic.tennistrader.domain.profile.AccountFunds;
import org.ic.tennistrader.domain.profile.ProfileData;

public class ProfileWindow {
	private ProfileData profileData;
	private Shell shell;
	private Display display;
	private GridData gd;

	public static void main (String[] args) {
		Display d = new Display();
		ProfileWindow p = new ProfileWindow(d, null);
		p.stay();
	}

	/** Constructor */
	public ProfileWindow(Display display, ProfileData profdata) {

		/* Initialisation */
		this.display = display;
		shell = new Shell(display,SWT.SHELL_TRIM);
		profileData = profdata;

		/* Grid Layout of the page */
		GridLayout gl = new GridLayout(3,false);
		gd = new GridData(SWT.CENTER,SWT.CENTER,true,true);
		gd.minimumWidth = 150;

		/* Shell Layout */
		shell.setLayout(gl);
		shell.setText("Profile");
		shell.setSize(500,500);
		shell.setLocation(1100,80);

		/* Labels and Shell opening */
		fillProfile(gd);
		shell.open();
	}

	/** Text representation of the profile */
	private void fillProfile(GridData gd) {
		AccountFunds af = null;
		try {
			af = profileData.getUkAccountFunds();
		} catch (Exception e) {
			e.getMessage();
		}
		addDescriptionLabel(new Image(display,"images/profile/user.png"),3);
		addLabel("USERNAME",toS(profileData.getUsername()));
		addLabel("FIRST NAME", toS(profileData.getFirstName()));
		addLabel("LAST NAME", toS(profileData.getSurname()));
		addDescriptionLabel(new Image(display,"images/profile/address.png"),3);
		addLabel("ADDRESS 1", toS(profileData.getAddress1()));
		addLabel("ADDRESS 2", toS(profileData.getAddress2()));
		addLabel("ADDRESS 3", toS(profileData.getAddress3()));
		addDescriptionLabel(new Image(display,"images/profile/location.png"),3);
		addLabel("CITY",toS(profileData.getTownCity()));
		addLabel("POST CODE", toS(profileData.getPostCode()));
		addLabel("COUNTRY", toS(profileData.getCountry()));
		addDescriptionLabel(new Image(display,"images/profile/contacts.png"),3);
		addLabel("HOME PHONE", toS(profileData.getHomePhone()));
		addLabel("MOBILE PHONE", toS(profileData.getMobilePhone()));
		addLabel("E-MAIL ADDRESS", toS(profileData.getEmailAddress()));
		addDescriptionLabel(new Image(display,"images/profile/deposit.png"),3);
		addLabel("BETFAIR POINTS", toS(af.getBetfairPoints()));
		addLabel("CURRENT BALANCE", toS(af.getBalance()));
		addLabel("AVAILABLE BALANCE", toS(af.getAvailable()));
		addDescriptionLabel(new Image(display,"images/profile/credit.png"),3);
		addLabel("CREDIT LIMIT", toS(af.getCreditLimit()));
		addLabel("EXPOSURE", toS(af.getExposure()));
		addLabel("EXPOSURE LIMIT", toS(af.getExposureLimit()));
	}

	/** Method for description labels */
	private void addDescriptionLabel(Image img, int span) {
		GridData ngd = new GridData(SWT.CENTER,SWT.CENTER,true,true);
		ngd.verticalSpan = span;
		Label descr = new Label(shell, SWT.NONE);
		descr.setImage(img);
		descr.setLayoutData(ngd);
	}

	/** Method for label addition */
	private void addLabel(String key,String value) {
		Label nkey = new Label(shell, SWT.BORDER);
		nkey.setText(key);
		nkey.setLayoutData(gd);
		Text nvalue = new Text(shell, SWT.BORDER);
		nvalue.setText(value);
		nvalue.setLayoutData(gd);
	}

	/** External dispose check */
	public boolean isDisposed() {
		return shell.isDisposed();
	}

	/** External method for forcing */
	public void forceActive() {
		shell.forceActive();
	}

	/** Method for keeping the shell */
	public void stay() {
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch() ) display.sleep();
		}
		display.dispose();
	}

	/** Method for String permutation */
	private String toS (Object o) {
		if (o != null) {
			return o.toString();
		} else {
			return "";
		}
	}
}


/* TEST */
//addDescriptionLabel(new Image(display,"images/profile/user.png"),3);
//addLabel("USERNAME","");
//addLabel("FIRST NAME", "");
//addLabel("LAST NAME", "");
//addDescriptionLabel(new Image(display,"images/profile/address.png"),3);
//addLabel("ADDRESS 1", "");
//addLabel("ADDRESS 2", "");
//addLabel("ADDRESS 3", "");
//addDescriptionLabel(new Image(display,"images/profile/location.png"),3);
//addLabel("CITY", "");
//addLabel("POST CODE", "");
//addLabel("COUNTRY", "");
//addDescriptionLabel(new Image(display,"images/profile/contacts.png"),3);
//addLabel("HOME PHONE", "");
//addLabel("MOBILE PHONE", "");
//addLabel("E-MAIL ADDRESS","");
//addDescriptionLabel(new Image(display,"images/profile/deposit.png"),3);
//addLabel("BETFAIR POINTS","");
//addLabel("CURRENT BALANCE","");
//addLabel("AVAILABLE BALANCE","");
//addDescriptionLabel(new Image(display,"images/profile/credit.png"),3);
//addLabel("CREDIT LIMIT","");
//addLabel("EXPOSURE","");
//addLabel("EXPOSURE LIMIT","");