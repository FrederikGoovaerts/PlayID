package fodot.objects.structure.enumerations;

import fodot.objects.IFodotElement;

public abstract class FodotEnumeration implements IFodotElement {
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
}
