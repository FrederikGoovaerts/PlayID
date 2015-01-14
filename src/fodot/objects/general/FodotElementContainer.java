package fodot.objects.general;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class FodotElementContainer<E extends IFodotElement> implements IFodotElement {

	private Set<E> elements;

	public FodotElementContainer(Collection<? extends E> elements) {
		super();
		setElements(elements);
	}

	/**********************************************
	 *  Elements methods
	 ***********************************************/
	protected void setElements(Collection<? extends E> argElements) {
		if (this.elements != null && this.elements.equals(argElements)){
			return;
		}
		this.elements = new LinkedHashSet<E>();
		addAllElements(argElements);
	}

	public Set<E> getElements() {
		return new LinkedHashSet<E>(elements);
	}

	public void addElement(E argElement) {
		if (!isValidElement(argElement)) {
			throw new RuntimeException(argElement + " is not a valid argument for " + this);
		
		}
		this.elements.add(argElement);
	}

	public abstract boolean isValidElement(E argElement);
	
	public void addAllElements(Collection<? extends E> argElements) {
		if (argElements != null) {
			for (E el : argElements) {
				addElement(el);
			}
		}
	}

	public boolean containsElement(E element) {
		return this.elements.contains(element);
	}

	public boolean hasElements() {
		return !elements.isEmpty();
	}

	public void removeElement(E argElement) {
		this.elements.remove(argElement);
	}

	public int getAmountOfElements() {
		return this.elements.size();
	}
	
	public Set<E> getElementsOfClass(Class<?> clazz) {
		if (clazz == null) {
			return new HashSet<E>();
		}
		Set<E> result = new LinkedHashSet<E>();
		for (E el : elements) {
			if (clazz.isInstance(el)) {
				result.add(el);
			}
		}
		return result;
	}
	/**********************************************/


	//Hashcode&Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FodotElementContainer<?> other = (FodotElementContainer<?>) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}
	

}
