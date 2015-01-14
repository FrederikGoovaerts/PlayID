package fodot.objects.structure.elements;

import fodot.objects.general.IFodotElement;

public interface IFodotEnumerationElement extends IFodotElement {
	String getValue();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}