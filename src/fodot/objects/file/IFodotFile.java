package fodot.objects.file;

import java.util.Collection;

import fodot.objects.general.IFodotElement;
import fodot.objects.includes.FodotIncludeHolder;

public interface IFodotFile extends IFodotElement {
	void addElement(IFodotFileElement element);
	void addIncludes(FodotIncludeHolder includes);
	@Deprecated
	Collection<? extends IFodotFileElement> getElementsOfClass(Class<?> claz);
	IFodotFileElement getElementWithName(String name);
}
