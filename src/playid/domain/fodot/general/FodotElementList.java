package playid.domain.fodot.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class FodotElementList<E extends IFodotElement> extends FodotElementCollection<E> implements IFodotElement {
	
	public FodotElementList(Collection<? extends E> elements, int minSize, int maxSize) {
		super(elements, minSize, maxSize);
	}

	public FodotElementList(Collection<? extends E> elements, int maxSize) {
		this(elements, -1, maxSize);
	}
	
	public FodotElementList(Collection<? extends E> elements) {
		this(elements, -1, -1);
	}

	/**********************************************
	 *  Elements methods
	 ***********************************************/

	@Override
	public List<E> getElements() {
		return new ArrayList<E>(super.getElements());
	}
	
	public E getElement(int index) {
		return getElements().get(index);
	}
	
	public void setElement(int index, E element) {
		( (List<E>) (super.getElements())).set(index, element);
	}

	@Override
	protected List<E> wrapInDefaultCollection(Collection<? extends E> elements) {
		return new ArrayList<E>(elements);
	}
	
	/**********************************************/


}
