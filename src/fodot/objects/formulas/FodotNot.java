package fodot.objects.formulas;

public class FodotNot extends FodotFormula {
	private FodotFormula formula;

	public FodotNot(FodotFormula formula) {
		super();
		this.formula = formula;
	}

	public FodotFormula getFormula() {
		return formula;
	}
	
	
}
