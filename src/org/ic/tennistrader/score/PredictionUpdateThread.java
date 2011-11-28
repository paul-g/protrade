package org.ic.tennistrader.score;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.ic.tennistrader.domain.match.Match;
import org.ic.tennistrader.service.threads.MatchUpdaterThread;

public class PredictionUpdateThread extends MatchUpdaterThread {    
	private ProbabilityPanel probabilityPanel;
	private Match match;
	
	public PredictionUpdateThread(Match match, ProbabilityPanel probabilityPanel)
	{
		this.probabilityPanel = probabilityPanel;
		this.match = match;
	}
	
	@Override
	protected void runBody() {
		//updateTable(predict);
	}
	
	public void updateTable( PredictionCalculator predict ) {
		Table table = this.probabilityPanel.table;
		// Filling the probabilities table with data
        table.setRedraw(false);       
       
		TableItem item = table.getItem(0);
        int c = 0;
        item.setText(c++, match.getPlayerOne().getLastname());
        item.setText(c++, Double.toString(predict.result[0]));
        item.setText(c++, Double.toString(predict.result[2]));
        item.setText(c++, "57%");
        item.setText(c++, "63%");

        TableItem item2 = table.getItem(1);
        c = 0;
        item2.setText(c++, match.getPlayerTwo().getLastname());
        item2.setText(c++, Double.toString(predict.result[1]));
        item2.setText(c++, Double.toString(predict.result[3]));
        item2.setText(c++, "43%");
        item2.setText(c++, "37%");
        
        table.setRedraw(true);

	}

}
