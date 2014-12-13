package fodot.util;

import fodot.helpers.FodotPartBuilder;
import fodot.objects.sentence.formulas.IFodotFormula;

public class FormulaUtil {
	
	/**
	 * This method makes a formula variablefree by adding "forall" structures around it
	 * @param formula
	 * @return
	 */
	public static IFodotFormula makeVariableFree(IFodotFormula formula) {
		IFodotFormula newFormula = formula;
		newFormula = FodotPartBuilder.createForAll(formula.getFreeVariables(), newFormula);
		return newFormula;
	}
}
