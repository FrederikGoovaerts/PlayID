package fodot.objects.theory.elements.formulas;

import java.util.Arrays;
import java.util.List;

import fodot.objects.theory.elements.terms.IFodotTerm;

public class FodotTermComparator extends FodotSentenceElementConnector<IFodotTerm> implements IFodotFormula {
	
	private static final int BINDING_ORDER = 10;
	
	public FodotTermComparator(IFodotTerm term1, String connector, IFodotTerm term2) {
		super(connector, Arrays.asList(term1, term2));
	}
	
	protected FodotTermComparator(String connector, IFodotTerm... terms) {
		super(connector, Arrays.asList(terms));
	}
	
	/* VALID CONNECTORS */
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"=", "~=", "<", ">"});
	
	public boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}

	@Override
	public String toString() {
		return "[termconnector "+toCode()+ "]";
	}

	@Override
	protected boolean isAssociativeConnector(String connector) {
		return false;
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}
}
