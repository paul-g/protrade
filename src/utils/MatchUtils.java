package src.utils;

import org.eclipse.swt.widgets.TreeItem;

import src.domain.match.RealMatch;
import src.model.connection.BetfairExchangeHandler;
import src.ui.NavigationPanel;

public class MatchUtils {
    
    public static boolean isMatch(String name){
        return name.contains(" v ");
    }
    
    public static boolean inPlay(TreeItem ti){
    	RealMatch match = NavigationPanel.getMatch(ti);
    	if (match.getRecentMarketData() == null)
    		match.addMarketData(BetfairExchangeHandler.getMarketOdds(match.getEventBetfair()));
        return match.getRecentMarketData().getDelay() > 0;
    }
}
