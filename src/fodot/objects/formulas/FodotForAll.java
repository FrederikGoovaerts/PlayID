package fodot.objects.formulas;

import fodot.objects.terms.FodotVariable;

public class FodotForAll extends FodotFormula {
	private FodotFormula formula;
	private FodotVariable variable;

	
	public FodotForAll(FodotFormula formula, FodotVariable variable) {
		super();
		this.formula = formula;
		this.variable = variable;
	}

	public FodotFormula getFormula() {
		return formula;
	}

	public FodotVariable getVariable() {
		return variable;
	}
	
}
