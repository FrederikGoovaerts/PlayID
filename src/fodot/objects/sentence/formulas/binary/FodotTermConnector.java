package fodot.objects.sentence.formulas.binary;

import java.util.Arrays;
import java.util.List;

import fodot.objects.sentence.terms.FodotTerm;

public class FodotTermConnector extends FodotSentenceElementConnector<FodotTerm> {
	
	public FodotTermConnector(FodotTerm term1,
			String connector, FodotTerm term2) {
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
