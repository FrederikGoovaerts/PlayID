package fodot.objects.theory;

import fodot.objects.IFodotElement;
import fodot.objects.sentence.formulas.IFodotFormula;

/**
 * A FO(.) sentence is a FO(.) formula with no free occurences of variables
 * Sentences end with a dot.
 */
public class FodotSentence implements IFodotElement {
	public IFodotFormula formula;

	public FodotSentence(IFodotFormula formula) {
		super();
		setFormula(formula);
	}

	public IFodotFormula getFormula() {
		return formula;
	}

	private void setFormula(IFodotFormula formula) {
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
