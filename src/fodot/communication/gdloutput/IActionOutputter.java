package fodot.communication.gdloutput;

import java.util.List;

import fodot.communication.output.GdlAction;

public interface IActionOutputter {
	void setActions(List<GdlAction> actions);
	String getOutput();
	void output();
}
