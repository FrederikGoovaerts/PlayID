package playid.domain.fodot.structure.elements.typeenum.elements;

import playid.domain.fodot.structure.elements.IFodotEnumerationElement;
import playid.domain.fodot.theory.elements.terms.IFodotTerm;

public interface IFodotTypeEnumerationElement extends IFodotEnumerationElement {
	IFodotTerm toTerm();	
}
