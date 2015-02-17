package fodot.communication.gdloutput;

import java.util.List;

import fodot.communication.output.GdlAction;
import fodot.communication.output.GdlActions;

public class GdlActionPrinter implements IActionOutputter {

	private List<GdlAction> actions;
	private int score;
	private int maxScore;
	
	public GdlActionPrinter() {
		super();
	}

	private void setActions(List<GdlAction> actions) {
		this.actions = actions;
	}

	private void setScore(int score) {
		this.score = score;
	}
	
	private void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	
	private int getMaxScore() {
		return maxScore;
	}
	
	private String generateTextOutput() {
		StringBuilder builder = new StringBuilder();
		builder.append("SCORE/MAXSCORE: " + score + "/" + getMaxScore() + "\n");
		
		for(GdlAction ac : actions) {
			builder.append(ac.toString() + "\n");
		}
		return builder.toString();
	}
	
	@Override
	public void output(GdlActions actions) {
		setActions(actions.getActions());
		setScore(actions.getScore());
		setMaxScore(actions.getMaximumScore());
		System.out.println(generateTextOutput());
	}

}
