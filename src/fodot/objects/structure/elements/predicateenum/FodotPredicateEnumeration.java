package fodot.objects.structure.elements.predicateenum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.general.FodotElementComparators;
import fodot.objects.structure.elements.FodotEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.structure.elements.typenum.elements.IFodotTypeEnumerationElement;
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
		List<IFodotPredicateEnumerationElement> domainElements = new ArrayList<IFodotPredicateEnumerationElement>(getElements());
		Collections.sort(domainElements, FodotElementComparators.ENUMERATION_ELEMENT_COMPARATOR);
		return getDeclaration().getName() + " = "+ CollectionPrinter.toDomain(CollectionPrinter.toCode(domainElements));
	}

	@Override
	public String toString() {
		return "FodotPredicateEnumeration ["+toCode()+"]";
	}
		
}
