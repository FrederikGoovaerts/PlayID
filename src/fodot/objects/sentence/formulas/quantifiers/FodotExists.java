package fodot.objects.sentence.formulas.quantifiers;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotExists extends FodotQuantifier {
private static String FOREACH_SYMBOL = "!";
	
	public FodotExists(FodotVariable variable, FodotFormula formula) {
		super(FOREACH_SYMBOL, variable, formula);
	}

	@Override
	public String toString() {
		return "[foreach "+getVariable()+" in "+getFormula()+"]";
	}

	@Override
	public boolean isValidSymbol(String symbol) {
		return symbol.equals(FOREACH_SYMBOL);
	}
	
}
