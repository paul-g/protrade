package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.Main;
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
		shell.setSize(400,300);
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
		new Label(shell, SWT.NONE).setImage(new Image(display,"images/profile/user.png"));
//		addLabel("USERNAME",Main.username);
//		addLabel("BETFAIR POINTS",af.getBetfairPoints()+"");
//		addLabel("CURRENT BALANCE",af.getBalance()+"");
//		addLabel("AVAILABLE BALANCE",af.getAvailable()+"");
//		addLabel("CREDIT LIMIT",af.getCreditLimit()+"");
//		addLabel("EXPOSURE",af.getExposure()+"");
//		addLabel("EXPOSURE LIMIT",af.getExposureLimit()+"");
		addLabel("USERNAME","");
		addLabel("BETFAIR POINTS","");
		addLabel("CURRENT BALANCE","");
		addLabel("AVAILABLE BALANCE","");
		addLabel("CREDIT LIMIT","");
		addLabel("EXPOSURE","");
		addLabel("EXPOSURE LIMIT","");
	}
	
	/** Method for label addition */
	private void addLabel(String key,String value) {
		Label nkey = new Label(shell, SWT.NONE);
		nkey.setText(key);
		nkey.setLayoutData(gd);
		Label nvalue = new Label(shell, SWT.NONE);
		nvalue.setText(value);
		nvalue.setLayoutData(gd);
		nvalue.setAlignment(SWT.CENTER);
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
}