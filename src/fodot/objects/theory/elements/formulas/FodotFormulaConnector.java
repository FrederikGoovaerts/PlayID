package fodot.objects.theory.elements.formulas;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FodotFormulaConnector extends FodotSentenceElementConnector<IFodotFormula> implements IFodotFormula {

	
	public FodotFormulaConnector(String connector, Collection<? extends IFodotFormula> formulas) {
		super(connector, formulas);
	}
	
	public FodotFormulaConnector(String connector, IFodotFormula... formulas) {
		this(connector, Arrays.asList(formulas));
	}
	
	public FodotFormulaConnector(IFodotFormula formula1, String connector, IFodotFormula formula2) {
		this(connector, Arrays.asList(new IFodotFormula[]{formula1, formula2}));
	}

	/* VALID CONNECTORS */
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"&", "|",  "=>", "<=", "<=>"});
	private static final List<String> ASSOCIATIVE_CONNECTORS =
			Arrays.asList(new String[]{"&", "|"});

	public boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}

	@Override
	public String toString() {
		return "[formulaconnector "+toCode()+ "]";
	}

	@Override
	protected boolean isAssociativeConnector(String connector) {
		return ASSOCIATIVE_CONNECTORS.contains(connector);
	}
	
	

}
