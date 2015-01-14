package fodot.objects.theory.elements.terms;

import java.util.List;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotAbstractArgumentList;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class FodotPredicateTerm extends FodotAbstractArgumentList
		implements IFodotTerm, IFodotEnumerationElement {

	public FodotPredicateTerm(FodotPredicateTermDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}

	@Override
	public FodotPredicateTermDeclaration getDeclaration() {
		return ((FodotPredicateTermDeclaration) super.getDeclaration());
	}
	
	@Override
	public FodotType getType() {
		return getDeclaration().getType();
	}

	@Override
	public String toString() {
		return "FodotPredicateTerm ["+toCode()+"]";
	}

	@Override
	public String getValue() {
		return toCode();
	}

}
