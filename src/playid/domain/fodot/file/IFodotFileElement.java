package playid.domain.fodot.file;

import java.util.Collection;

import playid.domain.fodot.general.IFodotNamedElement;

public interface IFodotFileElement extends IFodotNamedElement {
	Collection<? extends IFodotFileElement> getPrerequiredElements();
}
