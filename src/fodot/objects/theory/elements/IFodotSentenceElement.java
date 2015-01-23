package fodot.objects.theory.elements;

import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.terms.FodotVariable;

public interface IFodotSentenceElement extends IFodotElement {
	Set<FodotVariable> getFreeVariables();
	int getBindingOrder();
	
	@Override
	boolean equals(Object obj);
	
	@Override
	int hashCode();
}
