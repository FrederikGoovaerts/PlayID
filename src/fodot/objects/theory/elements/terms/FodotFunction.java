package fodot.objects.theory.elements.terms;

import java.util.List;

import fodot.objects.theory.elements.formulas.FodotAbstractArgumentList;
import fodot.objects.vocabulary.elements.FodotFunctionFullDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

public class FodotFunction extends FodotAbstractArgumentList implements IFodotTerm {
	
	public FodotFunction(FodotFunctionFullDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}
	
	@Override
	public FodotFunctionFullDeclaration getDeclaration() {
		return ((FodotFunctionFullDeclaration) super.getDeclaration());
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
