package fodot.objects.structure.elements.functionenum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fodot.objects.general.sorting.FodotElementComparators;
import fodot.objects.structure.elements.FodotEnumeration;
import fodot.objects.structure.elements.functionenum.elements.IFodotFunctionEnumerationElement;
import fodot.objects.vocabulary.elements.FodotFunctionDeclaration;
import fodot.util.CollectionPrinter;

public class FodotFunctionEnumeration extends FodotEnumeration<IFodotFunctionEnumerationElement> {
	
	public FodotFunctionEnumeration(FodotFunctionDeclaration declaration, Collection<? extends IFodotFunctionEnumerationElement> elements) {
		super(declaration, elements);
	}

	public FodotFunctionEnumeration(FodotFunctionDeclaration declaration) {
		this(declaration, null);
	}

	@Override
	public FodotFunctionDeclaration getDeclaration() {
		return (FodotFunctionDeclaration) super.getDeclaration();
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