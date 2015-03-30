package playid.domain.gdl_transformers.second_phase.data;

import playid.domain.fodot.theory.elements.formulas.FodotPredicate;
import playid.domain.fodot.theory.elements.formulas.IFodotFormula;

public abstract class FodotDefinitionData {
	private FodotPredicate predicate;
	private IFodotFormula formula;
	
	public FodotDefinitionData(FodotPredicate predicate, IFodotFormula formula) {
		super();
		setPredicate(predicate);
		setFormula(formula);
	}
	
	public FodotPredicate getPredicate() {
		return predicate;
	}
	public void setPredicate(FodotPredicate predicate) {
		this.predicate = predicate;
	}
	public IFodotFormula getFormula() {
		return formula;
	}
	public void setFormula(IFodotFormula formula) {
		this.formula = formula;
	}
}
