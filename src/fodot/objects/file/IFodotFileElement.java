package fodot.objects.file;

import java.util.Set;

import fodot.objects.general.IFodotNamedElement;

public interface IFodotFileElement extends IFodotNamedElement {
	Set<IFodotFileElement> getPrerequiredElements();
	void mergeWith(IFodotFileElement other);
}
