package src.demo;

import java.util.ArrayList;

import src.demo.handler.ExchangeAPI;
import src.demo.handler.GlobalAPI;
import src.demo.handler.ExchangeAPI.Exchange;
import src.demo.util.APIContext;
import src.demo.util.Display;

import src.generated.exchange.BFExchangeServiceStub.BetCategoryTypeEnum;
import src.generated.exchange.BFExchangeServiceStub.BetPersistenceTypeEnum;
import src.generated.exchange.BFExchangeServiceStub.BetTypeEnum;
import src.generated.exchange.BFExchangeServiceStub.CancelBets;
import src.generated.exchange.BFExchangeServiceStub.Market;
import src.generated.exchange.BFExchangeServiceStub.PlaceBets;
import src.generated.exchange.BFExchangeServiceStub.PlaceBetsResult;
import src.generated.exchange.BFExchangeServiceStub.Runner;
//import src.generated.global.BFGlobalServiceStub.BFEvent;
//import src.generated.global.BFGlobalServiceStub.EventType;
//import src.generated.global.BFGlobalServiceStub.GetEventsResp;
//import src.generated.global.BFGlobalServiceStub.MarketSummary;

public class BetPlacer extends Thread{
    
    private static APIContext apiContext = new APIContext();
    private static String username="testaccount1";
    private static String password="password03";
    
    private static Market selectedMarket;
    
    private static int marketID = 30105577;
    private static int selectionID = 2078969;
    private Exchange exchange = Exchange.UK;
    
    private ArrayList<CancelBets> cancelBets = new ArrayList<CancelBets>();
    
    public static void main(String[] args){
        // Perform the login before anything else.
        try
        {
            GlobalAPI.login(apiContext, username, password);
            BetPlacer betPlacer = new BetPlacer();
            betPlacer.start();
        }
        catch (Exception e)
        {
            // If we can't log in for any reason, just exit.
            Display.showException("*** Failed to log in", e);
            System.exit(1);
        }
        
    }
    
    public BetPlacer(){
        try {
            selectedMarket = ExchangeAPI.getMarket(exchange, apiContext, marketID);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void run(){
        try{
            int i=0;
            while (true){
                try{
                    this.placeBet();
                }
                finally{
                }
                sleep(1000);
                i++;
                if (i%30==0){
                    try{
                        cancelAll();
                    }
                    finally{                        
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // Place a bet on the specified market.
    public void placeBet() throws Exception {
        
        @SuppressWarnings("unused")
        Runner[] runners = selectedMarket.getRunners().getRunner();
        
        // Set up the individual bet to be placed
        PlaceBets bet = new PlaceBets();
        bet.setMarketId(selectedMarket.getMarketId());
        bet.setSelectionId(selectionID);
        bet.setBetCategoryType(BetCategoryTypeEnum.E);
        bet.setBetPersistenceType(BetPersistenceTypeEnum.NONE);
        bet.setBetType(BetTypeEnum.L);
        bet.setPrice(2);
        bet.setSize(2);
        
        PlaceBetsResult betResult = ExchangeAPI.placeBets(exchange, apiContext, new PlaceBets[] {bet})[0];
        
        
        if (betResult.getSuccess()) {
            Display.println("Bet "+betResult.getBetId()+" placed. "+betResult.getSizeMatched() +" matched @ "+betResult.getAveragePriceMatched());
            CancelBets cancelBet = new CancelBets();
            cancelBet.setBetId(betResult.getBetId());
            cancelBets.add(cancelBet);
        } else {
            Display.println("Failed to place bet: Problem was: "+betResult.getResultCode());
        }
    }
    
    public void cancelAll() throws Exception {
        CancelBets[] allCancelBets = cancelBets.toArray(new CancelBets[cancelBets.size()]);
        ExchangeAPI.cancelBets(exchange, apiContext, allCancelBets);
        cancelBets.clear();
    }


}
