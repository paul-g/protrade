package src;

import java.util.List;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import src.demo.handler.GlobalAPI;
import src.demo.util.APIContext;
import src.domain.Match;
import src.domain.Tournament;
import src.exceptions.LoginFailedException;
import src.service.BetfairConnectionHandler;
import src.ui.DisplayPanel;
import src.ui.NavigationPanel;

public class Main {

	private static final String TITLE = "Tennis Trader";

	public static void main(String[] args) {
		final Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText(TITLE);

		NavigationPanel np = new NavigationPanel(shell);
		DisplayPanel dp = new DisplayPanel(shell);

		np.addListener(dp);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

}
