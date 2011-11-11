package org.ic.tennistrader.utils;

import org.eclipse.swt.widgets.TreeItem;

import org.ic.tennistrader.domain.match.RealMatch;
import org.ic.tennistrader.model.connection.BetfairExchangeHandler;
import org.ic.tennistrader.ui.NavigationPanel;

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
