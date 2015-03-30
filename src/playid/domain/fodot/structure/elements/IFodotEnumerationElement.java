package playid.domain.fodot.structure.elements;

import playid.domain.fodot.general.IFodotElement;

public interface IFodotEnumerationElement extends IFodotElement {
	String getValue();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
