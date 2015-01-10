package fodot.objects.theory.elements;

import java.util.Collection;
import java.util.Set;

import fodot.objects.general.IFodotElement;
import fodot.objects.theory.elements.terms.FodotVariable;

public interface IFodotSentenceElement extends IFodotElement {
	Collection<? extends IFodotSentenceElement> getElementsOfClass(Class<? extends IFodotSentenceElement> clazz);
	Set<FodotVariable> getFreeVariables();
	int getBindingOrder();
	
	@Override
	boolean equals(Object obj);
	
	@Override
	int hashCode();
}
