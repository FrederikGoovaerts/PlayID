package fodot.objects.structure.elements.predicateenum.elements;

import java.util.Collection;
import java.util.HashSet;

import fodot.objects.general.FodotElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;

public class FodotPredicateBooleanEnumerationElement extends FodotElement implements IFodotPredicateEnumerationElement {

	private boolean value;
	private FodotPredicateDeclaration declaration;
	
	public FodotPredicateBooleanEnumerationElement(FodotPredicateDeclaration declaration, boolean value) {
		this.declaration = declaration;
		this.value = value;
	}

	@Override
	public String getValue() {
		return value + "";
	}
	
	public FodotPredicateDeclaration getDeclaration() {
		return declaration;
	}

	@Override
	public String toCode() {
		return getValue();
	}

	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return new HashSet<IFodotElement>();
	}

	@Override
	public IFodotTypeEnumerationElement getElement(int index) {
		return null;
	}

	@Override
	public FodotPredicate toPredicate() {
		return null;
	}

}
