package fodot.objects.theory;

import java.util.List;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class NonVariablefreeSentenceException extends RuntimeException {
	
	public NonVariablefreeSentenceException(FodotFormula formula) {
		super(formula + "is not variablefree: " + formula.getFreeVariables()
				+ "\n Perhaps you should consider using FormulaUtil's 'makeVariableFree' method");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
