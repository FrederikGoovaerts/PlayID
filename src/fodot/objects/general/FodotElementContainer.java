package fodot.objects.general;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class FodotElementContainer<E extends IFodotElement> extends FodotElementCollection<E> implements IFodotElement {

	public FodotElementContainer(Collection<? extends E> elements) {
		super(elements, -1, -1);
	}

	/**********************************************
	 *  Elements methods
	 ***********************************************/

	@Override
	public Set<E> getElements() {
		return new LinkedHashSet<E>(super.getElements());
	}

	@Override
	protected Set<E> wrapInDefaultCollection(Collection<? extends E> elements) {
		return new LinkedHashSet<E>(elements);
	}
	
	/**********************************************/	

}
