package fodot.objects.sentence.terms;

import java.util.List;

import fodot.objects.sentence.formulas.argumented.FodotAbstractArgumentList;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.util.CollectionUtil;

public class FodotPredicateTerm extends FodotAbstractArgumentList
		implements IFodotTerm {

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
		return "FodotPredicateDomainElement ["+getDeclaration().toString()
				+ ", " + CollectionUtil.toCouple(CollectionUtil.toString(getArguments()))+"]";
	}

}
