package fodot.objects.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fodot.exceptions.fodot.FodotException;

public abstract class FodotElementCollection<E extends IFodotElement> extends FodotElement implements IFodotElement {
	private int minSize;
	private int maxSize;
	private Collection<E> elements;

	public FodotElementCollection(Collection<? extends E> argElements, int minSize, int maxSize) {
		super();
		setMinSize(minSize);
		setMaxSize(maxSize);
		setElements(argElements);
	}
	
	/**********************************************
	 *  Size methods
	 ***********************************************/
	
	public int getMaxSize() {
		return maxSize;
	}

	private void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMinSize() {
		return minSize;
	}

	private void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	public boolean hasSizeLimit() {
		return maxSize >= 0;
	}

	public int getSize() {
		return elements.size();
	}
	
	public boolean isFull() {
		return getMaxSize() >= 0 && getSize() >= getMaxSize();
	}
	
	public boolean isValid() {
		return (getMaxSize() < 0 || getMaxSize() >= getSize())
				&& getSize() >= getMinSize();
	}
	
	/***********************************************/
	
	
	/**********************************************
	 *  Elements methods
	 ***********************************************/
	protected void setElements(Collection<? extends E> argElements) {
		if (this.elements != null && this.elements.equals(argElements)){
			return;
		}
		this.elements = wrapInDefaultCollection(new ArrayList<E>());
		addAllElements(argElements);
	}

	protected Collection<E> getElements() {
		return elements;
	}
	
	protected abstract Collection<E> wrapInDefaultCollection(Collection<? extends E> elements);
	
	@Override
	public Collection<? extends IFodotElement> getDirectFodotElements() {
		return getElements();
	}

	public void addElement(E argElement) {
		if (!isValidElement(argElement)) {
			throw new FodotException(argElement + " is not a valid argument for " + this);
		
		}
		this.elements.add(argElement);
	}
	
	public void addAllElements(Collection<? extends E> argElements) {
		if (argElements != null) {
			for (E el : argElements) {
				addElement(el);
			}
		}
	}

	public abstract boolean isValidElement(E argElement);

	public boolean containsElement(E element) {
		return this.elements.contains(element);
	}
	
	public boolean containsAllElements(Collection<? extends E> argElements) {
		return this.elements.containsAll(argElements);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		result = prime * result + maxSize;
		result = prime * result + minSize;
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
		FodotElementCollection<?> other = (FodotElementCollection<?>) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		if (maxSize != other.maxSize)
			return false;
		if (minSize != other.minSize)
			return false;
		return true;
	}

	
	
}
