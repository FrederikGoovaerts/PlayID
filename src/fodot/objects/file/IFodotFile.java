package fodot.objects.file;

import fodot.objects.general.IFodotElement;
import fodot.objects.includes.FodotIncludeHolder;

public interface IFodotFile extends IFodotElement {
	void addElement(IFodotFileElement element);
	void addIncludes(FodotIncludeHolder includes);
	IFodotFileElement getElementWithName(String name);
}
