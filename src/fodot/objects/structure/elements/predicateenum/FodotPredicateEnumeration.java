package fodot.objects.structure.elements.predicateenum;

import java.util.Collection;

import fodot.objects.structure.elements.FodotEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.util.CollectionPrinter;

public class FodotPredicateEnumeration extends FodotEnumeration<IFodotPredicateEnumerationElement> {	
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration declaration, Collection<? extends IFodotPredicateEnumerationElement> elements) {
		super(declaration, elements);
	}	
	
	public FodotPredicateEnumeration(FodotPredicateDeclaration predicate) {
		this(predicate, null);
	}

	public FodotPredicateDeclaration getDeclaration() {
		return (FodotPredicateDeclaration) super.getDeclaration();
	}

	@Override
	public String toCode() {
		return getDeclaration().getName() + " = "+ CollectionPrinter.toDomain(CollectionPrinter.toCode(getElements()));
	}

	@Override
	public String toString() {
		return "FodotPredicateEnumeration ["+toCode()+"]";
	}
		
}
