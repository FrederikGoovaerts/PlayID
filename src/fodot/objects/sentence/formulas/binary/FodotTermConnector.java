package fodot.objects.sentence.formulas.binary;

import java.util.Arrays;
import java.util.List;

import fodot.objects.sentence.terms.IFodotTerm;

public class FodotTermConnector extends FodotSentenceElementConnector<IFodotTerm> {
	
	public FodotTermConnector(IFodotTerm term1,
			String connector, IFodotTerm term2) {
		super(term1, connector, term2);
	}
	
	/* VALID CONNECTORS */
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"=", "~="});
	
	public boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}

	@Override
	public String toString() {
		return "[termconnector "+getConnector()+":" + getArgument1() + ", " + getArgument2() + "]";
	}
}
