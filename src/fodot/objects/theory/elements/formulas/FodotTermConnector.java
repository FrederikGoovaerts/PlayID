package fodot.objects.theory.elements.formulas;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fodot.objects.theory.elements.terms.IFodotTerm;

public class FodotTermConnector extends FodotSentenceElementConnector<IFodotTerm> implements IFodotFormula {
	
	protected FodotTermConnector(String connector, Collection<IFodotTerm> terms) {
		super(connector, terms);
	}
	
	public FodotTermConnector(IFodotTerm term1, String connector, IFodotTerm term2) {
		this(connector, Arrays.asList(new IFodotTerm[]{term1, term2}));
	}
	
	protected FodotTermConnector(String connector, IFodotTerm... terms) {
		this(connector, Arrays.asList(terms));
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
}
