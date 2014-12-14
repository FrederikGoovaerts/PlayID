package fodot.util;

import fodot.helpers.FodotPartBuilder;
import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

import java.util.Set;

public class FormulaUtil {
	
	/**
	 * This method makes a formula variablefree by adding "forall" structures around it
	 * @param formula
	 * @return
	 */
	public static IFodotFormula makeVariableFree(IFodotFormula formula) {
		IFodotFormula newFormula = formula;
		Set<FodotVariable> freeVars = formula.getFreeVariables();
		if(!freeVars.isEmpty()) {
			newFormula = FodotPartBuilder.createForAll(freeVars, newFormula);
		}
		return newFormula;
	}
}
