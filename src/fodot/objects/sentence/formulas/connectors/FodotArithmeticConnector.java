package fodot.objects.sentence.formulas.connectors;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.IFodotTerm;

public class FodotArithmeticConnector extends FodotSentenceElementConnector<IFodotTerm> implements IFodotTerm {
	
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"+", "-",  "*", "/", "%"});
	private static final List<String> ASSOCIATIVE_CONNECTORS =
			Arrays.asList(new String[]{"+", "*"});
	
	private FodotArithmeticConnector(String connector, Collection<IFodotTerm> args) {
		super(connector, args);
	}
	
	public FodotArithmeticConnector(IFodotTerm term1, String connector, IFodotTerm term2) {
			this(connector, Arrays.asList(new IFodotTerm[]{term1, term2}));
		}

	@Override
	protected boolean isAssociativeConnector(String connector) {
		return ASSOCIATIVE_CONNECTORS.contains(connector);
	}

	@Override
	public boolean isValidConnector(String connector) {
		return VALID_CONNECTORS.contains(connector);
	}

}
