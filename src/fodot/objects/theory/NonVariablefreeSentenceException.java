package fodot.objects.theory;

import fodot.objects.sentence.formulas.FodotFormula;

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
