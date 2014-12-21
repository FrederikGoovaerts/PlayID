package fodot.objects.structure.enumerations;

import fodot.objects.structure.IFodotStructureElement;

public abstract class FodotEnumeration implements IFodotStructureElement {
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
}
