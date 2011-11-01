package src.utils;

import org.eclipse.swt.widgets.TreeItem;

import src.domain.MOddsMarketData;
import src.domain.Match;
import src.model.connection.BetfairExchangeHandler;
import src.ui.NavigationPanel;

public class MatchUtils {
    
    public static boolean isMatch(String name){
        return name.contains(" v ");
    }
    
    public static boolean inPlay(TreeItem ti){
    	Match match = NavigationPanel.getMatch(ti);
    	if (match.getMarketData() == null)
    		match.setMarketData(BetfairExchangeHandler.getMarketOdds(match.getEventBetfair()));
        return match.getMarketData().getDelay() > 0;
    }
}
