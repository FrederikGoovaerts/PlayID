package fodot.objects.sentence.formulas.binary;

import java.util.Arrays;
import java.util.List;

import fodot.objects.sentence.formulas.FodotFormula;

public class FodotFormulaConnector extends FodotSentenceElementConnector<FodotFormula> {
	
	public FodotFormulaConnector(FodotFormula formula1,
			String connector, FodotFormula formula2) {
		super(formula1, connector, formula2);
	}

	/* VALID CONNECTORS */
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"&", "|",  "=>", "<=", "<=>"});
	
	public boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}

	@Override
	public String toString() {
		return "[formulaconnector "+getConnector()+":" + getArgument1() + ", " + getArgument2() + "]";
		}
	
}
