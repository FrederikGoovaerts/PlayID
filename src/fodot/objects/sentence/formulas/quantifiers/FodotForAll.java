package fodot.objects.sentence.formulas.quantifiers;

import java.util.List;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotForAll extends FodotQuantifier {
	
	private static String FORALL_SYMBOL = "!";
	
	public FodotForAll(FodotVariable variable, FodotFormula formula) {
		super(FORALL_SYMBOL, variable, formula);
	}

	@Override
	public String toString() {
		return "[forall "+getVariable()+" in "+getFormula()+"]";
	}

	@Override
	public boolean isValidSymbol(String symbol) {
		return symbol.equals(FORALL_SYMBOL);
	}
}
