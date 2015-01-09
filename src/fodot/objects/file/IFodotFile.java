package fodot.objects.file;

import java.util.Collection;

import fodot.objects.general.IFodotElement;

public interface IFodotFile extends IFodotElement {
	Collection<? extends IFodotFileElement> getElementsOf(Class<?> claz);
	IFodotFileElement getElementWithName(String name);
}
