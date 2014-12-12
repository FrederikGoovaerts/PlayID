package fodot.objects.sentence.terms;

import java.util.List;

import fodot.objects.sentence.formulas.argumented.FodotAbstractArgumentList;
import fodot.objects.vocabulary.elements.FodotArgumentListDeclaration;

public class FodotFunction extends FodotAbstractArgumentList implements IFodotTerm {
	
	public FodotFunction(FodotArgumentListDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public String toString() {
		return "[function "+getName()+": " + argumentsToString() + "]";
	}

}
