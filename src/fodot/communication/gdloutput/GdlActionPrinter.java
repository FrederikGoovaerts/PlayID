package fodot.communication.gdloutput;

import java.util.List;

import fodot.communication.output.GdlAction;

public class GdlActionPrinter implements IActionOutputter {

	private List<GdlAction> actions;
	
	public GdlActionPrinter(List<GdlAction> actions) {
		setActions(actions);
	}

	@Override
	public void setActions(List<GdlAction> actions) {
		this.actions = actions;
	}

	@Override
	public String getOutput() {
		StringBuilder builder = new StringBuilder();
		for(GdlAction ac : actions) {
			builder.append(ac.toString() + "\n");
		}
		return builder.toString();
	}
	
	@Override
	public void output() {
		System.out.println(getOutput());
	}

}
