package fodot.objects.sentence.formulas.quantifiers;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotForAll extends FodotQuantifier {
	
	private static String FORALL_SYMBOL = "!";
	
	public FodotForAll(FodotVariable variable, IFodotFormula formula) {
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
