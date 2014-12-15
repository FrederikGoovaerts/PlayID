package fodot.objects.sentence.terms;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fodot.objects.sentence.formulas.connectors.FodotSentenceElementConnector;
import fodot.objects.vocabulary.elements.FodotType;

public class FodotArithmeticConnector extends FodotSentenceElementConnector<IFodotTerm> implements IFodotTerm {
	
	private static final List<String> VALID_CONNECTORS =
			Arrays.asList(new String[]{"+", "-",  "*", "/", "%"});
	private static final List<String> ASSOCIATIVE_CONNECTORS =
			Arrays.asList(new String[]{"+", "*"});
	
	private FodotArithmeticConnector(String connector, Collection<IFodotTerm> args) {
		super(connector, args);
		
		//All terms must have integer as supertype, you can comment this out if it's too restrictive
		for (IFodotTerm term : args) {
			if (!term.getType().containsSupertype(FodotType.INTEGER)) {
				throw new IllegalArgumentException("You can't use arithmetic on " + term + " because it does not have integer as its superclass");
			}
		}
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

	@Override
	public FodotType getType() {
		return FodotType.INTEGER;
	}

}
