package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.ic.tennistrader.Main;
import org.ic.tennistrader.exceptions.LoginFailedException;
import org.ic.tennistrader.model.connection.BetfairConnectionHandler;
import org.pushingpixels.trident.Timeline;

public class LoginShell {

	List<Listener> loginSucces = new ArrayList<Listener>();

	private Shell loginShell;

	private Label result;

	private static Logger log = Logger.getLogger(LoginShell.class);

	private static final String TITLE = "Tennis Trader Login";

	public static final String SUCCESS = "Login successful! Please wait...";

	public static final String FAIL = "Login failed! Please try again...";

	private ProgressBar bar;

	public Shell show() {
		return loginShell;
	}

	public LoginShell(final Display display) {
		this.loginShell = new Shell(display, SWT.NO_TRIM);// SWT.TRANSPARENCY_ALPHA);
		loginShell.setSize(600, 350);
		loginShell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		final Image loginImg = new Image(loginShell.getDisplay(),
				"images/login/login.png");
		final Image cancel = new Image(loginShell.getDisplay(),
				"images/login/cancel.png");
		final Image accept = new Image(loginShell.getDisplay(),
				"images/login/accept.png");

		loginShell.setBackgroundImage(loginImg);

		// Region region = new Region();
		// region.add(circle(67, 67, 67));
		// define the shape of the shell using setRegion
		// loginShell.setRegion(region);

		Listener l = new Listener() {
			Point origin;

			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.MouseDown:
					origin = new Point(e.x, e.y);
					break;
				case SWT.MouseUp:
					origin = null;
					break;
				case SWT.MouseMove:
					if (origin != null) {
						Point p = display.map(loginShell, null, e.x, e.y);
						loginShell.setLocation(p.x - origin.x, p.y - origin.y);
					}
					break;
				}
			}
		};
		loginShell.addListener(SWT.MouseDown, l);
		loginShell.addListener(SWT.MouseUp, l);
		loginShell.addListener(SWT.MouseMove, l);

		Rectangle rect = loginShell.getClientArea();

		loginShell.setText(TITLE);

		final Text username = new Text(loginShell, SWT.NONE);
		username.setText("username");
		username.setBounds(180, 165, 300, 30);

		final Text password = new Text(loginShell, SWT.PASSWORD);
		password.setText("password");
		password.setBounds(180, 210, 300, 30);

		Button login = makeLoginButton(display);
		login.setImage(accept);
		login.pack();
		login.setLocation(300, 250);

		Button cancelButton = makeCancelButton(display);
		cancelButton.setImage(cancel);
		cancelButton.pack();
		cancelButton.setLocation(400, 250);
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				loginShell.dispose();
			}
		});

		login.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String user = username.getText();
				if (checkLogin(user, password.getText())) {
					Main.username = user;
					updateResult(SUCCESS);
					handleLoginSuccess();
				} else
					updateResult(FAIL);
			}
		});

		// Center the login screen
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		loginShell.setLocation(x, y);

		this.bar = new ProgressBar(loginShell, SWT.SMOOTH);
		this.bar.setBounds(50, 305, 500, 20);
		bar.setToolTipText("Test");
		bar.setVisible(false);
		loginShell.open();
	}

	private Button makeCancelButton(Display display) {

		Button cancel = new Button(loginShell, SWT.PUSH);
		cancel.setText("Cancel");

		Color init = new org.eclipse.swt.graphics.Color(display, 3, 3, 3);
		Color last = new org.eclipse.swt.graphics.Color(display, 105, 105, 105);

		cancel.setForeground(init);

		final Timeline rolloverTimeline = new Timeline(cancel);
		rolloverTimeline.addPropertyToInterpolate("foreground", init, last);
		rolloverTimeline.setDuration(100);

		cancel.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseEnter(MouseEvent arg0) {
				rolloverTimeline.play();

			}

			@Override
			public void mouseExit(MouseEvent arg0) {
				rolloverTimeline.playReverse();

			}

			@Override
			public void mouseHover(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		return cancel;

	}

	private Button makeLoginButton(final Display display) {
		Button login = new Button(loginShell, SWT.PUSH);
		GridData buttonData = new GridData();
		login.setText("Login");
		login.setLayoutData(buttonData);

		Color init = new org.eclipse.swt.graphics.Color(display, 3, 3, 3);
		Color last = new org.eclipse.swt.graphics.Color(display, 105, 105, 105);

		login.setForeground(init);

		final Timeline rolloverTimeline = new Timeline(login);
		rolloverTimeline.addPropertyToInterpolate("foreground", init, last);
		rolloverTimeline.setDuration(100);

		login.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseEnter(MouseEvent arg0) {
				rolloverTimeline.play();

			}

			@Override
			public void mouseExit(MouseEvent arg0) {
				rolloverTimeline.playReverse();

			}

			@Override
			public void mouseHover(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		return login;
	}

	public void run(final Display display) {
		while (!loginShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	private static boolean checkLogin(String username, String password) {
		// Perform the login
		try {
			BetfairConnectionHandler.login(username, password);
		} catch (LoginFailedException e) {
			log.info(e.getMessage());
			return false;
		}

		log.info("Login succeeded with token - "
				+ BetfairConnectionHandler.getApiContext().getToken());

		return true;
	}

	private void updateResult(String message) {
		this.loginShell.update();
	}

	public void dispose() {
		loginShell.dispose();
	}

	public void addLoginSuccessListener(Listener listener) {
		loginSucces.add(listener);
	}

	public void handleLoginSuccess() {
		for (Listener l : loginSucces)
			l.handleEvent(new Event());
	}

	public void setText(String text) {
		//TODO: update text on progress bar
	}

	public void updateProgressBar(int amount) {
		if (!bar.getVisible())
			bar.setVisible(true);
		bar.setSelection(bar.getSelection() + amount);
	}

	public void finishProgressBar() {
		bar.setSelection(bar.getMaximum());
	}

	static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}

}
