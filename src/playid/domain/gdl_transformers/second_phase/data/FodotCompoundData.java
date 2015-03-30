package playid.domain.gdl_transformers.second_phase.data;

import playid.domain.fodot.theory.elements.formulas.FodotPredicate;
import playid.domain.fodot.theory.elements.formulas.IFodotFormula;

public class FodotCompoundData extends FodotDefinitionData{
	public FodotCompoundData(FodotPredicate predicate, IFodotFormula formula) {
		super(predicate, formula);
	}
}
