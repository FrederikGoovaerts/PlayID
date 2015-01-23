package fodot.objects.structure.elements.predicateenum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.general.sorting.FodotElementComparators;
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
		List<IFodotPredicateEnumerationElement> domainElements = new ArrayList<IFodotPredicateEnumerationElement>(getElements());
		Collections.sort(domainElements, FodotElementComparators.ENUMERATION_ELEMENT_COMPARATOR);
		return getDeclaration().getName() + " = "+ CollectionPrinter.toDomain(CollectionPrinter.toCode(domainElements));
	}

	@Override
	public String toString() {
		return "[FodotPredicateEnumeration "+toCode()+"]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((getDeclaration() == null) ? 0 : getDeclaration().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotPredicateEnumeration other = (FodotPredicateEnumeration) obj;
		if (getDeclaration() == null) {
			if (other.getDeclaration() != null)
				return false;
		} else if (!getDeclaration().equals(other.getDeclaration()))
			return false;
		if (getElements() == null) {
			if (other.getElements() != null)
				return false;
		} else if (!getElements().equals(other.getElements()))
			return false;
		return true;
	}	
}
