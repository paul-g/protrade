package org.ic.tennistrader.model.betting;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ic.tennistrader.domain.match.HistoricalMatch;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.domain.match.Player;
import org.ic.tennistrader.domain.match.PlayerEnum;
import org.ic.tennistrader.exceptions.OddsButtonNotFoundException;
import org.ic.tennistrader.ui.updatable.UpdatableMarketDataGrid;
import org.ic.tennistrader.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import org.ic.tennistrader.model.betting.BetController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BetControllerTest {
    private BetController betController;
    private Display display;
    private Shell comp;
    private Match match;
    private UpdatableMarketDataGrid dataGrid;
    
    @Before
    public void setUp() {
        display = new Display();
        comp = new Shell(display);
        Player firstPlayer = new Player("Rafael", "Nadal");
        Player secondPlayer = new Player("Roger", "Federer");
        match = new HistoricalMatch(firstPlayer, secondPlayer);
        dataGrid = new UpdatableMarketDataGrid(comp,
                SWT.None, match);
        betController = new BetController(Arrays.asList(dataGrid
                .getP1BackButtons()),
                Arrays.asList(dataGrid.getP1LayButtons()), Arrays
                        .asList(dataGrid.getP2BackButtons()), Arrays
                        .asList(dataGrid.getP2LayButtons()), match);
    }
    
    @After
    public void teardown() {
        while (display.readAndDispatch()){
            // handle remaining work
        }
        display.dispose();
    }
    
    @Test
    public void testMatch() {
        assertEquals(match, betController.getMatch());
    }
        
    @Test
    public void testBetTypes() {
        try {
            assertEquals(BetTypeEnum.B, betController.getBetType(dataGrid
                    .getP1BackButtons()[0]));
            assertEquals(BetTypeEnum.B, betController.getBetType(dataGrid
                    .getP1BackButtons()[1]));
            assertEquals(BetTypeEnum.B, betController.getBetType(dataGrid
                    .getP1BackButtons()[2]));
            assertEquals(BetTypeEnum.B, betController.getBetType(dataGrid
                    .getP2BackButtons()[0]));
            assertEquals(BetTypeEnum.B, betController.getBetType(dataGrid
                    .getP2BackButtons()[1]));
            assertEquals(BetTypeEnum.B, betController.getBetType(dataGrid
                    .getP2BackButtons()[2]));
            assertEquals(BetTypeEnum.L, betController.getBetType(dataGrid
                    .getP1LayButtons()[0]));
            assertEquals(BetTypeEnum.L, betController.getBetType(dataGrid
                    .getP1LayButtons()[1]));
            assertEquals(BetTypeEnum.L, betController.getBetType(dataGrid
                    .getP1LayButtons()[2]));
            assertEquals(BetTypeEnum.L, betController.getBetType(dataGrid
                    .getP2LayButtons()[0]));
            assertEquals(BetTypeEnum.L, betController.getBetType(dataGrid
                    .getP2LayButtons()[1]));
            assertEquals(BetTypeEnum.L, betController.getBetType(dataGrid
                    .getP2LayButtons()[2]));
        } catch (OddsButtonNotFoundException e) {
            fail("Odds Button not found in Bet controller");
        }
    }
    
    @Test
    public void testBetPlayers() {
        try {
            assertEquals(PlayerEnum.PLAYER1, betController.getBetPlayer(dataGrid
                    .getP1BackButtons()[0]));
            assertEquals(PlayerEnum.PLAYER1, betController.getBetPlayer(dataGrid
                    .getP1BackButtons()[1]));
            assertEquals(PlayerEnum.PLAYER1, betController.getBetPlayer(dataGrid
                    .getP1BackButtons()[2]));
            assertEquals(PlayerEnum.PLAYER1, betController.getBetPlayer(dataGrid
                    .getP1LayButtons()[0]));
            assertEquals(PlayerEnum.PLAYER1, betController.getBetPlayer(dataGrid
                    .getP1LayButtons()[1]));
            assertEquals(PlayerEnum.PLAYER1, betController.getBetPlayer(dataGrid
                    .getP1LayButtons()[2]));
            assertEquals(PlayerEnum.PLAYER2, betController.getBetPlayer(dataGrid
                    .getP2BackButtons()[0]));
            assertEquals(PlayerEnum.PLAYER2, betController.getBetPlayer(dataGrid
                    .getP2BackButtons()[1]));
            assertEquals(PlayerEnum.PLAYER2, betController.getBetPlayer(dataGrid
                    .getP2BackButtons()[2]));
            assertEquals(PlayerEnum.PLAYER2, betController.getBetPlayer(dataGrid
                    .getP2LayButtons()[0]));
            assertEquals(PlayerEnum.PLAYER2, betController.getBetPlayer(dataGrid
                    .getP2LayButtons()[1]));
            assertEquals(PlayerEnum.PLAYER2, betController.getBetPlayer(dataGrid
                    .getP2LayButtons()[2]));
        } catch (OddsButtonNotFoundException e) {
            fail("Odds Button not found in Bet controller");
        }
    }
}
