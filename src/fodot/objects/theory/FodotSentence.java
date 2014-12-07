package fodot.objects.theory;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.formulas.FodotFormula;

/**
 * A FO(.) sentence is a FO(.) formula with no free occurences of variables
 * Sentences end with a dot.
 */
public class FodotSentence implements IFodotElement {
	public FodotFormula formula;

	public FodotSentence(FodotFormula formula) {
		super();
		setFormula(formula);
	}

	public FodotFormula getFormula() {
		return formula;
	}

	private void setFormula(FodotFormula formula) {
		if (formula.getFreeVariables() != null) {
			throw new NonVariablefreeSentenceException(formula);
		}
		this.formula = formula;
	}

	@Override
	public String toCode() {
		return formula.toCode() + ".";
	}
	
}
