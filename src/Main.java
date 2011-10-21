package src;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import src.domain.Match;
import src.domain.Tournament;
import src.exceptions.LoginFailedException;
import src.service.BetfairConnectionHandler;
import src.ui.DisplayPanel;
import src.ui.NavigationPanel;

public class Main {

	private static final String TITLE = "Tennis Trader";

	private static final Display display = new Display();

	public static void main(String[] args) {

		createLoginShell();

	}

	private static void createLoginShell() {

		Shell loginShell = new Shell(display, SWT.MAX);

		/*
		 * Monitor primary = display.getPrimaryMonitor(); Rectangle bounds =
		 * primary.getBounds(); Rectangle rect = loginShell.getBounds();
		 * 
		 * int x = bounds.x + (bounds.width - rect.width) / 2; int y = bounds.y
		 * + (bounds.height - rect.height) / 2;
		 * 
		 * loginShell.setLocation(x,y);
		 */

		loginShell.setText("Login to tennis trader");

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.numColumns = 3;

		loginShell.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;

		Label loginLabel = new Label(loginShell, SWT.NONE);
		loginLabel.setText("Username: ");

		Text username = new Text(loginShell, SWT.NONE);
		username.setLayoutData(gridData);

		Label passLabel = new Label(loginShell, SWT.NONE);
		passLabel.setText("Password: ");

		Text password = new Text(loginShell, SWT.PASSWORD);
		password.setLayoutData(gridData);

		// just for alignment
		@SuppressWarnings("unused")
		Label blankLabel = new Label(loginShell, SWT.NONE);

		Button button = new Button(loginShell, SWT.PUSH);
		GridData buttonData = new GridData();
		button.setText("Login");
		button.setLayoutData(buttonData);

		Button reset = new Button(loginShell, SWT.None);
		reset.setText("Reset");

		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if (checkLogin())
					launchApp(display);
			}
		});

		loginShell.pack();

		loginShell.open();

		while (!loginShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}

	private static void launchApp(Display display) {
		Shell shell = new Shell(display);
		shell.setMaximized(true);
		shell.setLayout(new FillLayout());
		shell.setText(TITLE);

		NavigationPanel np = new NavigationPanel(shell);
		DisplayPanel dp = new DisplayPanel(shell);

		np.addListener(dp);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	private static boolean checkLogin() {
		// TODO: impement logic for checking login here

		// check a trivial login using Betfair API
		String username = "corina409";
		String password = "ureanidiot1";

		// Perform the login
		try {
			BetfairConnectionHandler.login(username, password);
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Login succeeded with token - "
				+ BetfairConnectionHandler.getApiContext().getToken());

		// get list of tournaments (events and markets of tennis type event id)
		/*
		 * List<Tournament> tournaments =
		 * BetfairConnectionHandler.getTournamentsData();
		 * System.out.println("List of events under \' tennis event type id \': "
		 * ); for (Tournament t : tournaments) {
		 * System.out.println(t.toString()); for (Match m : t.getMatches()) {
		 * System.out.println("\t " + m.toString()); } }
		 */

		// Perform logout
		/*
		 * try { BetfairConnectionHandler.logout(); } catch (Exception e){
		 * System.out.println(e.getMessage()); System.exit(-1); }
		 * System.out.println("Logout succesful");
		 */

		return true;
	}

}
