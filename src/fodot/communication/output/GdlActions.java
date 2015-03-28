package fodot.communication.output;

import java.util.ArrayList;
import java.util.List;

import fodot.util.CollectionPrinter;

public class GdlActions {
	private final List<GdlAction> actions;
	private final int score;
	private final int maximumScore;
	
	public GdlActions(List<GdlAction> actions, int score, int maximumScore) {
		this.actions = new ArrayList<>(actions);
		this.score = score;
		this.maximumScore = maximumScore;
	}
	
	public List<GdlAction> getActions() {
		return new ArrayList<>(actions);
	}
	
	public int getScore() {
		return score;
	}
	
	public int getMaximumScore() {
		return maximumScore;
	}
	
	@Override
	public String toString() {
		return "SCORE/MAXSCORE: " + score + "/" + getMaximumScore()
				+ "\n"+CollectionPrinter.printStringList("", "", "\n", getActions());
	}
	
}
