package src.utils;

import org.eclipse.swt.widgets.TreeItem;

import src.domain.MOddsMarketData;
import src.service.BetfairExchangeHandler;
import src.ui.NavigationPanel;

public class MatchUtils {
    
    public static boolean isMatch(String name){
        return name.contains(" v ");
    }
    
    public static boolean inPlay(TreeItem ti){
        MOddsMarketData data = BetfairExchangeHandler.getMarketOdds(NavigationPanel.getMatch(ti).getEventBetfair());
        return data.getDelay() > 0;
    }
}
