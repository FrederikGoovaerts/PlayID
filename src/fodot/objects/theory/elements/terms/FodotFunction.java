package fodot.objects.theory.elements.terms;

import java.util.List;

import fodot.objects.theory.elements.formulas.FodotAbstractArgumentList;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class FodotFunction extends FodotAbstractArgumentList implements IFodotTerm {
	
	public FodotFunction(FodotFunctionDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public FodotFunctionDeclaration getDeclaration() {
		return ((FodotFunctionDeclaration) super.getDeclaration());
	}
	
	@Override
	public String toString() {
		return "[function "+getName()+": " + argumentsToString() + "]";
	}

	@Override
	public FodotType getType() {
		return getDeclaration().getReturnType();
	}

	
}
