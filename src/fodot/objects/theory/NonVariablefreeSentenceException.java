package fodot.objects.theory;

import fodot.objects.sentence.formulas.IFodotFormula;

public class NonVariablefreeSentenceException extends RuntimeException {
	
	public NonVariablefreeSentenceException(IFodotFormula formula) {
		super(formula + "is not variablefree: " + formula.getFreeVariables()
				+ "\n Perhaps you should consider using FormulaUtil's 'makeVariableFree' method");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
