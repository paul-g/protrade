package org.ic.tennistrader.service.threads.file_readers;

import static org.ic.tennistrader.utils.Pair.pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.ic.tennistrader.exceptions.EndOfFracsoftFileException;
import org.ic.tennistrader.service.threads.MatchUpdaterThread;
import org.ic.tennistrader.utils.Pair;

public abstract class FracsoftReader<K> extends MatchUpdaterThread {
	protected Iterator<K> pointer = null;
	protected int updatesPerSecond = 1;
	protected int inPlayPointer = -1;
	protected List<K> matchDataList = new ArrayList<K>();

	public void setUpdatesPerSecond(int updates) {
		this.updatesPerSecond = updates;
	}

	protected static void skipHeader(Scanner scanner, int lines) {
		for (int i = 0; i < lines; i++) {
			scanner.nextLine();
		}
	}

	protected void trim(String[] lines1) {
		for (int kk = 0; kk < lines1.length; kk++) {
			lines1[kk] = lines1[kk].trim();
		}
	}

	protected ArrayList<Pair<Double, Double>> getOdds(String[] lines1,
			int offset) {
		ArrayList<Pair<Double, Double>> pl1Backs = new ArrayList<Pair<Double, Double>>();
		for (int i = 0; i < 3; i++) {
			double odds = Double.parseDouble(lines1[offset + i * 2]);
			double amount = Double.parseDouble(lines1[offset + i * 2 + 1]);
			Pair<Double, Double> p = pair(odds, amount);
			pl1Backs.add(p);
		}
		return pl1Backs;
	}

	protected K getMarketData() throws EndOfFracsoftFileException {
		if (pointer.hasNext())
			return pointer.next();
		throw new EndOfFracsoftFileException();
	}

	@Override
	public void runBody() {
		if (pointer != null)
			runFileReader();
	}

	protected abstract void runFileReader();
}
