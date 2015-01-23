package fodot.communication.gdloutput;

import java.util.List;

import fodot.communication.output.GdlAction;

public interface IActionOutputter {
	void output(List<GdlAction> actions);
}
