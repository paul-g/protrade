package org.ic.tennistrader.domain.match;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.ic.tennistrader.domain.MOddsMarketData;
import org.ic.tennistrader.exceptions.MatchNotFinishedException;
import org.ic.tennistrader.service.FracsoftReader;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.ic.tennistrader.utils.Pair;

public class HistoricalMatch extends Match {
    private String name;
    public HistoricalMatch(Player player1, Player player2){
        this.score = new Score(3);
        this.player1 = player1;
        this.player2 = player2;
    }
    
    public HistoricalMatch(String filename){
        this.score = new Score();
        this.marketDatas = new ArrayList<MOddsMarketData>();

        Pair<String, String> p = FracsoftReader.getPlayerNames(filename);
        
        System.out.println(p.first());
        System.out.println(p.second());
        
        String names [] = p.first().split(" ");
        
        this.player1.setFirstname(names[0]);
        this.player1.setLastname(names[1]);
        
        names = p.second().split(" ");
        this.player2.setFirstname(names[0]);
        this.player2.setLastname(names[1]);
        
        System.out.println(player2.getFirstname());
        System.out.println(player2.getLastname());
        
        System.out.println(player1.getFirstname());
        System.out.println(player1.getLastname());
        
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
	public PlayerEnum getWinner() throws MatchNotFinishedException {
		return this.score.getWinner();
	}

    @Override
    public void addMarketData(MOddsMarketData data) {
        this.marketDatas.add(data);
    }

    @Override
    public boolean isFromFile() {
        return true;
    }
}
