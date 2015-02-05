package fodot.objects.general;

import java.util.Collection;

import fodot.exceptions.fodot.FodotException;

public abstract class FodotNamedElementContainer<E extends IFodotNamedElement> extends FodotElementContainer<E> {

	public FodotNamedElementContainer(Collection<? extends E> elements, int minSize, int maxSize) {
		super(elements, minSize, maxSize);
	}
	
	public FodotNamedElementContainer(Collection<? extends E> elements) {
		super(elements);
	}
	
	//NAMED STUFF

	public E getElementWithName(String name) {
		if (name != null) {
			for (E el : getElements()) {
				if (name.equals(el.getName())) {
					return el;
				}
			}
		}
		return null;
	}

	public boolean containsElementWithName(String name) {
		return getElementWithName(name) != null;
	}
	
	public boolean containsElementWithNameOfClass(String name, Class<?> clazz) {
		//Check for invalid inputs
		if (clazz == null) {
			throw new FodotException("Class can not be null");
		}
		if (name == null) {
			throw new FodotException("Name can not be null");
		}
		
		//Search
		for (E el : getElements()) {
			if (clazz.isInstance(el) && name.equals(el.getName())) {
				return true;
			}
		}
		return false;
	}
}
