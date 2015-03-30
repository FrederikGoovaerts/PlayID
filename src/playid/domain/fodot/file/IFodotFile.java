package playid.domain.fodot.file;

import playid.domain.fodot.general.IFodotElement;
import playid.domain.fodot.includes.FodotIncludeHolder;

public interface IFodotFile extends IFodotElement {
	void addElement(IFodotFileElement element);
	void addIncludes(FodotIncludeHolder includes);
	IFodotFileElement getElementWithName(String name);
}
