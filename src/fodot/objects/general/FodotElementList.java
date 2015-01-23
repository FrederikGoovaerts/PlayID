package fodot.objects.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class FodotElementList<E extends IFodotElement> extends FodotElement implements IFodotElement {
	private List<E> elements;
	
	public FodotElementList(Collection<? extends E> elements) {
		super();
		setElements(elements);
	}

	/**********************************************
	 *  Elements methods
	 ***********************************************/
	private void setElements(Collection<? extends E> argElements) {
		if (this.elements != null && this.elements.equals(argElements)){
			return;
		}
		this.elements = new ArrayList<E>();
		addAllElements(argElements);
	}

	public List<E> getElements() {
		return new ArrayList<E>(elements);
	}
	
	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return getElements();
	}


	public E getElement(int index) {
		return elements.get(index);
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
	
	public List<? extends IFodotElement> getElementsOfClass(Class<? extends IFodotElement> clazz) {
		if (clazz == null) {
			return new ArrayList<E>();
		}
		List<E> result = new ArrayList<E>();
		for (E el : elements) {
			if (clazz.isInstance(el)) {
				result.add(el);
			}
		}
		return result;
	}
	/**********************************************/

	
	/**********************************************
	 *  Obligatory methods
	 ***********************************************/
	
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
		FodotElementList<?> other = (FodotElementList<?>) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	/**********************************************/


}
