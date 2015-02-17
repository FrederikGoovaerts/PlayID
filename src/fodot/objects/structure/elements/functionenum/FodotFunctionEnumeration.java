package fodot.objects.structure.elements.functionenum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.general.sorting.FodotElementComparators;
import fodot.objects.structure.elements.FodotEnumeration;
import fodot.objects.structure.elements.functionenum.elements.FodotFunctionEnumerationElement;
import fodot.objects.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import fodot.objects.vocabulary.elements.FodotFunctionFullDeclaration;
import fodot.util.CollectionPrinter;

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