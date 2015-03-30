package playid.domain.fodot.theory.elements.terms;

import java.util.List;

import playid.domain.fodot.theory.elements.formulas.FodotArgumentList;
import playid.domain.fodot.vocabulary.elements.FodotFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotType;

public class FodotFunction extends FodotArgumentList implements IFodotTerm {
	
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
		return getDeclaration().getType();
	}

	
}
