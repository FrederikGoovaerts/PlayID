package playid.domain.fodot.structure.elements.functionenum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import playid.domain.fodot.general.sorting.FodotElementComparators;
import playid.domain.fodot.structure.elements.FodotEnumeration;
import playid.domain.fodot.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import playid.domain.fodot.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import playid.domain.fodot.vocabulary.elements.FodotFunctionFullDeclaration;
import playid.util.CollectionPrinter;

public class FodotFunctionEnumeration extends FodotEnumeration<FodotFunctionEnumerationElement> {
	
	public FodotFunctionEnumeration(FodotFunctionFullDeclaration declaration, Collection<? extends FodotFunctionEnumerationElement> elements) {
		super(declaration, elements);
	}

	public FodotFunctionEnumeration(FodotFunctionFullDeclaration declaration) {
		this(declaration, null);
	}

	@Override
	public FodotFunctionFullDeclaration getDeclaration() {
		return (FodotFunctionFullDeclaration) super.getDeclaration();
	}


	//SENTENCE ELEMENT

	@Override
	public String toCode() {
		List<IFodotFunctionEnumerationElement> domainElements = new ArrayList<IFodotFunctionEnumerationElement>(getElements());
		Collections.sort(domainElements, FodotElementComparators.ENUMERATION_ELEMENT_COMPARATOR);
		return getDeclaration().getName() + " = " + CollectionPrinter.toDomain(CollectionPrinter.toCode(domainElements));
	}

	@Override
	public String toString() {
		return "FodotFunctionEnumeration ["+toCode()+ "]";
	}



}