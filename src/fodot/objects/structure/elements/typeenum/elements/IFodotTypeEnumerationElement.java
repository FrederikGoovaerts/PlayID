package fodot.objects.structure.elements.typeenum.elements;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.theory.elements.terms.IFodotTerm;

public interface IFodotTypeEnumerationElement extends IFodotEnumerationElement {
	IFodotTerm toTerm();	
}
