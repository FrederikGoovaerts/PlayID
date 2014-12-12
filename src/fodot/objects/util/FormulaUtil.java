package fodot.objects.util;

import java.util.ArrayList;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.formulas.quantifiers.FodotForAll;
import fodot.objects.sentence.terms.FodotVariable;

public class FormulaUtil {
	
	/**
	 * This method makes a formula variablefree by adding "forall" structures around it
	 * @param formula
	 * @return
	 */
	public static IFodotFormula makeVariableFree(IFodotFormula formula) {
		IFodotFormula newFormula = formula;
		newFormula = new FodotForAll(new ArrayList<FodotVariable>(formula.getFreeVariables()), newFormula);
		return newFormula;
	}
}
