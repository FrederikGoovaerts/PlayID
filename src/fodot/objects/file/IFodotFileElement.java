package fodot.objects.file;

import java.util.Collection;

import fodot.objects.general.IFodotNamedElement;

public interface IFodotFileElement extends IFodotNamedElement {
	Collection<? extends IFodotFileElement> getPrerequiredElements();
}
