package org.ic.tennistrader.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
        this.loginShell = new Shell(display, SWT.MAX/* SWT.NO_TRIM|SWT.ON_TOP */);// SWT.TRANSPARENCY_ALPHA);
        loginShell.setSize(400, 160);
        loginShell.setBackgroundMode(SWT.INHERIT_DEFAULT);

        final Image logoUp = new Image(loginShell.getDisplay(),
                "images/sports_tennis.jpg");
        loginShell.setBackgroundImage(logoUp);

        Rectangle rect = loginShell.getClientArea();

        loginShell.setText(TITLE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        gridLayout.marginTop = 0;

        loginShell.setLayout(gridLayout);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData.horizontalSpan = 1;

        GridData gridData2 = new GridData(150, 16);
        gridData2.horizontalAlignment = GridData.FILL_HORIZONTAL;
        gridData2.horizontalSpan = 4;

        Label loginLabel = new Label(loginShell, SWT.NONE);
        loginLabel.setText("Username: ");
        loginLabel.setLayoutData(gridData);

        final Text username = new Text(loginShell, SWT.NONE);
        username.setLayoutData(gridData2);
        username.setText("username");

        Label passLabel = new Label(loginShell, SWT.NONE);
        passLabel.setLayoutData(gridData);
        passLabel.setText("Password: ");

        final Text password = new Text(loginShell, SWT.PASSWORD);
        password.setLayoutData(gridData2);
        password.setText("password");

        // just for alignment
        @SuppressWarnings("unused")
        Label blankLabel = new Label(loginShell, SWT.NONE);

        @SuppressWarnings("unused")
        Label blankLabel2 = new Label(loginShell, SWT.NONE);

        Button login = makeLoginButton(display);

        // test account button
        makeTestAccount(display);

        GridData resultData = new GridData();
        resultData.horizontalSpan = 5;
        this.result = new Label(loginShell, SWT.NONE);
        result.setLayoutData(resultData);
        result.setText(FAIL.length() > SUCCESS.length() ? FAIL : SUCCESS);
        result.setVisible(false);

        login.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                String user = username.getText();
                if (checkLogin(user, password.getText())) {
                    Main.USERNAME = user;
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

        GridData barData = new GridData();
        barData.grabExcessHorizontalSpace = true;
        barData.horizontalSpan = 5;
        barData.horizontalAlignment = SWT.FILL;
        bar.setLayoutData(barData);
        bar.setVisible(false);
        loginShell.open();

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

    private void makeTestAccount(final Display display) {
        Button testAccount = new Button(loginShell, SWT.NONE);
        testAccount.setText("Test");
        testAccount.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                String username = Main.TEST_USERNAME;
                String password = Main.TEST_PASSWORD;
                log.info("username " + username);
                Main.USERNAME = Main.TEST_USERNAME;
                Main.PASSWORD = Main.TEST_PASSWORD;
                if (checkLogin(username, password)) {
                    updateResult(SUCCESS);
                    handleLoginSuccess();
                } else
                    updateResult(FAIL);
            }
        });
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
        result.setText(message);
        result.setVisible(true);
        result.update();
        this.loginShell.update();
        if (message.equals(SUCCESS))
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

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
        result.setText(text + "...");
    }

    public void updateProgressBar(int amount) {
        if (!bar.getVisible())
            bar.setVisible(true);
        bar.setSelection(bar.getSelection() + amount);
    }

    public void finishProgressBar() {
        bar.setSelection(bar.getMaximum());
    }
}
