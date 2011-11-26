package org.ic.tennistrader.domain.match;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.ic.tennistrader.exceptions.MatchNotFinishedException;
import org.ic.tennistrader.service.FracsoftReader;
import org.ic.tennistrader.service.LiveDataFetcher;
import org.ic.tennistrader.ui.updatable.UpdatableWidget;
import org.ic.tennistrader.utils.Pair;

public class HistoricalMatch implements Match {
    private String name;
    private String filename;
    private Score score;
    private Player player1 = new Player();
    private Player player2 = new Player();
    
    public HistoricalMatch(Player player1, Player player2){
        this.score = new Score(3);
        this.player1 = player1;
        this.player2 = player2;
    }
    
    public HistoricalMatch(String filename){
        this.score = new Score();

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

    @Override
    public void registerForUpdate(UpdatableWidget widget) {
        LiveDataFetcher.registerFromFile(widget, this, filename);
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
    public Player getPlayerOne() {
        return player1;
    }

    @Override
    public Player getPlayerTwo() {
        return player2;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void setScore(Score score) {
        this.score = score;
    }

	@Override
	public PlayerEnum getWinner() throws MatchNotFinishedException {
		return this.score.getWinner();
	}
}
