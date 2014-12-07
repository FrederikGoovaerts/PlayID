package fodot.objects.util;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.formulas.quantifiers.FodotForAll;
import fodot.objects.sentence.terms.FodotVariable;

public class FormulaUtil {
	
	/**
	 * This method makes a formula variablefree by adding "forall" structures around it
	 * @param formula
	 * @return
	 */
	public static FodotFormula makeVariableFree(FodotFormula formula) {
		FodotFormula newFormula = formula;
		for (FodotVariable var : formula.getFreeVariables()) {
			newFormula = new FodotForAll(var, newFormula);
		}
		return newFormula;
	}
}
