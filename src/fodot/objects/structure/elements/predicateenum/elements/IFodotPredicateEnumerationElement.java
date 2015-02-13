package fodot.objects.structure.elements.predicateenum.elements;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import fodot.objects.theory.elements.formulas.FodotPredicate;

public interface IFodotPredicateEnumerationElement extends IFodotEnumerationElement {
	IFodotTypeEnumerationElement getElement(int index);
	FodotPredicate toPredicate();
}
