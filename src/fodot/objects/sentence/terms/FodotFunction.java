package fodot.objects.sentence.terms;

import java.util.List;

import fodot.objects.sentence.formulas.argumented.FodotAbstractArgumentList;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class FodotFunction extends FodotAbstractArgumentList implements IFodotTerm {
	
	public FodotFunction(FodotFunctionDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public String toString() {
		return "[function "+getName()+": " + argumentsToString() + "]";
	}

	@Override
	public FodotType getType() {
		return ((FodotFunctionDeclaration)getDeclaration()).getReturnType();
	}

	
}
