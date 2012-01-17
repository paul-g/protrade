package org.ic.protrade.ui.updatable;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.ic.protrade.data.match.PlayerEnum;
import org.ic.protrade.exceptions.OddsButtonNotFoundException;
import org.ic.protrade.model.betting.BetManager;
import org.ic.protrade.ui.betting.BetPlacingShell;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;

public class OddsButton extends Composite {

	private static final Logger log = Logger.getLogger(OddsButton.class);

	private String CURRENCY = "£";

	private final MarketDataGrid dataGrid;
	
	private final Label odds;
	private final Label amount;
	private final Display display;

	private Image initialImage;
	private Image hoverImage;
	private Image clickImage;

	public OddsButton(Composite parent, Color color, Font oddsFont,
			MarketDataGrid dataGrid) {
		super(parent, SWT.BORDER);
		this.dataGrid = dataGrid;
		this.display = parent.getDisplay();
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		setLayoutData(gd);
		setLayout(new RowLayout(SWT.VERTICAL));

		odds = new Label(this, SWT.NONE);
		odds.setFont(oddsFont);
		amount = new Label(this, SWT.NONE);
		amount.setFont(oddsFont);

		addClickListener();

		addMouseTrackListener(new MouseTrackAdapter() {
			
			@Override
			public void mouseEnter(MouseEvent arg0) {
				setBackgroundImage(hoverImage);
			}

			@Override
			public void mouseExit(MouseEvent e) {
				setBackgroundImage(initialImage);
			}
		});

		Menu menu = makeMenu(this);
		setMenu(menu);
		odds.setMenu(menu);
		amount.setMenu(menu);

	}

	private Menu makeMenu(Composite comp) {
		Menu menu = new Menu(comp.getShell(), SWT.POP_UP);
		for (int i = 10; i < 50; i += 10)
			makeItem(menu, i);
		return menu;
	}

	private void makeItem(Menu menu, final double amount) {
		final MenuItem a = new MenuItem(menu, SWT.PUSH);
		a.setText(amount + "£");

		a.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				try {
					BetTypeEnum betType = dataGrid.getBetController()
							.getBetType(OddsButton.this);
					PlayerEnum betPlayer = dataGrid.getBetController()
							.getBetPlayer(OddsButton.this);
					double initOdds = Double.parseDouble(OddsButton.this
							.getOdds().getText());
					BetManager.placeBet(dataGrid.getBetController().getMatch(),
							betPlayer, betType, initOdds, amount);
					dataGrid.updateProfits();
				} catch (OddsButtonNotFoundException odbnfe) {

				}
			}
		});
	}

	void addClickListener() {
		final OddsButton button = this;
		Listener l = new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (dataGrid.getMatch() != null) {
					try {
						BetTypeEnum betType = dataGrid.getBetController()
								.getBetType(OddsButton.this);
						PlayerEnum betPlayer = dataGrid.getBetController()
								.getBetPlayer(OddsButton.this);
						String betDetails = dataGrid.getBetController()
								.getBettingDetails(OddsButton.this);
						double initOdds = Double.parseDouble(OddsButton.this
								.getOdds().getText());
						BetPlacingShell betPlacingShell = new BetPlacingShell(button,
								 dataGrid.getBetController()
										.getMatch(), betPlayer, betType,
								initOdds, betDetails);
						betPlacingShell.open();
						/*
						betPlacingShell
								.addDisposeListener(new DisposeListener() {
									@Override
									public void widgetDisposed(DisposeEvent arg0) {
										dataGrid.updateProfits();
									}
								});
								*/
						//Rectangle rect = getClientArea();
		//				betPlacingShell.setLocation(rect.x, rect.y);
					} catch (OddsButtonNotFoundException obnfe) {

					}
					setBackgroundImage(clickImage);
					display.timerExec(100, new Runnable() {
						@Override
						public void run() {
							setBackgroundImage(initialImage);
						}
					});
				}
			}
		};

		addListener(SWT.MouseUp, l);
		odds.addListener(SWT.MouseUp, l);
		amount.addListener(SWT.MouseUp, l);
	}

	void setOdds(String odds) {
		this.odds.setText(odds);
	}

	public void setCurrency(String c) {
		this.CURRENCY = c;
	}

	void setAmount(String amount) {
		this.amount.setText(CURRENCY + amount);
	}

	public Label getOdds() {
		return odds;
	}

	public double getAmount() {
		String amount = this.amount.getText().substring(1);
		return Double.parseDouble(amount);
	}

	public void setInitialBackgroundImage(Image image) {
		setBackgroundImage(image);
		this.initialImage = image;
	}

	public void setHighlightImage(Image highlightImage) {
		this.hoverImage = highlightImage;
	}

	public void setClickImage(Image clickImage) {
		this.clickImage = clickImage;
	}

	public MarketDataGrid getDataGrid() {
		return dataGrid;
	}

}