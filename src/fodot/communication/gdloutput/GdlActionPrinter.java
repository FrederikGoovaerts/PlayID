package fodot.communication.gdloutput;

import java.util.List;

import fodot.communication.output.GdlAction;

public class GdlActionPrinter implements IActionOutputter {

	private List<GdlAction> actions;
	
	public GdlActionPrinter() {
		super();
	}

	private void setActions(List<GdlAction> actions) {
		this.actions = actions;
	}

	private String generateTextOutput() {
		StringBuilder builder = new StringBuilder();
		for(GdlAction ac : actions) {
			builder.append(ac.toString() + "\n");
		}
		return builder.toString();
	}
	
	@Override
	public void output(List<GdlAction> actions) {
		setActions(actions);
		System.out.println(generateTextOutput());
	}

}
