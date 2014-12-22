package fodot.objects.file;

import java.util.Set;

import fodot.objects.IFodotElement;

public interface IFodotFileElement extends IFodotElement {
	String getName();
	Set<IFodotFileElement> getPrerequiredElements();
	void merge(IFodotFileElement other);
}
