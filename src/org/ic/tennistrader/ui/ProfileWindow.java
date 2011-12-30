package org.ic.tennistrader.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
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
	
	public static void main (String[] args) {
		Display d = new Display();
		ProfileWindow p = new ProfileWindow(d, null);
		p.stay();
	}
	
	/** Constructor */
	public ProfileWindow(Display display, ProfileData profdata) {
		this.display = display;
		shell = new Shell(display,SWT.SHELL_TRIM);
		profileData = profdata;
		shell.setLayout(new FillLayout());
		shell.setText("Profile");
		shell.setSize(200,200);
		shell.setLocation(1100,80);
		Label profData = new Label(shell,SWT.BORDER);
		profData.setText("Hello!");
		textProfile();
		shell.open();
	}
	
	/** Text representation of the profile */
	private String textProfile() {
		String res ="";
		try {
			AccountFunds af = profileData.getUkAccountFunds();
			res =
				"Username : " + Main.username +
				"\nBetfair points : " + af.getBetfairPoints() +
				"\nCurrent balance : " + af.getBalance() +
				"\nAvailable balance : " + af.getAvailable() +
				"\nCredit limit : " + af.getCreditLimit() +
				"\nExposure : " + af.getExposure() +
				"\nExposure limit : " + af.getExposureLimit();
		} catch (Exception e) {
			e.getMessage();
		}
		return res;
	}
	
	public boolean isDisposed() {
		return shell.isDisposed();
	}
	
	public void forceActive() {
		shell.forceActive();
	}
	
	public void stay() {
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch() ) display.sleep();
		}
		display.dispose();
	}
}
