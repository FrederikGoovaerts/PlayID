package playid.domain.fodot.theory.elements.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FodotFormulaConnector extends FodotSentenceElementConnector<IFodotFormula> implements IFodotFormula {

	/**********************************************
	 *  Static map with bindingorder
	 ***********************************************/

	private static final List<String> ASSOCIATIVE_CONNECTORS =
			Arrays.asList(new String[]{"&", "|"});
	
	private static final Map<String, Integer> BINDING_ORDERS;
    static {
        Map<String, Integer> connectorsMap = new HashMap<String, Integer>();
	    connectorsMap.put("&", 21);
	    connectorsMap.put("|", 22);
	    connectorsMap.put("<=", 23);
	    connectorsMap.put("=>", 24);
	    connectorsMap.put("<=>", 25);
        BINDING_ORDERS = Collections.unmodifiableMap(connectorsMap);
    }

	/**********************************************/

	
	/**********************************************
	 *  Constructors
	 ***********************************************/
	public FodotFormulaConnector(String connector, List<? extends IFodotFormula> formulas) {
		super(connector, formulas);
	}
	
	public FodotFormulaConnector(String connector, IFodotFormula... formulas) {
		this(connector, Arrays.asList(formulas));
	}
	
	public FodotFormulaConnector(IFodotFormula formula1, String connector, IFodotFormula formula2) {
		this(connector, Arrays.asList(new IFodotFormula[]{formula1, formula2}));
	}

	/**********************************************/
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDERS.get(getConnector());
	}

	/* VALID CONNECTORS */

	public boolean isValidConnector(String connector) {
		return BINDING_ORDERS.keySet().contains(connector);
	}

	@Override
	public String toString() {
		return "[Formulaconnector: "+toCode()+ "]";
	}

	@Override
	protected boolean isAssociativeConnector(String connector) {
		return ASSOCIATIVE_CONNECTORS.contains(connector);
	}
	
	

}
