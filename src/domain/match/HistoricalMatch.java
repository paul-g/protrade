package src.domain.match;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.swt.widgets.Composite;

import src.service.FracsoftReader;
import src.service.LiveDataFetcher;
import src.ui.updatable.UpdatableWidget;
import src.utils.Pair;

public class HistoricalMatch implements Match {
    private String name;
    private String filename;
    private String player1;
    private String player2;
    
    public HistoricalMatch(String filename){
        Pair<String, String> p = FracsoftReader.getPlayerNames(filename);
        this.player1 = p.getI();
        this.player2 = p.getJ();
        
        this.filename = filename;
        this.name = getMatchName();
    }

    @Override
    public boolean isInPlay() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void registerForUpdate(UpdatableWidget widget, Composite composite) {
        LiveDataFetcher.registerFromFile(widget, this, filename, composite);
    }
    
    // needs to be in a different class!!!
	public String getMatchName() {
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Match name";
		}
		String mName = scanner.nextLine();
		scanner.close();
		return mName;
	}
	
	public String toString() {
		return getName();
	}

    @Override
    public String getPlayer1() {
        return player1;
    }

    @Override
    public String getPlayer2() {
        return player2;
    }
}
