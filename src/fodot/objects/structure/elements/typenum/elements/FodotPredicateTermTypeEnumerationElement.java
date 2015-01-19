package fodot.objects.structure.elements.typenum.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.general.FodotElementComparators;
import fodot.objects.general.FodotElementContainer;
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
			throw new IllegalArgumentException("Declaration can't be null");
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

	/**********************************************/


}
