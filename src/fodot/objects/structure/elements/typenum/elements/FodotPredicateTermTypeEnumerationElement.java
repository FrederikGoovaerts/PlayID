package fodot.objects.structure.elements.typenum.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import fodot.exceptions.fodot.FodotException;
import fodot.objects.general.FodotElementContainer;
import fodot.objects.general.sorting.FodotElementComparators;
import fodot.objects.vocabulary.elements.FodotPredicateTermDeclaration;
import fodot.util.CollectionPrinter;

public class FodotPredicateTermTypeEnumerationElement extends
		FodotElementContainer<IFodotTypeEnumerationElement> implements
		IFodotTypeEnumerationElement {

	private FodotPredicateTermDeclaration declaration;
	
	public FodotPredicateTermTypeEnumerationElement(FodotPredicateTermDeclaration decl, Collection<? extends IFodotTypeEnumerationElement> elements) {
		super(elements);
		setDeclaration(decl);
	}

	/**********************************************
	 *  Declaration
	 ***********************************************/
	
	public FodotPredicateTermDeclaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(FodotPredicateTermDeclaration declaration) {
		if (declaration == null) {
			throw new FodotException("Declaration can't be null");
		}
		this.declaration = declaration;
	}
	
	/**********************************************/


	/**********************************************
	 *  Obligatory methods
	 ***********************************************/
	
	@Override
	public String getValue() {
		return toCode();
	}

	@Override
	public String toCode() {
		List<IFodotTypeEnumerationElement> domainElements = new ArrayList<IFodotTypeEnumerationElement>(getElements());
		Collections.sort(domainElements, FodotElementComparators.ENUMERATION_ELEMENT_COMPARATOR);
		return getDeclaration().getName() + CollectionPrinter.toCoupleAsCode(domainElements);
	}

	@Override
	public boolean isValidElement(IFodotTypeEnumerationElement argElement) {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((declaration == null) ? 0 : declaration.hashCode());
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
		FodotPredicateTermTypeEnumerationElement other = (FodotPredicateTermTypeEnumerationElement) obj;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		return new HashSet<IFodotTypeEnumerationElement>(other.getElements()).equals(new HashSet<IFodotTypeEnumerationElement>(getElements()));
	}

	/**********************************************/

	

}
