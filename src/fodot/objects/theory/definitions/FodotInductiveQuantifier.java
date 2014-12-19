package fodot.objects.theory.definitions;

import java.util.Set;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.quantifiers.FodotQuantifier;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotInductiveQuantifier extends FodotQuantifier implements IFodotInductiveDefinitionElement {

	public FodotInductiveQuantifier(String symbol, Set<FodotVariable> variable,
			IFodotInductiveDefinitionElement formula) {
		super(symbol, variable, formula);
		setShouldShowBrackets(false);
	}
	
}
