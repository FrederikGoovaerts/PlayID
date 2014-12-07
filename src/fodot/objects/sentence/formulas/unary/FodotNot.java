package fodot.objects.sentence.formulas.unary;

import java.util.List;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotNot implements FodotFormula {
	private FodotFormula formula;

	public FodotNot(FodotFormula formula) {
		super();
		this.formula = formula;
	}

	public FodotFormula getFormula() {
		return formula;
	}

	@Override
	public List<FodotVariable> getFreeVariables() {
		return formula.getFreeVariables();
	}

	@Override
	public String toCode() {
		return "~" + formula.toCode();
	}
	@Override
	public String toString() {
		return "[not : " + formula + "]";
	}
	
	
}
