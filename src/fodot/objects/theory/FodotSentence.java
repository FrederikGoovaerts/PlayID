package fodot.objects.theory;

import fodot.objects.formulas.FodotFormula;

/**
 * A FO(.) sentence is a FO(.) formula with no free occurences of variables
 * Sentences end with a dot.
 */
public class FodotSentence {
	public FodotFormula formula;

	public FodotSentence(FodotFormula formula) {
		super();
		setFormula(formula);
	}

	public FodotFormula getFormula() {
		return formula;
	}

	private void setFormula(FodotFormula formula) {
		this.formula = formula;
	}
	
}
