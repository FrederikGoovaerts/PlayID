package fodot.objects.formulas;

import java.util.Arrays;
import java.util.List;

public class FodotFormulaConnector extends FodotFormula {
	private FodotFormula formula1;
	private FodotFormula formula2;
	private String connector;
	
	public FodotFormulaConnector(FodotFormula formula1,
			String connector, FodotFormula formula2) {
		super();
		this.formula1 = formula1;
		this.formula2 = formula2;
		this.connector = connector;
	}
	
	public FodotFormula getFormula1() {
		return formula1;
	}

	public FodotFormula getFormula2() {
		return formula2;
	}

	public String getConnector() {
		return connector;
	}


	/* VALID CONNECTORS */
	
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"&", "|",  "=>", "<=", "<=>"});
	
	public static boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}
}
