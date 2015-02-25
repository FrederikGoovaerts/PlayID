package fodot.objects.structure.elements.predicateenum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.exceptions.fodot.FodotException;
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
        StringBuilder builder = new StringBuilder();

        List<IFodotPredicateEnumerationElement> domainElements = new ArrayList<IFodotPredicateEnumerationElement>(getElements());
        Collections.sort(domainElements, FodotElementComparators.ENUMERATION_ELEMENT_COMPARATOR);

        builder.append(getDeclaration().getName());
        if(certFalse){
            builder.append("<cf>");
        } else if(certTrue){
            builder.append("<ct>");
        }
        builder.append(" = " + CollectionPrinter.toDomain(CollectionPrinter.toCode(domainElements)));
        return builder.toString();
	}

    /**********************************************
     *  Certainly true of certainly false
     ***********************************************/

    private boolean certTrue = false;

    public void setCT(){
        if(certFalse) {
            throw new FodotException("Cannot be <ct> and <cf> at the same time!");
        }
        certTrue = true;
    }

    private boolean certFalse = false;

    public void setCF(){
        if(certTrue) {
            throw new FodotException("Cannot be <ct> and <cf> at the same time!");
        }
        certFalse = true;
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
