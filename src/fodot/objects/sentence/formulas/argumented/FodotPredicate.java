package fodot.objects.sentence.formulas.argumented;

import java.util.List;

import fodot.objects.sentence.formulas.FodotFormula;
import fodot.objects.sentence.terms.FodotTerm;

public class FodotPredicate extends FodotAbstractArgumentList implements FodotFormula {

	public FodotPredicate(String name, List<FodotTerm> arguments) {
		super(name, arguments);
	}
	
	@Override
	public String toString() {
		return "[predicate "+getName()+": " + argumentsToString() + "]";
	}

}
