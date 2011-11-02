package src.domain.match;

public class HistoricalMatch implements Match {
    
    public HistoricalMatch(){
    }

    @Override
    public boolean isInPlay() {
        return false;
    }

    @Override
    public String getName() {
        return "History";
    }
}
