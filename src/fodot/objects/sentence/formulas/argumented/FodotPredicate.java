package fodot.objects.sentence.formulas.argumented;

import java.util.List;

import fodot.objects.sentence.formulas.IFodotFormula;
import fodot.objects.sentence.terms.IFodotTerm;

public class FodotPredicate extends FodotAbstractArgumentList implements IFodotFormula {

	//TODO: zouden we hier niet een predicateDeclaration aannemen en diens name
	// naar boven doorgooien? Dan heb je ook een check of de argumenten aan
	// de arity voldoen.
	public FodotPredicate(String name, List<IFodotTerm> arguments) {
		super(name, arguments);
	}
	
	@Override
	public String toString() {
		return "[predicate "+getName()+": " + argumentsToString() + "]";
	}

}
