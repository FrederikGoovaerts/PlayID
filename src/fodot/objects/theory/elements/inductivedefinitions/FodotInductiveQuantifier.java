package fodot.objects.theory.elements.inductivedefinitions;

import java.util.Set;

import fodot.objects.theory.elements.formulas.FodotQuantifier;
import fodot.objects.theory.elements.terms.FodotVariable;

public class FodotInductiveQuantifier extends FodotQuantifier implements IFodotInductiveDefinitionElement {

	private static final int BINDING_ORDER = -1;
	
	public FodotInductiveQuantifier(String symbol, Set<FodotVariable> variable,
			IFodotInductiveDefinitionElement formula) {
		super(symbol, variable, formula);
		setShouldShowBrackets(false);
	}
	
	@Override
	public int getBindingOrder() {
		return BINDING_ORDER;
	}
	
}
