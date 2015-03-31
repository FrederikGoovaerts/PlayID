package playid.domain.fodot.theory.elements.terms;

import java.util.ArrayList;
import java.util.List;

import playid.domain.fodot.structure.elements.typeenum.elements.FodotTypeFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.formulas.FodotArgumentList;
import playid.domain.fodot.vocabulary.elements.FodotFunctionDeclaration;
import playid.domain.fodot.vocabulary.elements.FodotType;
import playid.domain.fodot.vocabulary.elements.FodotTypeFunctionDeclaration;

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

	@Override
	public IFodotTypeEnumerationElement toEnumerationElement() {
		if ( !(getDeclaration() instanceof FodotTypeFunctionDeclaration)) {
			throw new RuntimeException("Can't convert a regular function to an enumeration element.");
		}
		List<IFodotTypeEnumerationElement> body = new ArrayList<>();
		for (IFodotTerm term : getArguments()) {
			body.add(term.toEnumerationElement());
		}
		return new FodotTypeFunctionEnumerationElement((FodotTypeFunctionDeclaration) getDeclaration(), body);
	}
}
