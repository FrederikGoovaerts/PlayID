package fodot.objects.structure.elements.functionenum.elements;

import fodot.objects.structure.elements.IFodotEnumerationElement;
import fodot.objects.theory.elements.terms.FodotFunction;

public interface IFodotFunctionEnumerationElement extends IFodotEnumerationElement {
	FodotFunction toFunction();
}
