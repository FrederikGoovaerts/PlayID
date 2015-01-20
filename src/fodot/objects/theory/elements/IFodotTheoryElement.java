package fodot.objects.theory.elements;

import java.util.Collection;

import fodot.objects.general.IFodotElement;

public interface IFodotTheoryElement extends IFodotElement{
	Collection<? extends IFodotElement> getElementsOfClass(Class<? extends IFodotElement> clazz);
}
