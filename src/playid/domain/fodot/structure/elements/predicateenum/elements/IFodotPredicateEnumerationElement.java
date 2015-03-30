package playid.domain.fodot.structure.elements.predicateenum.elements;

import playid.domain.fodot.structure.elements.IFodotEnumerationElement;
import playid.domain.fodot.structure.elements.typeenum.elements.IFodotTypeEnumerationElement;
import playid.domain.fodot.theory.elements.formulas.FodotPredicate;

public interface IFodotPredicateEnumerationElement extends IFodotEnumerationElement {
	IFodotTypeEnumerationElement getElement(int index);
	FodotPredicate toPredicate();
}
