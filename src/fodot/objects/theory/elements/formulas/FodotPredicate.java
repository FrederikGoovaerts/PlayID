package fodot.objects.theory.elements.formulas;

import java.util.List;

import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;

public class FodotPredicate extends FodotArgumentList implements IFodotFormula {
	
	public FodotPredicate(FodotPredicateDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public String toString() {
		return "[predicate "+getName()+": " + argumentsToString() + "]";
	}

}
