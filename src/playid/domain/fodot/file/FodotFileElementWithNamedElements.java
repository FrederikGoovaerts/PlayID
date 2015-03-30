package playid.domain.fodot.file;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import playid.domain.fodot.general.IFodotNamedElement;

public abstract class FodotFileElementWithNamedElements<E extends IFodotNamedElement> extends FodotFileElement<E> {

	//CONSTRUCTORS

	public FodotFileElementWithNamedElements(String name, Collection<? extends E> elements,
			Collection<? extends IFodotFileElement> prerequired) {
		super(name, elements, prerequired);
	}

	public FodotFileElementWithNamedElements(String name, Collection<? extends E> elements,
			IFodotFileElement... prerequired) {
		super(name, elements, Arrays.asList(prerequired));
	}

	public FodotFileElementWithNamedElements(String name, Collection<? extends E> elements) {
		this(name, elements, new HashSet<IFodotFileElement>());
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
	
	public boolean isValidElement(E el) {
		return el != null && (!containsElementWithName(el.getName()) || containsElement(el));
	}

}
