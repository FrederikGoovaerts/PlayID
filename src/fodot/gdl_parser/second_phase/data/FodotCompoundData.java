package fodot.gdl_parser.second_phase.data;

import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;

public class FodotCompoundData extends FodotDefinitionData{
	public FodotCompoundData(FodotPredicate predicate, IFodotFormula formula) {
		super(predicate, formula);
	}
}
