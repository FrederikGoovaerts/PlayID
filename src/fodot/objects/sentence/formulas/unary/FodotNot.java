package fodot.objects.sentence.formulas.unary;

import java.util.List;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.FodotVariable;

public class FodotNot implements IFodotFormula {
	private IFodotFormula formula;

	public FodotNot(IFodotFormula formula) {
		super();
		this.formula = formula;
	}

	public IFodotFormula getFormula() {
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
