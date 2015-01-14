package fodot.objects.structure.elements.functionenum;

import java.util.Collection;

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
		return getDeclaration().getName() + " = " + CollectionPrinter.toDomain(CollectionPrinter.toCode(getElements()));
	}

	@Override
	public String toString() {
		return "FodotFunctionEnumeration ["+toCode()+ "]";
	}



}