package fodot.communication.output;

import java.util.ArrayList;
import java.util.List;

public class GdlActions {
	private final List<GdlAction> actions;
	private final int score;
	
	public GdlActions(List<GdlAction> actions, int score) {
		this.actions = new ArrayList<>(actions);
		this.score = score;
	}
	
	public List<GdlAction> getActions() {
		return new ArrayList<>(actions);
	}
	
	public int getScore() {
		return score;
	}
	
	
}
