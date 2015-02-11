package fodot.objects.structure.elements.predicateenum.elements;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;

public interface IFodotPredicateEnumerationElement extends IFodotEnumerationElement {
	IFodotTypeEnumerationElement getElement(int index);
}
