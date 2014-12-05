package fodot.objects.formulas;

import java.util.Arrays;
import java.util.List;

import fodot.objects.terms.FodotTerm;

public class FodotTermConnector extends FodotFormula {
	private FodotTerm term1;
	private FodotTerm term2;
	private String connector;
	
	public FodotTermConnector(FodotTerm term1,
			String connector, FodotTerm term2) {
		super();
		this.term1 = term1;
		this.term2 = term2;
		this.connector = connector;
	}
	
	public FodotTerm getTerm1() {
		return term1;
	}

	public FodotTerm getTerm2() {
		return term2;
	}

	public String getConnector() {
		return connector;
	}

	/* VALID CONNECTORS */
	
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"=", "~="});
	
	public static boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}
}
