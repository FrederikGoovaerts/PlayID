package playid.domain.fodot.theory.elements.formulas;

import java.util.List;

import playid.domain.fodot.theory.elements.terms.IFodotTerm;
import playid.domain.fodot.vocabulary.elements.FodotPredicateDeclaration;

public class FodotPredicate extends FodotArgumentList implements IFodotFormula {
	
	public FodotPredicate(FodotPredicateDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public String toString() {
		return "[predicate "+getName()+": " + argumentsToString() + "]";
	}

}
