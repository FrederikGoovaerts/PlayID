package fodot.objects.sentence.formulas.argumented;

import java.util.List;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;

public class FodotPredicate extends FodotAbstractArgumentList implements IFodotFormula {

	public FodotPredicate(FodotPredicateDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public String toString() {
		return "[predicate "+getName()+": " + argumentsToString() + "]";
	}

}
