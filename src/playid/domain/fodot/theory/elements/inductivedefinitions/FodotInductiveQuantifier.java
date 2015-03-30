package playid.domain.fodot.theory.elements.inductivedefinitions;

import java.util.Set;

import playid.domain.fodot.theory.elements.formulas.FodotQuantifier;
import playid.domain.fodot.theory.elements.terms.FodotVariable;

public class FodotInductiveQuantifier extends FodotQuantifier implements IFodotInductiveDefinitionElement {

	private static final int BINDING_ORDER = -1;
	
	public FodotInductiveQuantifier(String symbol, Set<FodotVariable> variable,
			IFodotInductiveDefinitionElement formula) {
		super(symbol, variable, formula);
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}
	
}
