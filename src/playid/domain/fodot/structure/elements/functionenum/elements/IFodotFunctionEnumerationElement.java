package playid.domain.fodot.structure.elements.functionenum.elements;

import playid.domain.fodot.structure.elements.IFodotEnumerationElement;
import playid.domain.fodot.theory.elements.terms.FodotFunction;

public interface IFodotFunctionEnumerationElement extends IFodotEnumerationElement {
	FodotFunction toFunction();
}
