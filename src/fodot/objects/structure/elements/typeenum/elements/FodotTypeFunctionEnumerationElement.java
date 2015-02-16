package fodot.objects.structure.elements.typeenum.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import fodot.exceptions.fodot.FodotException;
import fodot.objects.general.FodotElementList;
import fodot.objects.general.sorting.FodotElementComparators;
import fodot.objects.theory.elements.terms.FodotConstant;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.elements.FodotTypeFunctionDeclaration;
import fodot.util.CollectionPrinter;

public class FodotTypeFunctionEnumerationElement extends
		FodotElementList<IFodotTypeEnumerationElement> implements
		IFodotTypeEnumerationElement {

	private FodotTypeFunctionDeclaration declaration;
	
	public FodotTypeFunctionEnumerationElement(FodotTypeFunctionDeclaration decl, Collection<? extends IFodotTypeEnumerationElement> elements) {
		super(elements);
		setDeclaration(decl);
	}

	/**********************************************
	 *  Declaration
	 ***********************************************/
	
	public FodotTypeFunctionDeclaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(FodotTypeFunctionDeclaration declaration) {
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
		FodotTypeFunctionEnumerationElement other = (FodotTypeFunctionEnumerationElement) obj;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		return new HashSet<IFodotTypeEnumerationElement>(other.getElements()).equals(new HashSet<IFodotTypeEnumerationElement>(getElements()));
	}

	@Override
	public String toString() {
		return "[FodotTypeFunctionEnumEl : "+toCode()+"]";
	}
	/**********************************************/

	@Override
	public IFodotTerm toTerm() {
		if (getElements() == null || getElements().isEmpty()) {
			return new FodotConstant(getValue(), getDeclaration().getType());
		}
		
		List<IFodotTerm> arguments = new ArrayList<IFodotTerm>();
		for (IFodotTypeEnumerationElement element : getElements()) {
			arguments.add(element.toTerm());
		}
		return new FodotFunction(getDeclaration(), arguments);
	}

}
