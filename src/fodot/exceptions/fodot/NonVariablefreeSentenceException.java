package fodot.exceptions.fodot;

import fodot.objects.theory.elements.formulas.IFodotFormula;

public class NonVariablefreeSentenceException extends FodotException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3375443583981724188L;

	public NonVariablefreeSentenceException(IFodotFormula formula) {
		super("\n" + formula + "is not variablefree: \n" + formula.getFreeVariables()
				+ "\n Perhaps you should consider using FormulaUtil's 'makeVariableFree' method");
	}


}
