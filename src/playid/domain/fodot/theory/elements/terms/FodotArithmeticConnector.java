package playid.domain.fodot.theory.elements.terms;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playid.domain.exceptions.fodot.FodotException;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.formulas.FodotSentenceElementConnector;
import playid.domain.fodot.vocabulary.elements.FodotType;

public class FodotArithmeticConnector extends FodotSentenceElementConnector<IFodotTerm> implements IFodotTerm {
	
	private static final Map<String, Integer> BINDING_ORDERS;
    static {
        Map<String, Integer> connectorsMap = new HashMap<String, Integer>();
	    connectorsMap.put("*", 1);
	    connectorsMap.put("/", 1);
	    connectorsMap.put("%", 1);
	    connectorsMap.put("+", 2);
	    connectorsMap.put("-", 2);
        BINDING_ORDERS = Collections.unmodifiableMap(connectorsMap);
    }
	private static final List<String> ASSOCIATIVE_CONNECTORS =
			Arrays.asList(new String[]{"+", "*"});

	private FodotArithmeticConnector(String connector, List<IFodotTerm> args) {
		super(connector, args);

		//All terms must have integer as supertype, you can comment this out if it's too restrictive
		for (IFodotTerm term : args) {
			if (!(term.getType().isASubtypeOf(FodotType.INTEGER))) {
				throw new FodotException(
						"You can't use arithmetic on " + term + " because it does not have integer as its superclass");
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
		return BINDING_ORDERS.keySet().contains(connector);
	}

	@Override
	public FodotType getType() {
		return FodotType.INTEGER;
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDERS.get(getConnector());
	}
	
	@Override @Deprecated
	public IFodotTypeEnumerationElement toEnumerationElement() {
		throw new RuntimeException("Can't convert an aggregate to enumeration element");
	}
	
}
