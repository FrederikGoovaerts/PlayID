package fodot.objects.theory.elements.terms;

import java.util.List;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotArgumentList;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.objects.vocabulary.elements.FodotType;

@Deprecated
public class FodotPredicateTerm extends FodotArgumentList
		implements IFodotTerm, IFodotEnumerationElement {

	public FodotPredicateTerm(FodotTypeFunctionDeclaration decl, List<IFodotTerm> arguments) {
		super(decl, arguments);
	}

	@Override
	public FodotTypeFunctionDeclaration getDeclaration() {
		return ((FodotTypeFunctionDeclaration) super.getDeclaration());
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
